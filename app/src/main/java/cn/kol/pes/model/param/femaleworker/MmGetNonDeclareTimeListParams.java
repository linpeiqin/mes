package cn.kol.pes.model.param.femaleworker;

import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesItem;
import cn.kol.pes.model.item.femaleworker.MmGetNonDeclareTimesItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmGetNonDeclareTimeListParams extends KoHttpParams {
    public MmGetNonDeclareTimeListParams(MmGetNonDeclareTimesItem nonDeclareTimesItem) {
        setParam("uri", "get_non_declare_time_list_4f");
        setParam("organizationId", nonDeclareTimesItem.organizationId);
        setParam("jobId", nonDeclareTimesItem.jobId);
        setParam("wipEntityId", nonDeclareTimesItem.wipEntityId);
        setParam("inventoryItemId", nonDeclareTimesItem.inventoryItemId);
        setParam("scheduleDate", nonDeclareTimesItem.scheduleDate);
        setParam("workGroup", nonDeclareTimesItem.workGroup);
        setParam("workMonitor", nonDeclareTimesItem.workMonitor);
        setParam("workClassCode", nonDeclareTimesItem.workClassCode);
    }
}
