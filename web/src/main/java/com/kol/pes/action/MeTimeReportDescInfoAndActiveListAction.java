package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.service.MeTimeReportService;
import com.kol.pes.utils.LogUtil;


public class MeTimeReportDescInfoAndActiveListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("timeReportService")
	private MeTimeReportService timeReportService;
	
	private String wipId = null;
	private String assetCode = null;
	private String reportType = null;
	private String schedDate = null;
	private String staffNo = null;
	private String workClassCode = null;
	private String opCode = null;
	
	private DataMeTimeReportDescInfoAndActiveListInfo descInfo;

	@Override
	@Action(value="/time_report_get_desc_info", results={
			@Result(name="success", location="time_report_get_desc_info.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		descInfo = timeReportService.timeReportGetDescInfoWhenStart(assetCode, schedDate, reportType, wipId, staffNo, workClassCode, opCode);
		
		LogUtil.log("MeTimeReportDescInfoAndActiveListAction:wipId="+this.wipId);
		
		if(descInfo!=null && descInfo.errCode>=0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取信息失败");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public void setSchedDate(String schedDate) {
		this.schedDate = schedDate;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public void setWorkClassCode(String workClassCode) {
		this.workClassCode = workClassCode;
	}
	
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo() {
		return this.descInfo;
	}
}
