/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.DataProcessItem;
import com.kol.pes.pojo.MainProcessReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmMainProcessListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String resCode;
	private String scheduleDate;
	private String organizationId;
	private String workClassCode;
	private String workGroup;
	private String workMonitor;

	private List<DataProcessItem> processList;//获取的工序列表
	
	@Override
	@Action(value="/main_process_list_4f", results={
			@Result(name="success", location="main_process_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		MainProcessReq mainProcessReq = new MainProcessReq();
		initData(mainProcessReq);
		this.processList = seqService.getProcessList4F(mainProcessReq);//获取排期列表
		
		if(processList==null) {
			setCode(CODE_FAIL);
			setMessage("获取排产计划失败");
			return ERROR;
		}

		if (processList.size()==0){
			setCode(CODE_FAIL);
			setMessage("该机当天没有排产计划");
			return ERROR;
		}

		return SUCCESS;
	}
	private void initData(MainProcessReq mainProcessReq){
		mainProcessReq.organizationId = organizationId;
		mainProcessReq.scheduleDate = scheduleDate;
		mainProcessReq.workGroup = workGroup;
		mainProcessReq.resCode = resCode;
		mainProcessReq.workClassCode = workClassCode;
		mainProcessReq.workMonitor = workMonitor;
	}


	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setWorkClassCode(String workClassCode) {
		this.workClassCode = workClassCode;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}

	public void setWorkMonitor(String workMonitor) {
		this.workMonitor = workMonitor;
	}

	public List<DataProcessItem> getProcessList() {
		return this.processList;
	}

}
