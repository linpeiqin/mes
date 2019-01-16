/*-----------------------------------------------------------

-- PURPOSE

--    设备基本信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item;


public class DataAssetInfoItem extends DataItem {

	public int resourceId;
	public String assetNumber;
	public String description;
	public String createdDate;
	
	
	public String opDscr;
	public String location;
	public String attribute7;
	
	
	
	public int getResourceId() {
		return resourceId;
	}
	
	public String getAssetNumber() {
		return assetNumber;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	
	
	
	public String getOpDscr() {
		return opDscr;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getAttribute7() {
		return attribute7;
	}

}
