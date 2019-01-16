/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class DeclareMtlInfoItem extends DataItem {

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

	public String getMtlTransactionId() {
		return mtlTransactionId;
	}

	public String getScheduleTransactionId() {
		return scheduleTransactionId;
	}

	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public String getConcatenatedSegments() {
		return concatenatedSegments;
	}

	public String getDescription() {
		return description;
	}

	public String getTransactionQuantity() {
		return transactionQuantity;
	}

	public String getTransactionUom() {
		return transactionUom;
	}

	public String getTransactionItemType() {
		return transactionItemType;
	}

	public String getRemark() {
		return remark;
	}

	public String getTransactionType() {
		return transactionType;
	}
}
