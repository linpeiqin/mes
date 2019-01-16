package cn.kol.pes.model.param.femaleworker;

import cn.kol.pes.model.item.femaleworker.MmGetDeclareMtlsItem;
import cn.kol.pes.model.item.femaleworker.MmGetDeclareTimesItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmGetDeclareMtlListParams extends KoHttpParams {
    public MmGetDeclareMtlListParams(MmGetDeclareMtlsItem declareMtlsItem) {
        setParam("uri", "get_declare_mtl_list_4f");
        setParam("organizationId", declareMtlsItem.organizationId);
        setParam("transactionType", declareMtlsItem.transactionType);
        setParam("jobTransactionId", declareMtlsItem.jobTransactionId);
        setParam("moveTransactionId", declareMtlsItem.moveTransactionId);

    }
}
