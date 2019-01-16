/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class DataWipInfoItem extends DataItem {

	public String jobId;
	public String jobNo;
	public String jobName;
	public String wipEntityName;
	public String wipEntityId;
	public String inventoryItemId;
	public String itemNumber;
	public String itemDesc;
	public String operationCode;
	public String operationSeqNum;
	public String operationDesc;
	public String jobDesc;

	public String getJobId() {
		return jobId;
	}

	public String getJobNo() {
		return jobNo;
	}

	public String getJobName() {
		return jobName;
	}

	public String getWipEntityName() {
		return wipEntityName;
	}

	public String getWipEntityId() {
		return wipEntityId;
	}

	public String getInventoryItemId() {
		return inventoryItemId;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public String getOperationSeqNum() {
		return operationSeqNum;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public String getJobDesc() {
		return jobDesc;
	}
}
