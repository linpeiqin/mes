/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.DataGroupInfoItem;
import com.kol.pes.item.femaleworker.DataOrgInfoItem;
import com.kol.pes.service.MesService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmGetOrgListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585199L;
	
	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private List<DataOrgInfoItem> orgList;

	@Override
	@Action(value="/get_org_list_4f", results={
			@Result(name="success", location="get_org_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		this.orgList = mesService.getOrgList();
		
		if(orgList == null) {
			
			setCode(CODE_FAIL);
			setMessage(getText("获取组织信息失败"));//获取组织信息失败
			
			return ERROR;
		}
		
		return SUCCESS;
	}


	public List<DataOrgInfoItem> getOrgList() {
		return orgList;
	}
}
