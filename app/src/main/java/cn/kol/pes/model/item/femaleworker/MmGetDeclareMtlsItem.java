package cn.kol.pes.model.item.femaleworker;

import java.io.Serializable;

import cn.kol.pes.model.item.Item;

public class MmGetDeclareMtlsItem extends Item implements Serializable {

	public String organizationId;		//组织ID
	public String transactionType;      //报数类型
	public String jobTransactionId;		//生产任务ID
	public String moveTransactionId;		//生产状况ID


}
