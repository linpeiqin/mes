/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.DataProcessItem;
import com.kol.pes.item.femaleworker.MtlPlanInfoItem;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmGetMtlPlanListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String wipEntityId;
	private String operationSeqNum;
	private String organizationId;

	private List<MtlPlanInfoItem> mtlPlanList;//获取的工序列表
	
	@Override
	@Action(value="/get_mtl_plan_list_4f", results={
			@Result(name="success", location="get_mtl_plan_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {

		this.mtlPlanList = seqService.getMtlPlanList(wipEntityId, operationSeqNum, organizationId);//获取计划物料列表
		
		if(mtlPlanList==null) {
			setCode(CODE_FAIL);
			setMessage("获取计划物料失败");
			return ERROR;
		}

		if (mtlPlanList.size()==0){
			setCode(CODE_FAIL);
			setMessage("该工序没有计划物料");
			return ERROR;
		}

		return SUCCESS;
	}


	public List<MtlPlanInfoItem> getMtlPlanList() {
		return mtlPlanList;
	}

	public void setWipEntityId(String wipEntityId) {
		this.wipEntityId = wipEntityId;
	}

	public void setOperationSeqNum(String operationSeqNum) {
		this.operationSeqNum = operationSeqNum;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
}
