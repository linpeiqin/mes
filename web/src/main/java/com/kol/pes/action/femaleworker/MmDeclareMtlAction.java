/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.pojo.DeclareMtlReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class MmDeclareMtlAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String declareMtlId;

	private String organizationId;		//组织ID
	private String jobTransactionId;		//生产任务ID
	private String moveTransactionId;		//生产状况ID
	private String inventoryItemId;		//物料ID
	private String transactionUom;		//单位
	private String transactionQuantity;		//实际用量
	private String remark;		//备注
	private String transactionItemType;		//标准/新增：BOM/SPEC
	private String transactionType;		//报数类型：DECLARE(使用)/WITHDRAWAL(退回)

	
	@Override
	@Action(value="/declare_mtl_finish_4f", results={
			@Result(name="success", location="declare_mtl_finish_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		DeclareMtlReq declareMtlReq = new DeclareMtlReq();
		initData(declareMtlReq);
		String codes[] = seqService.declareMtlFinish(declareMtlReq);
		Integer codeI = Integer.valueOf(codes[0]);
		String  codeMsg = codes[1];
		if(codeI < 0) {
			setCode(CODE_FAIL);
			setMessage(getText(codeMsg));
			return ERROR;
		}
		this.declareMtlId = String.valueOf(codeI);
		return SUCCESS;
	}
	private void initData(DeclareMtlReq declareMtlReq){
		declareMtlReq.organizationId = organizationId;
		declareMtlReq.jobTransactionId = jobTransactionId;
		declareMtlReq.moveTransactionId = moveTransactionId;
		declareMtlReq.inventoryItemId = inventoryItemId;
		declareMtlReq.transactionUom = transactionUom;
		declareMtlReq.transactionQuantity = transactionQuantity;
		declareMtlReq.remark = remark;
		declareMtlReq.transactionItemType = transactionItemType;
		declareMtlReq.transactionType = transactionType;

	}

	public String getDeclareMtlId() {
		return declareMtlId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setJobTransactionId(String jobTransactionId) {
		this.jobTransactionId = jobTransactionId;
	}

	public void setMoveTransactionId(String moveTransactionId) {
		this.moveTransactionId = moveTransactionId;
	}

	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}

	public void setTransactionUom(String transactionUom) {
		this.transactionUom = transactionUom;
	}

	public void setTransactionQuantity(String transactionQuantity) {
		this.transactionQuantity = transactionQuantity;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setTransactionItemType(String transactionItemType) {
		this.transactionItemType = transactionItemType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
