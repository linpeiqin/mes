package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.service.MeTimeReportService;
import com.kol.pes.utils.LogUtil;
import com.kol.pes.item.DataMeProcedure;

public class MeHolidayOnOffAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("timeReportService")
	private MeTimeReportService timeReportService;

	private String staffNo;
	private DataMeProcedure resData;

	@Override
	@Action(value="/holiday_on_off", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		resData = timeReportService.holidayOnOff(staffNo);
		
		LogUtil.log("MeHolidayOnOffAction");
		
		setCode(CODE_SUCCESS);
		setMessage(resData.errorMsg);
		return SUCCESS;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
}
