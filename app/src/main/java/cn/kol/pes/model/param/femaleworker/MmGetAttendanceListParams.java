package cn.kol.pes.model.param.femaleworker;

import cn.kol.pes.model.item.femaleworker.MmGetAttendancesItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmGetAttendanceListParams extends KoHttpParams {
    public MmGetAttendanceListParams(MmGetAttendancesItem declareTimesItem) {
        setParam("uri", "get_attendance_list_4f");
        setParam("organizationId", declareTimesItem.organizationId);
        setParam("scheduleDate", declareTimesItem.scheduleDate);
        setParam("workGroup", declareTimesItem.workGroup);
        setParam("workMonitor", declareTimesItem.workMonitor);
        setParam("workClassCode", declareTimesItem.workClassCode);
    }
}
