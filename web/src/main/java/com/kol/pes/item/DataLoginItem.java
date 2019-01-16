/*-----------------------------------------------------------

-- PURPOSE

--    登录员工信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item;

public class DataLoginItem extends DataItem {

	public String staffNo;
	public String staffName;
	public String pwd;
	public String type;

	public String getStaffNo() {
		return staffNo;
	}
	
	public String getStaffName() {
		return staffName;
	}

	public String getType() {
		return type;
	}
}
