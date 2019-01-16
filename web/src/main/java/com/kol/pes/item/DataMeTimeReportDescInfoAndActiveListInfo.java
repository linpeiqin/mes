
package com.kol.pes.item;

import java.util.List;

public class DataMeTimeReportDescInfoAndActiveListInfo extends DataItem {
	
	public int completeQty;
	public int scrapQty;
	public String display;
	public int errCode;
	public String errMsg;
	
	public List<DataMeActiveItem> activeList;
	
	public List<String> reasonList;
	
	
	public int getCompleteQty() {
		return completeQty;
	}
	
	public int getScrapQty() {
		return scrapQty;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public int getErrCode() {
		return errCode;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	
	public List<DataMeActiveItem> getActiveList() {
		return activeList;
	}
	
	public List<String> getReasonList() {
		return reasonList;
	}
}
