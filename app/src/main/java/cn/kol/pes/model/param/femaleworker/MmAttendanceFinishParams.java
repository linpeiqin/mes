/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.item.femaleworker.MmAttendanceItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmAttendanceFinishParams extends KoHttpParams {

	public MmAttendanceFinishParams(MmAttendanceItem item){
		
		setParam("uri", "attendance_finish_4f");
		
		setParam("organizationId", item.organizationId);
		setParam("workGroup", item.workGroup);
		setParam("workMonitor", item.workMonitor);
		setParam("workClassCode", item.workClassCode);
		setParam("scheduleDate", item.scheduleDate);
		setParam("forecastWorkMan", item.forecastWorkMan);
		setParam("factWorkMan", item.factWorkMan);
		setParam("leaveQuantity", item.leaveQuantity);
		setParam("absentQuantity", item.absentQuantity);
		setParam("yearOfRestDay", item.yearOfRestDay);
		setParam("turnOfRestDay", item.turnOfRestDay);
		setParam("autoAemissionQuantity", item.autoAemissionQuantity);
		setParam("fireQuantity", item.fireQuantity);
		setParam("outQuantity", item.outQuantity);
		setParam("newInQuantity", item.newInQuantity);
		setParam("jobTime", item.jobTime);
		setParam("jobOvertime", item.jobOvertime);
		setParam("nonjobTime", item.nonjobTime);
		setParam("nonjobOvertime", item.nonjobOvertime);
		setParam("remark", item.remark);
	}
}
