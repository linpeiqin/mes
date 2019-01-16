/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class MtlPlanInfoItem extends DataItem {

	public String wipEntityId;			//生产单id
	public String operationSeqNum;		//工序号
	public String organizationId;		//组织id
	public String inventoryItemId;		//物料id
	public String concatenatedSegments;//物料编码
	public String itemDescription;		//物料描述
	public String requiredQuantity;		//申请用量
	public String itemPrimaryUomCode;	//申请单位
	public String issued;	//已报数量
	public String available;	//可报数量

	public String getWipEntityId() {
		return wipEntityId;
	}

	public String getOperationSeqNum() {
		return operationSeqNum;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public String getConcatenatedSegments() {
		return concatenatedSegments;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public String getRequiredQuantity() {
		return requiredQuantity;
	}

	public String getItemPrimaryUomCode() {
		return itemPrimaryUomCode;
	}

	public String getIssued() {
		return issued;
	}

	public String getAvailable() {
		return available;
	}
}
