/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class DataGroupInfoItem extends DataItem {

	public String groupName;
	public String groupCode;

	public String getGroupName() {
		return groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}
}
