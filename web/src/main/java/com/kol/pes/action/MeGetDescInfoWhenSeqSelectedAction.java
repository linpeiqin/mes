package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetDescInfoWhenSeqSelectedAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String wipId = null;
	private String opCode = null;
	private String seqNum = null;
	private String assetId = null;
	private String schedDate = null;
	
	private String pAfterOp;
	private String staffNo;
	
	private DataMeProcedure descInfo;

	@Override
	@Action(value="/get_desc_info_when_seq_selected", results={
			@Result(name="success", location="get_desc_info_when_seq_selected.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		descInfo = mesService.getDescInfoWhenSeqSelected(wipId, opCode, assetId, schedDate, pAfterOp, staffNo, seqNum);
		
		LogUtil.log("wipId="+this.wipId);
		
		if(descInfo!=null && descInfo.errorCode>=0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取工序信息失败");
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
	
	public void setPAfterOp(String pAfterOp) {
		this.pAfterOp = pAfterOp;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public DataMeProcedure getDescInfo() {
		return this.descInfo;
	}
	
}
