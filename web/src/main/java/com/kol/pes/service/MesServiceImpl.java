package com.kol.pes.service;

import java.sql.SQLException;
import java.util.List;

import com.kol.pes.item.femaleworker.DataGroupInfoItem;
import com.kol.pes.item.femaleworker.DataOrgInfoItem;
import com.kol.pes.item.femaleworker.DataWipInfoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kol.pes.dao.MesDao;
import com.kol.pes.item.DataDateShift;
import com.kol.pes.item.DataMeAssetSeqInfo;
import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataMeProduceItem;
import com.kol.pes.item.DataMeSearchSeqItem;
import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.item.DataMeStartedSeqItem;
import com.kol.pes.item.DataQaNeedFillItem;
import com.kol.pes.item.DataQaReqItem;

@Service("mesService")
public class MesServiceImpl extends DataEnableServiceImpl implements MesService {
	
	@Autowired
	@Qualifier("mesDao")
	private MesDao mesDao;

	@Override
	public List<DataMeProduceItem> getProduceListByProjectNum(String projectNum) {
		try {
			return this.mesDao.getProduceListByProjectNum(projectNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataMeAssetSeqInfo getAssetSeqInfoByAssetId(String assetId) {
		try {
			return this.mesDao.getAssetSeqInfoByAssetId(assetId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DataMeSeqInfoData> getSeqInfoBySeqId(String seqId, String wipId) {
		try {
			return this.mesDao.getSeqInfoBySeqId(seqId, wipId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataMeProcedure getDescInfoWhenSeqSelected(String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String staffNo, String seqNum) {
		try {
			return this.mesDao.getDescInfoWhenSeqSelected(wipId, opCode, assetId, schedDate, pAfterOp, staffNo, seqNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataMeProcedure getDescInfoWhenStartingSeqClicked(String wipId, String opCode, String assetId, String schedDate, String seqNum) {
		try {
			return this.mesDao.getDescInfoWhenStartingSeqClicked(wipId, opCode, assetId, schedDate, seqNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DataMeProcedure startSeq(String staffNo, String inputQty, String startOpTime, String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String seqNum, String pafteropSeqNum, String workClassCode) {
		try {
			return this.mesDao.startSeq(staffNo, inputQty, startOpTime, wipId, opCode, assetId, schedDate, pAfterOp, seqNum, pafteropSeqNum, workClassCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DataMeStartedSeqItem> getStartedSeqList(String assetId) {
		try {
			return this.mesDao.getStartedSeqList(assetId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DataQaNeedFillItem> getQaList(String opCode, String orgId) {
		try {
			return this.mesDao.getQaList(opCode, orgId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteStartedSeq(String trxId) {
		try {
			return this.mesDao.deleteStartedSeq(trxId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public DataMeProcedure endOp(String trxId, String wipId, String opCode, String planId, String staffNo, String inputQty,String scrapQty, String endTime, List<DataQaReqItem> qaDataList, String workClassCode, String seqNum, String schedDate) {
		try {
			return this.mesDao.endOp(trxId, wipId, opCode, planId, staffNo, inputQty, scrapQty, endTime, qaDataList, workClassCode, seqNum, schedDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DataMeSearchSeqItem> searchSeqList(String wipId) {
		try {
			return this.mesDao.searchSeqList(wipId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String receiveOrderGetQtyById(String id, String staffNum) {
		try {
			return this.mesDao.receiveOrderGetQtyById(id, staffNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean receiveOrderSureReceive(String id, String qty, String staffNo) {
		try {
			return this.mesDao.receiveOrderSureReceive(id, qty, staffNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean receiveOrderSureReject(String id, String staffNo) {
		try {
			return this.mesDao.receiveOrderSureReject(id, staffNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public DataDateShift getDateShift() {
		try {
			return this.mesDao.getDateShift();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DataMeSeqInfoData> getPafteropList(String wipId) {
		try {
			return this.mesDao.getPafteropList(wipId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DataGroupInfoItem> getGroupList4F(String assetNumber) {
		try {
			return this.mesDao.getGroupList4F(assetNumber);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DataOrgInfoItem> getOrgList() {
		try {
			return this.mesDao.getOrgList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DataWipInfoItem> getWipList4F(String jobNo,String organizationId) {
		try {
			return this.mesDao.getWipList4F(jobNo,organizationId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
