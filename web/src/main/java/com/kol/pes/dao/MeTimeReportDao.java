package com.kol.pes.dao;

import java.sql.SQLException;

import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;

public interface MeTimeReportDao {
	
	public DataMeTimeReportDescInfoAndActiveListInfo timeReportGetDescInfoWhenStart(String assetCode, String schedDate, String reportType, String wipId, String staffNo, String workClassCode, String opCode)  throws SQLException;

	public DataMeTimeReportProduceInfoAndSeqList timeReportGetProduceInfoAndSeqListByProduceId(String produceNum, String assetId) throws SQLException;
	
	public DataCodeMessageItem timeReportComplete(String p_sched_date, String assetCode, String staffNo, 
    		String reportType, String wipId, String opCode, 
    		String seqNum, String activityName, 
    		String completeNum, String scrapNum, 
    		String addTime, String addTimeReason,String workClassCode) throws SQLException;

	public DataMeProcedure holidayOnOff(String staffNo) throws SQLException;
}
