/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class NonDeclareTimeInfoItem extends DataItem {

	public String organizationId;		//组织ID
	public String jobTransactionId;		//生产任务ID
	public String moveTransactionId;		//非生产状况ID
	public String jobId;		//工程单ID
	public String jobNo;		//工程单编码
	public String wipEntityId;		//生产单ID
	public String wipEntityName;		//生成单编码
	public String inventoryItemId ;		//物料（成品）ID
	public String scheduleTransactionId;		//班次ID
	public String quantity;		//人数
	public String workTime;		//耗用工时
	public String goodsQuantity;		//非生产总数
	public String goodsWasteQuantity;		//非生产坏货数
	public String reasonCode;		//坏货原因代码
	public String reasonRemark;		//坏货原因说明

	public String getOrganizationId() {
		return organizationId;
	}

	public String getJobTransactionId() {
		return jobTransactionId;
	}

	public String getMoveTransactionId() {
		return moveTransactionId;
	}

	public String getJobId() {
		return jobId;
	}

	public String getJobNo() {
		return jobNo;
	}

	public String getWipEntityId() {
		return wipEntityId;
	}

	public String getWipEntityName() {
		return wipEntityName;
	}

	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public String getScheduleTransactionId() {
		return scheduleTransactionId;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getWorkTime() {
		return workTime;
	}

	public String getGoodsQuantity() {
		return goodsQuantity;
	}

	public String getGoodsWasteQuantity() {
		return goodsWasteQuantity;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public String getReasonRemark() {
		return reasonRemark;
	}
}
