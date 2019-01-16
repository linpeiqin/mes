
package com.kol.pes.item;

import java.util.List;

public class DataMeTimeReportProduceInfoAndSeqList extends DataItem {
	
	public int trxQty;
	public int scrapQty;

	public List<DataMeSeqInfoData> seqList;
	
	public List<DataMeSeqInfoData> getSeqList() {
		return seqList;
	}
	
	public int getTrxQty() {
		return trxQty;
	}
	
	public int getScrapQty() {
		return scrapQty;
	}
}
