/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.pojo.AttendanceReq;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class MmAttendanceFinishAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String attendanceId;

	private String organizationId;		//组织ID
	private String workGroup;		//组别
	private String workMonitor;		//组长
	private String workClassCode;		//班次(DAY/NIGHT)
	private String scheduleDate;		//计划日期
	private String forecastWorkMan;		//预计出勤人数
	private String factWorkMan;		//实际出勤人数
	private String leaveQuantity;		//请假工时
	private String absentQuantity ;		//旷工工时
	private String yearOfRestDay;		//年休
	private String turnOfRestDay;		//轮休
	private String autoAemissionQuantity;		//自离
	private String fireQuantity;		//辞工解雇
	private String outQuantity;		//出差
	private String newInQuantity;		//新进员工数
	private String jobTime;		//生产正常工时
	private String jobOvertime;		//生产加班工时
	private String nonjobTime;		//非生产正常工时
	private String nonjobOvertime;		//非生产加班工时
	private String remark;		//备注

	
	@Override
	@Action(value="/attendance_finish_4f", results={
			@Result(name="success", location="attendance_finish_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		AttendanceReq attendanceReq = new AttendanceReq();
		initData(attendanceReq);
		String codes[] = seqService.attendanceFinish(attendanceReq);
		Integer codeI = Integer.valueOf(codes[0]);
		String  codeMsg = codes[1];
		if(codeI < 0) {
			setCode(CODE_FAIL);
			setMessage(getText(codeMsg));
			return ERROR;
		}
		this.attendanceId = String.valueOf(codeI);
		return SUCCESS;
	}
	private void initData(AttendanceReq attendanceReq){
		attendanceReq.organizationId = organizationId;
		attendanceReq.workGroup = workGroup;
		attendanceReq.workMonitor = workMonitor;
		attendanceReq.workClassCode = workClassCode;
		attendanceReq.scheduleDate = scheduleDate;
		attendanceReq.forecastWorkMan = forecastWorkMan;
		attendanceReq.factWorkMan = factWorkMan;
		attendanceReq.leaveQuantity = leaveQuantity;
		attendanceReq.absentQuantity = absentQuantity;
		attendanceReq.yearOfRestDay = yearOfRestDay;
		attendanceReq.turnOfRestDay = turnOfRestDay;
		attendanceReq.autoAemissionQuantity = autoAemissionQuantity;
		attendanceReq.fireQuantity = fireQuantity;
		attendanceReq.outQuantity = outQuantity;
		attendanceReq.newInQuantity = newInQuantity;
		attendanceReq.jobTime = jobTime;
		attendanceReq.jobOvertime = jobOvertime;
		attendanceReq.nonjobTime = nonjobTime;
		attendanceReq.nonjobOvertime = nonjobOvertime;
		attendanceReq.remark = remark;
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

	public void setForecastWorkMan(String forecastWorkMan) {
		this.forecastWorkMan = forecastWorkMan;
	}

	public void setFactWorkMan(String factWorkMan) {
		this.factWorkMan = factWorkMan;
	}

	public void setLeaveQuantity(String leaveQuantity) {
		this.leaveQuantity = leaveQuantity;
	}

	public void setAbsentQuantity(String absentQuantity) {
		this.absentQuantity = absentQuantity;
	}

	public void setYearOfRestDay(String yearOfRestDay) {
		this.yearOfRestDay = yearOfRestDay;
	}

	public void setTurnOfRestDay(String turnOfRestDay) {
		this.turnOfRestDay = turnOfRestDay;
	}

	public void setAutoAemissionQuantity(String autoAemissionQuantity) {
		this.autoAemissionQuantity = autoAemissionQuantity;
	}

	public void setFireQuantity(String fireQuantity) {
		this.fireQuantity = fireQuantity;
	}

	public void setOutQuantity(String outQuantity) {
		this.outQuantity = outQuantity;
	}

	public void setNewInQuantity(String newInQuantity) {
		this.newInQuantity = newInQuantity;
	}

	public void setJobTime(String jobTime) {
		this.jobTime = jobTime;
	}

	public void setJobOvertime(String jobOvertime) {
		this.jobOvertime = jobOvertime;
	}

	public void setNonjobTime(String nonjobTime) {
		this.nonjobTime = nonjobTime;
	}

	public void setNonjobOvertime(String nonjobOvertime) {
		this.nonjobOvertime = nonjobOvertime;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAttendanceId() {
		return attendanceId;
	}
}
