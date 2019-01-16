/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class DeclareTimeInfoItem extends DataItem {

	public String organizationId;		//组织ID
	public String moveTransactionId;		//组织ID
	public String jobTransactionId;		//生产任务ID
	public String operationSeqNum;		//标准工序
	public String operationDesc;		//标准工序描述
	public String niOperationCode;		//女工工序编号
	public String niOperationDesc;		//女工工序描述
	public String quantity;				//人数
	public String startPullTime;				//开拉时间
	public String endPullTime;				//收拉时间
	public String workTime;				//总工时
	public String transactionUom;				//事务单位
	public String perQuantity;				//每小时产量
	public String subgoodsQuantity;				//半制品数
	public String goodsQuantity;				//成品数
	public String goodsWasteQuantity;				//生产坏货数
	public String returnWasteQuantity;				//返工坏货数
	public String inputQuantity;				//已导入好货数
	public String wasteInputQuantity;		//已导入坏货数
	public String reasonCode;					//坏货原因代码
	public String reasonRemark;				//坏货原因说明

	public String getOrganizationId() {
		return organizationId;
	}

	public String getJobTransactionId() {
		return jobTransactionId;
	}

	public String getMoveTransactionId() {
		return moveTransactionId;
	}

	public String getOperationSeqNum() {
		return operationSeqNum;
	}

	public String getNiOperationCode() {
		return niOperationCode;
	}

	public String getNiOperationDesc() {
		return niOperationDesc;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getStartPullTime() {
		return startPullTime;
	}

	public String getEndPullTime() {
		return endPullTime;
	}

	public String getWorkTime() {
		return workTime;
	}

	public String getTransactionUom() {
		return transactionUom;
	}

	public String getPerQuantity() {
		return perQuantity;
	}

	public String getSubgoodsQuantity() {
		return subgoodsQuantity;
	}

	public String getGoodsQuantity() {
		return goodsQuantity;
	}

	public String getGoodsWasteQuantity() {
		return goodsWasteQuantity;
	}

	public String getReturnWasteQuantity() {
		return returnWasteQuantity;
	}

	public String getInputQuantity() {
		return inputQuantity;
	}

	public String getWasteInputQuantity() {
		return wasteInputQuantity;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public String getReasonRemark() {
		return reasonRemark;
	}

	public String getOperationDesc() {
		return operationDesc;
	}
}
