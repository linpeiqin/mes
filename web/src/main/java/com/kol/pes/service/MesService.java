package com.kol.pes.service;

import java.util.List;

import com.kol.pes.item.DataDateShift;
import com.kol.pes.item.DataMeAssetSeqInfo;
import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataMeProduceItem;
import com.kol.pes.item.DataMeSearchSeqItem;
import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.item.DataMeStartedSeqItem;
import com.kol.pes.item.DataQaNeedFillItem;
import com.kol.pes.item.DataQaReqItem;
import com.kol.pes.item.femaleworker.DataGroupInfoItem;
import com.kol.pes.item.femaleworker.DataOrgInfoItem;
import com.kol.pes.item.femaleworker.DataWipInfoItem;

public interface MesService extends DataEnableService {
	
	public List<DataMeProduceItem> getProduceListByProjectNum(String projectNum);
	
	public DataMeAssetSeqInfo getAssetSeqInfoByAssetId(String assetId);
	
	public List<DataMeSeqInfoData> getSeqInfoBySeqId(String seqId, String wipId);
	
	public DataMeProcedure getDescInfoWhenSeqSelected(String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String staffNo, String seqNum);
	
	public DataMeProcedure getDescInfoWhenStartingSeqClicked(String wipId, String opCode, String assetId, String schedDate, String seqNum);
	
	public DataMeProcedure startSeq(String staffNo, String inputQty, String startOpTime, String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String seqNum, String pafteropSeqNum, String workClassCode);
	
	public List<DataMeStartedSeqItem> getStartedSeqList(String assetId);
	
	public List<DataQaNeedFillItem> getQaList(String opCode, String orgId);
	
	public boolean deleteStartedSeq(String trxId);
	
	public DataMeProcedure endOp(String trxId, String wipId, String opCode, String planId, String staffNo, String inputQty, String scrapQty, String endTime, List<DataQaReqItem> qaDataList, String workClassCode, String seqNum, String schedDate);

	public List<DataMeSearchSeqItem> searchSeqList(String wipId);
	
	public String receiveOrderGetQtyById(String id, String staffNum);
	
	public boolean receiveOrderSureReceive(String id, String qty, String staffNo);
	
	public boolean receiveOrderSureReject(String id, String staffNo);
	
	public DataDateShift getDateShift();
	
	public List<DataMeSeqInfoData> getPafteropList(String wipId);

	List<DataGroupInfoItem> getGroupList4F(String assetNumber);

    List<DataOrgInfoItem> getOrgList();

	List<DataWipInfoItem> getWipList4F(String jobNo,String organizationId);
}
