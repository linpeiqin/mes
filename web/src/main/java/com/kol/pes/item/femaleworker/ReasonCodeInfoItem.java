/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class ReasonCodeInfoItem extends DataItem {

	public String reasonName;
	public String reasonCode;
	public String reasonDesc;

	public String getReasonName() {
		return reasonName;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}
}
