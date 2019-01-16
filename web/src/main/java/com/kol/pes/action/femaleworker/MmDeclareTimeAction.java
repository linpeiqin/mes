/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.pojo.DeclareTimeReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class MmDeclareTimeAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String declareTimeId;


	private String organizationId;		//组织ID
	private String jobTransactionId;		//生产任务ID
	private String operationSeqNum;		//标准工序
	private String niOperationCode;		//女工工序编号
	private String niOperationDesc;		//女工工序描述
	private String assetCode;		//设备编号
	private String quantity;				//人数
	private String startPullTime;				//开拉时间
	private String endPullTime;				//收拉时间
	private String workTime;				//总工时
	private String transactionUom;				//事务单位
	private String perQuantity;				//每小时产量
	private String subgoodsQuantity;				//半制品数
	private String goodsQuantity;				//成品数
	private String goodsWasteQuantity;				//生产坏货数
	private String returnWasteQuantity;				//返工坏货数
	private String inputQuantity;				//已导入好货数
	private String wasteInputQuantity;		//已导入坏货数
	private String reasonCode;					//坏货原因代码
	private String reasonRemark;				//坏货原因说明

	
	@Override
	@Action(value="/declare_time_finish_4f", results={
			@Result(name="success", location="declare_time_finish_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		DeclareTimeReq declareTimeReq = new DeclareTimeReq();
		initData(declareTimeReq);
		String codes[] = seqService.declareTimeFinish(declareTimeReq);
		Integer codeI = Integer.valueOf(codes[0]);
		String  codeMsg = codes[1];
		if(codeI < 0) {
			setCode(CODE_FAIL);
			setMessage(getText(codeMsg));
			return ERROR;
		}
		this.declareTimeId = String.valueOf(codeI);
		return SUCCESS;
	}
	private void initData(DeclareTimeReq declareTime){
		declareTime.organizationId = organizationId;
		declareTime.jobTransactionId = jobTransactionId;
		declareTime.operationSeqNum = operationSeqNum;
		declareTime.niOperationCode = niOperationCode;
		declareTime.niOperationDesc = niOperationDesc;
		declareTime.assetCode = assetCode;
		declareTime.quantity = quantity;
		declareTime.startPullTime = startPullTime;
		declareTime.endPullTime = endPullTime;
		declareTime.workTime = workTime;
		declareTime.transactionUom = transactionUom;
		declareTime.perQuantity = perQuantity;
		declareTime.subgoodsQuantity = subgoodsQuantity;
		declareTime.goodsQuantity = goodsQuantity;
		declareTime.goodsWasteQuantity = goodsWasteQuantity;
		declareTime.returnWasteQuantity = returnWasteQuantity;
		declareTime.inputQuantity = inputQuantity;
		declareTime.wasteInputQuantity = wasteInputQuantity;
		declareTime.reasonCode = reasonCode;
		declareTime.reasonRemark = reasonRemark;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setJobTransactionId(String jobTransactionId) {
		this.jobTransactionId = jobTransactionId;
	}

	public void setOperationSeqNum(String operationSeqNum) {
		this.operationSeqNum = operationSeqNum;
	}

	public void setNiOperationCode(String niOperationCode) {
		this.niOperationCode = niOperationCode;
	}

	public void setNiOperationDesc(String niOperationDesc) {
		this.niOperationDesc = niOperationDesc;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setStartPullTime(String startPullTime) {
		this.startPullTime = startPullTime;
	}

	public void setEndPullTime(String endPullTime) {
		this.endPullTime = endPullTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public void setTransactionUom(String transactionUom) {
		this.transactionUom = transactionUom;
	}

	public void setPerQuantity(String perQuantity) {
		this.perQuantity = perQuantity;
	}

	public void setSubgoodsQuantity(String subgoodsQuantity) {
		this.subgoodsQuantity = subgoodsQuantity;
	}

	public void setGoodsQuantity(String goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}

	public void setGoodsWasteQuantity(String goodsWasteQuantity) {
		this.goodsWasteQuantity = goodsWasteQuantity;
	}

	public void setReturnWasteQuantity(String returnWasteQuantity) {
		this.returnWasteQuantity = returnWasteQuantity;
	}

	public void setInputQuantity(String inputQuantity) {
		this.inputQuantity = inputQuantity;
	}

	public void setWasteInputQuantity(String wasteInputQuantity) {
		this.wasteInputQuantity = wasteInputQuantity;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public void setReasonRemark(String reasonRemark) {
		this.reasonRemark = reasonRemark;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getDeclareTimeId() {
		return declareTimeId;
	}
}
