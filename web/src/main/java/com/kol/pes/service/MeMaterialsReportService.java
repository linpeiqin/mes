package com.kol.pes.service;

import java.util.List;

import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataMeMaterialsNumItem;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;

public interface MeMaterialsReportService {
	
	public DataMeTimeReportProduceInfoAndSeqList seqListByProduceId(String wipId);
	
	
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo(String wipId, int type, String seqNum);

	public List<DataMeMaterialsNumItem> getMaterialsNum(String type, String wipId, String seqNum, String keyWords);
	
	public DataMeTimeReportDescInfoAndActiveListInfo getMaterialsDescInfo(String wipId, String itemId);
	
	public DataCodeMessageItem materialsReportComplete(String assetCode, String staffNo, String reportType, String wipId, String opCode, String seqNum, String itemId, String trxQty, String remark, String schedDate);
}
