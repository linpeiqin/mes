package com.kol.pes.item;


public class DataMeProcedure extends DataItem {
	
	public String defaultStartDate="";
	public String display="";
	public int availQty=0;
	public int errorCode = -1;
	public String errorMsg="";
	
	public String databaseTime;
	
	public String getDefaultStartDate() {
		return defaultStartDate;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public int getAvailQty() {
		return availQty;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String getDatabaseTime() {
		return databaseTime;
	}
}
