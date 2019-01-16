package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.service.MeMaterialsReportService;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;


public class MeMaterialsReportCompleteAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("materialsReportService")
	private MeMaterialsReportService materialsReportService;

	
	String assetCode; 
	String staffNo;
	String reportType; 
	String wipId; 
	String opCode; 
	String seqNum;
	String itemId;
	String trxQty; 
	String remark;
	String schedDate;

	@Override
	@Action(value="/materials_report_complete", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		DataCodeMessageItem res = materialsReportService.materialsReportComplete(assetCode, staffNo, reportType, wipId, opCode, seqNum, itemId, trxQty, remark, schedDate);
		
		LogUtil.log("MeMaterialsReportCompleteAction:wipId="+this.wipId);
		
		if(res!=null && res.code < 2) {
			setCode(CODE_SUCCESS);
			if (res.code == 0) {
				setMessage("完成物料报数成功"); }
			else { 
				setMessage(CommonUtil.isStringNotNull(res.message)? res.message:"完成物料报数成功"); 
			}
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage(CommonUtil.isStringNotNull(res.message) ? res.message:"完成物料报数失败");
			return ERROR;
		}
	}
	

	
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
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
	
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public void setTrxQty(String trxQty) {
		this.trxQty = trxQty;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public void setSchedDate(String schedDate) {
		this.schedDate = schedDate;
	}
	
}
