package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeStartSeqAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String wipId = null;
	private String opCode = null;
	private String seqNum = null;
	private String assetId = null;
	private String schedDate = null;
	
	private String staffNo = null;
	private String inputQty = null;
	private String startOpTime = null;
	
	private String pAfterOp;
	private String pafteropSeqNum;
	private String workClassCode;
	
	private DataMeProcedure resData;

	@Override
	@Action(value="/start_seq", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		resData = mesService.startSeq(staffNo, inputQty, startOpTime, wipId, opCode, assetId, schedDate, pAfterOp, seqNum, pafteropSeqNum, workClassCode);
		
		LogUtil.log("assetId="+this.assetId);
		
		if(resData!=null && resData.errorCode>=0) {
			setMessage("成功开启工序");
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("开启工序失败");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public void setSchedDate(String schedDate) {
		this.schedDate = schedDate;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public void setInputQty(String inputQty) {
		this.inputQty = inputQty;
	}
	
	public void setStartOpTime(String startOpTime) {
		this.startOpTime = startOpTime;
	}
	
	public void setPAfterOp(String pAfterOp) {
		this.pAfterOp = pAfterOp;
	}
	
	public void setPafteropSeqNum(String pafteropSeqNum) {
		this.pafteropSeqNum = pafteropSeqNum;
	}
	
	public DataMeProcedure getResData() {
		return this.resData;
	}
	
}
