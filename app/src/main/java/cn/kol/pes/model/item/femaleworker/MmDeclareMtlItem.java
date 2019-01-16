package cn.kol.pes.model.item.femaleworker;

import cn.kol.pes.model.item.Item;

public class MmDeclareMtlItem extends Item {

	public String organizationId;		//组织ID
	public String jobTransactionId;		//生产任务ID
	public String moveTransactionId;		//生产状况ID
	public String inventoryItemId;		//物料ID
	public String transactionUom;		//单位
	public String transactionQuantity;		//实际用量
	public String remark;		//备注
	public String transactionItemType;		//标准/新增：BOM/SPEC
	public String transactionType;		//报数类型：DECLARE(使用)/WITHDRAWAL(退回)


}
