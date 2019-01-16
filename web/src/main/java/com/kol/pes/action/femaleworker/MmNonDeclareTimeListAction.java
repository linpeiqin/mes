/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.DeclareTimeInfoItem;
import com.kol.pes.item.femaleworker.NonDeclareTimeInfoItem;
import com.kol.pes.pojo.DeclareTimeListReq;
import com.kol.pes.pojo.NonDeclareTimeListReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmNonDeclareTimeListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;

	private String organizationId;		//组织ID
	private String jobId;		//工程单ID
	private String wipEntityId;		//生产单ID
	private String inventoryItemId;		//成品ID
	private String scheduleDate;		//计划日期
	private String workGroup;		//组别
	private String workMonitor;		//组长
	private String workClassCode;		//班次(DAY/NIGHT)

	private List<NonDeclareTimeInfoItem> nonDeclareTimeList;//获取非生产状况列表


	
	@Override
	@Action(value="/get_non_declare_time_list_4f", results={
			@Result(name="success", location="get_non_declare_time_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		NonDeclareTimeListReq nonDeclareTimeListReq = new NonDeclareTimeListReq();
		initData(nonDeclareTimeListReq);
		this.nonDeclareTimeList = seqService.getNonDeclareTimeList(nonDeclareTimeListReq);

		if(nonDeclareTimeList == null) {

			setCode(CODE_FAIL);
			setMessage(getText("获取非生产状况列表失败"));

			return ERROR;
		}
		if(nonDeclareTimeList.size() == 0) {

			setCode(CODE_FAIL);
			setMessage(getText("请先填写非生产状况"));//请先填写生产状况

			return ERROR;
		}
		return SUCCESS;
	}
	private void initData(NonDeclareTimeListReq nonDeclareTimeListReq){
		nonDeclareTimeListReq.organizationId = organizationId;
		nonDeclareTimeListReq.jobId = jobId;
		nonDeclareTimeListReq.wipEntityId = wipEntityId;
		nonDeclareTimeListReq.inventoryItemId = inventoryItemId;
		nonDeclareTimeListReq.scheduleDate = scheduleDate;
		nonDeclareTimeListReq.workGroup = workGroup;
		nonDeclareTimeListReq.workMonitor = workMonitor;
		nonDeclareTimeListReq.workClassCode = workClassCode;
	}

	public List<NonDeclareTimeInfoItem> getNonDeclareTimeList() {
		return nonDeclareTimeList;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setWipEntityId(String wipEntityId) {
		this.wipEntityId = wipEntityId;
	}

	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}

	public void setWorkMonitor(String workMonitor) {
		this.workMonitor = workMonitor;
	}

	public void setWorkClassCode(String workClassCode) {
		this.workClassCode = workClassCode;
	}
}
