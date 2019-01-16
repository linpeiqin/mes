/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.pojo.NonDeclareTimeReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class MmNonDeclareTimeAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String nonDeclareTimeId;


	private String organizationId;		//组织ID
	private String scheduleDate;		//计划日期
	private String workGroup;			//组别
	private String dayOrNight;			//班次
	private String groupMaster;		//组长
	private String jobId;		//工程单ID
	private String wipEntityId;		//生产单ID
	private String inventoryItemId ;		//物料（成品）ID
	private String quantity;		//人数
	private String workTime;		//耗用工时
	private String reasonCode;		//坏货原因代码
	private String reasonRemark;		//坏货原因说明
	private String goodsQuantity;		//非生产总数
	private String goodsWasteQuantity;		//非生产坏货数
	
	@Override
	@Action(value="/non_declare_time_finish_4f", results={
			@Result(name="success", location="non_declare_time_finish_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		NonDeclareTimeReq nonDeclareTimeReq = new NonDeclareTimeReq();
		initData(nonDeclareTimeReq);
		String codes[] = seqService.nonDeclareTimeFinish(nonDeclareTimeReq);
		Integer codeI = Integer.valueOf(codes[0]);
		String  codeMsg = codes[1];
		if(codeI < 0) {
			setCode(CODE_FAIL);
			setMessage(getText(codeMsg));
			return ERROR;
		}
		this.nonDeclareTimeId = String.valueOf(codeI);
		return SUCCESS;
	}
	private void initData(NonDeclareTimeReq nonDeclareTimeReq){
		nonDeclareTimeReq.organizationId = organizationId;
		nonDeclareTimeReq.scheduleDate = scheduleDate;
		nonDeclareTimeReq.workGroup = workGroup;
		nonDeclareTimeReq.dayOrNight = dayOrNight;
		nonDeclareTimeReq.groupMaster = groupMaster;
		nonDeclareTimeReq.jobId = jobId;
		nonDeclareTimeReq.wipEntityId = wipEntityId;
		nonDeclareTimeReq.inventoryItemId = inventoryItemId;
		nonDeclareTimeReq.quantity = quantity;
		nonDeclareTimeReq.workTime = workTime;
		nonDeclareTimeReq.goodsQuantity = goodsQuantity;
		nonDeclareTimeReq.goodsWasteQuantity = goodsWasteQuantity;
		nonDeclareTimeReq.reasonCode = reasonCode;
		nonDeclareTimeReq.reasonRemark = reasonRemark;
	}

	public String getNonDeclareTimeId() {
		return nonDeclareTimeId;
	}

	public void setOrganizationId(String organizationId) {

		this.organizationId = organizationId;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}

	public void setDayOrNight(String dayOrNight) {
		this.dayOrNight = dayOrNight;
	}

	public void setGroupMaster(String groupMaster) {
		this.groupMaster = groupMaster;
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

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public void setReasonRemark(String reasonRemark) {
		this.reasonRemark = reasonRemark;
	}

	public void setGoodsQuantity(String goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}

	public void setGoodsWasteQuantity(String goodsWasteQuantity) {
		this.goodsWasteQuantity = goodsWasteQuantity;
	}
}
