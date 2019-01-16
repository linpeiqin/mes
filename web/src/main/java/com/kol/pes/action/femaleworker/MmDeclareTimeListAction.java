/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.DeclareTimeInfoItem;
import com.kol.pes.pojo.DeclareTimeListReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmDeclareTimeListAction extends ParentAction {

	
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

	private List<DeclareTimeInfoItem> declareTimeList;//获取的工序列表


	
	@Override
	@Action(value="/get_declare_time_list_4f", results={
			@Result(name="success", location="get_declare_time_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		DeclareTimeListReq declareTimeListReq = new DeclareTimeListReq();
		initData(declareTimeListReq);
		this.declareTimeList = seqService.getDeclareTimeList(declareTimeListReq);

		if(declareTimeList == null) {

			setCode(CODE_FAIL);
			setMessage(getText("获取生产状况列表失败"));//获取生产状况列表失败

			return ERROR;
		}
		if(declareTimeList.size() == 0) {

			setCode(CODE_FAIL);
			setMessage(getText("请先填写生产状况"));//请先填写生产状况

			return ERROR;
		}
		return SUCCESS;
	}
	private void initData(DeclareTimeListReq declareTimeListReq){
		declareTimeListReq.organizationId = organizationId;
		declareTimeListReq.jobId = jobId;
		declareTimeListReq.wipEntityId = wipEntityId;
		declareTimeListReq.inventoryItemId = inventoryItemId;
		declareTimeListReq.scheduleDate = scheduleDate;
		declareTimeListReq.workGroup = workGroup;
		declareTimeListReq.workMonitor = workMonitor;
		declareTimeListReq.workClassCode = workClassCode;
	}

	public List<DeclareTimeInfoItem> getDeclareTimeList() {
		return declareTimeList;
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
