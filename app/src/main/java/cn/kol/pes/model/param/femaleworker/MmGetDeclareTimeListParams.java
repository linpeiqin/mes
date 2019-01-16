package cn.kol.pes.model.param.femaleworker;

import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmGetDeclareTimeListParams extends KoHttpParams {
    public MmGetDeclareTimeListParams(MmGetDeclareTimesItem declareTimesItem) {
        setParam("uri", "get_declare_time_list_4f");
        setParam("organizationId", declareTimesItem.organizationId);
        setParam("jobId", declareTimesItem.jobId);
        setParam("wipEntityId", declareTimesItem.wipEntityId);
        setParam("inventoryItemId", declareTimesItem.inventoryItemId);
        setParam("scheduleDate", declareTimesItem.scheduleDate);
        setParam("workGroup", declareTimesItem.workGroup);
        setParam("workMonitor", declareTimesItem.workMonitor);
        setParam("workClassCode", declareTimesItem.workClassCode);
    }
}
