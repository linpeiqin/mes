/*-----------------------------------------------------------

-- PURPOSE

--    获取推送消息的Service

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kol.pes.dao.MeMaterialsReportDao;
import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataMeMaterialsNumItem;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;

@Service("materialsReportService")
public class MeMaterialsReportServiceImpl implements MeMaterialsReportService {
	
	@Autowired
	@Qualifier("materialsReportDao")
	private MeMaterialsReportDao materialsReportDao;

	@Override
	public DataMeTimeReportProduceInfoAndSeqList seqListByProduceId(String produceNum) {
		try {
			return this.materialsReportDao.seqListByProduceId(produceNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo(String wipId, int type, String seqNum) {
		try {
			return this.materialsReportDao.getDescInfo(wipId, type, seqNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DataMeMaterialsNumItem> getMaterialsNum(String type, String wipId, String seqNum, String keyWords) {
		try {
			return this.materialsReportDao.getMaterialsNum(type, wipId, seqNum, keyWords);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DataMeTimeReportDescInfoAndActiveListInfo getMaterialsDescInfo(String wipId, String itemId) {
		try {
			return this.materialsReportDao.getMaterialsDescInfo(wipId, itemId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DataCodeMessageItem materialsReportComplete(String assetCode, String staffNo, String reportType, String wipId, String opCode, String seqNum, String itemId, String trxQty, String remark, String schedDate) {
		try {
			return this.materialsReportDao.materialsReportComplete(assetCode, staffNo, reportType, wipId, opCode, seqNum, itemId, trxQty, remark, schedDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
