/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item.femaleworker;


import com.kol.pes.item.DataItem;

public class RoutingInfoItem extends DataItem {

	public String routingName;
	public String routingCode;
	public String routingType;

	public String getRoutingName() {
		return routingName;
	}

	public String getRoutingCode() {
		return routingCode;
	}

	public String getRoutingType() {
		return routingType;
	}
}
