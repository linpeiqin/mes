/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.DataProcessItem;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmGetMachineReportTimeAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String resCode;
	private String scheduleDate;
	private String shift;
	
	private String machineReportTime;

	@Override
	@Action(value="/main_machine_report_time_4f", results={
			@Result(name="success", location="main_machine_report_time.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message_machine_report_time.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {

		this.machineReportTime = seqService.getMachineReportTime4f(resCode, scheduleDate, shift);
		if(machineReportTime==null) {
			setCode(CODE_FAIL);
			setMessage("获取机器班次失败");
			return ERROR;
		}

		return SUCCESS;
	}
	
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	
	public void setShift(String shift) {
		this.shift = shift;
	}
	
	public String getMachineReportTime() {
		return this.machineReportTime;
	}
	
}
