/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataProcessItem;
import com.kol.pes.service.SeqService;


public class MainProcessListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String resCode;
	private String scheduleDate;
	private String shift;
	
	private String machineReportTime;
	private List<DataProcessItem> processList;//获取的工序列表
	
	@Override
	@Action(value="/main_process_list", results={
			@Result(name="success", location="main_process_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message_machine_report_time.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {

		this.machineReportTime = seqService.getMachineReportTime(resCode, scheduleDate, shift);
		this.processList = seqService.getProcessList(resCode, scheduleDate, shift);//获取工序列表
		
		if(processList==null || processList.size()==0) {
			setCode(CODE_FAIL);
			setMessage("获取排产计划失败");
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
	
	public List<DataProcessItem> getProcessList() {
		return this.processList;
	}
}
