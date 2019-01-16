package com.kol.pes.dao;

import java.sql.SQLException;
import java.util.List;

import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataMeMaterialsNumItem;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;

public interface MeMaterialsReportDao {
	
	public DataMeTimeReportProduceInfoAndSeqList seqListByProduceId(String produceNum) throws SQLException;
	
	
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo(String wipId, int type, String seqNum)  throws SQLException;

	public List<DataMeMaterialsNumItem> getMaterialsNum(String type, String wipId, String seqNum, String keyWords) throws SQLException;
	
	public DataMeTimeReportDescInfoAndActiveListInfo getMaterialsDescInfo(String wipId, String itemId) throws SQLException;
	
	public DataCodeMessageItem materialsReportComplete(String assetCode, String staffNo, String reportType, String wipId, String opCode, String seqNum, String itemId, String trxQty, String remark, String schedDate) throws SQLException;

}
