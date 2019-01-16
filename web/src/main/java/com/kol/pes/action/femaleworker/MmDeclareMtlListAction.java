/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.DeclareMtlInfoItem;
import com.kol.pes.item.femaleworker.DeclareTimeInfoItem;
import com.kol.pes.pojo.DeclareMtlListReq;
import com.kol.pes.pojo.DeclareTimeListReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmDeclareMtlListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;

	private String organizationId;		//组织ID
	private String transactionType;		//组织ID
	private String jobTransactionId;		//组织ID
	private String moveTransactionId;		//


	private List<DeclareMtlInfoItem> declareMtlList;//获取的物料列表

	@Override
	@Action(value="/get_declare_mtl_list_4f", results={
			@Result(name="success", location="get_declare_mtl_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		DeclareMtlListReq declareMtlListReq = new DeclareMtlListReq();
		initData(declareMtlListReq);
		this.declareMtlList = seqService.getDeclareMtlList(declareMtlListReq);

		if(declareMtlList == null) {

			setCode(CODE_FAIL);
			setMessage(getText("获取物料列表失败"));//获取物料列表失败

			return ERROR;
		}
		if(declareMtlList.size() == 0) {

			setCode(CODE_FAIL);
			setMessage(getText("请填写物料"));//请填写物料

			return ERROR;
		}
		return SUCCESS;
	}
	private void initData(DeclareMtlListReq declareMtlListReq){
		declareMtlListReq.organizationId = organizationId;
		declareMtlListReq.transactionType = transactionType;
		declareMtlListReq.jobTransactionId = jobTransactionId;
		declareMtlListReq.moveTransactionId = moveTransactionId;
	}

	public List<DeclareMtlInfoItem> getDeclareMtlList() {
		return declareMtlList;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setJobTransactionId(String jobTransactionId) {
		this.jobTransactionId = jobTransactionId;
	}

	public void setMoveTransactionId(String moveTransactionId) {
		this.moveTransactionId = moveTransactionId;
	}
}
