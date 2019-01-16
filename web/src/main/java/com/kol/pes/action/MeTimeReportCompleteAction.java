package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.service.MeTimeReportService;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;


public class MeTimeReportCompleteAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("timeReportService")
	private MeTimeReportService timeReportService;
	
	String schedDate; 
	String assetCode;
	String staffNo; 
	String reportType; 
	String wipId;
	String opCode;
	String seqNum;
	String activityName;
	String completeNum;
	String scrapNum;
	String addTime;
	String addTimeReason;
	String workClassCode;

	@Override
	@Action(value="/time_report_complete", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		DataCodeMessageItem res = timeReportService.timeReportComplete(schedDate, assetCode, staffNo, reportType, wipId, opCode, seqNum, activityName, completeNum, scrapNum, addTime, addTimeReason, workClassCode);
		
		LogUtil.log("MeTimeReportCompleteAction:wipId="+this.wipId);
		
		if(res!=null && res.code<2) {
			setCode(CODE_SUCCESS);
			if (res.code == 0) {
				setMessage("完成时间报数成功"); }
			else { 
				setMessage(CommonUtil.isStringNotNull(res.message)? res.message:"完成时间报数成功"); 
			}
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage(CommonUtil.isStringNotNull(res.message) ? res.message:"完成时间报数失败");
			return ERROR;
		}
	}
	
	public void setSchedDate(String schedDate) {
		this.schedDate = schedDate;
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
	
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	public void setCompleteNum(String completeNum) {
		this.completeNum = completeNum;
	}
	
	public void setScrapNum(String scrapNum) {
		this.scrapNum = scrapNum;
	}
	
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	public void setAddTimeReason(String addTimeReason) {
		this.addTimeReason = addTimeReason;
	}
	
	public void setWorkClassCode(String workClassCode) {
		this.workClassCode = workClassCode;
	}
	
	
	
}
