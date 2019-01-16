/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.AttendanceInfoItem;
import com.kol.pes.item.femaleworker.DeclareMtlInfoItem;
import com.kol.pes.pojo.AttendanceListReq;
import com.kol.pes.pojo.AttendanceReq;
import com.kol.pes.pojo.DeclareMtlListReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmAttendanceListAction extends ParentAction {


	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;

	private String organizationId;		//组织ID
	private String workGroup;		//组别
	private String workMonitor;		//组长
	private String workClassCode;		//班次(DAY/NIGHT)
	private String scheduleDate;		//计划日期

	private List<AttendanceInfoItem> attendanceList;//获取的考勤列表

	@Override
	@Action(value = "/get_attendance_list_4f", results = {
			@Result(name = "success", location = "get_attendance_list_4f.ftl", type = "freemarker", params = {"contentType", "text/xml"}),
			@Result(name = "error", location = "code_message.ftl", type = "freemarker", params = {"contentType", "text/xml"})
	})

	public String execute() throws Exception {
		AttendanceListReq attendanceListReq = new AttendanceListReq();
		initData(attendanceListReq);
		this.attendanceList = seqService.getAttendanceList(attendanceListReq);

		if (attendanceList == null) {

			setCode(CODE_FAIL);
			setMessage(getText("获取考勤列表失败"));//获取考勤列表失败

			return ERROR;
		}
		if (attendanceList.size() == 0) {

			setCode(CODE_FAIL);
			setMessage(getText("请填写考勤"));//请填写考勤

			return ERROR;
		}
		return SUCCESS;
	}

	private void initData(AttendanceListReq attendanceListReq) {
		attendanceListReq.organizationId = organizationId;
		attendanceListReq.workGroup = workGroup;
		attendanceListReq.workMonitor = workMonitor;
		attendanceListReq.workClassCode = workClassCode;
		attendanceListReq.scheduleDate = scheduleDate;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public List<AttendanceInfoItem> getAttendanceList() {
		return attendanceList;
	}
}
