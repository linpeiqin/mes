package cn.kol.pes.model.item.femaleworker;

import cn.kol.pes.model.item.Item;

public class MmGetDeclareMtlsBackItem extends Item {

	public String mtlTransactionId;		//物料报数ID
	public String scheduleTransactionId;		//班次ID
	public String inventoryItemId;		//物料ID
	public String concatenatedSegments;		//物料编码
	public String description;		//物料描述
	public String transactionQuantity;		//实际用量
	public String transactionUom;		//单位
	public String transactionItemType;		//标准/新增：BOM/SPEC
	public String transactionType;		//报数类型：DECLARE(使用)/WITHDRAWAL(退回)
	public String remark;		//备注
	public String showMsg(){
		String msg = "<td align='left'>"+concatenatedSegments+"</td>"+
				     "<td align='center'>"+transactionQuantity+"</td>"+
				     "<td align='right'>"+(transactionType.equals("DECLARE")?"使用":"退回")+"</td>";
		return msg;
	}
}
