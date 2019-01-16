/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.DataGroupInfoItem;
import com.kol.pes.item.femaleworker.DataWipInfoItem;
import com.kol.pes.service.MesService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmGetWipListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585199L;
	
	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private List<DataWipInfoItem> wipList;
	private String jobNo;
	private String organizationId;

	@Override
	@Action(value="/get_wip_list_4f", results={
			@Result(name="success", location="get_wip_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		this.wipList = mesService.getWipList4F(jobNo,organizationId);
		
		if(wipList == null) {
			
			setCode(CODE_FAIL);
			setMessage(getText("获取生产单信息失败"));
			
			return ERROR;
		}
		if(wipList.size() ==  0) {

			setCode(CODE_FAIL);
			setMessage(getText("找不到该工程单或工程单没有生产单信息"));

			return ERROR;
		}
		
		return SUCCESS;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public List<DataWipInfoItem> getWipList() {
		return wipList;
	}
}
