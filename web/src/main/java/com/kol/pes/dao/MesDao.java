package com.kol.pes.dao;

import java.sql.SQLException;
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

public interface MesDao {
	
	public List<DataMeProduceItem> getProduceListByProjectNum(String projectNum) throws SQLException;
	
	public DataMeAssetSeqInfo getAssetSeqInfoByAssetId(String assetId) throws SQLException;
	
	public List<DataMeSeqInfoData> getSeqInfoBySeqId(String seqId, String wipId) throws SQLException;
	
	public DataMeProcedure getDescInfoWhenSeqSelected(String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String staffNo, String seqNum) throws SQLException;
	
	public DataMeProcedure getDescInfoWhenStartingSeqClicked(String wipId, String opCode, String assetId, String schedDate, String seqNum) throws SQLException;
	
	public DataMeProcedure startSeq(String staffNo, String inputQty, String startOpTime, String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String seqNum, String pafteropSeqNum, String workClassCode) throws SQLException;

	public List<DataMeStartedSeqItem> getStartedSeqList(String assetId) throws SQLException;
	
	public List<DataQaNeedFillItem> getQaList(String opCode, String orgId) throws SQLException;
	
	public boolean deleteStartedSeq(String trxId) throws SQLException;
	
	public DataMeProcedure endOp(String trxId, String wipId, String opCode, String planId, String staffNo, String inputQty, String scrapQty, String endTime, List<DataQaReqItem> qaDataList, String workClassCode, String seqNum, String schedDate) throws SQLException;

	public List<DataMeSearchSeqItem> searchSeqList(String wipId) throws SQLException;
	
	public String receiveOrderGetQtyById(String id, String staffNum) throws SQLException;
	
	public boolean receiveOrderSureReceive(String id, String qty, String staffNo) throws SQLException;
	
	public boolean receiveOrderSureReject(String id, String staffNo) throws SQLException;
	
	public DataDateShift getDateShift() throws SQLException;
	
	public List<DataMeSeqInfoData> getPafteropList(String wipId) throws SQLException;

    List<DataGroupInfoItem> getGroupList4F(String assetNumber) throws SQLException;

    List<DataOrgInfoItem> getOrgList() throws SQLException;

	List<DataWipInfoItem> getWipList4F(String jobNo,String organizationId) throws SQLException;
}
