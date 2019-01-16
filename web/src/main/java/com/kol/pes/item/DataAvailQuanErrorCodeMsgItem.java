/*-----------------------------------------------------------

-- PURPOSE

--    登录员工信息的数据封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.item;

public class DataAvailQuanErrorCodeMsgItem extends DataItem {

	public int availQty = 0;
	public int errorCode = -1;
	public String errorMsg = "";
	
	public int getAvailQty() {
		return availQty;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
}
