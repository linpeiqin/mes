/*-----------------------------------------------------------

-- PURPOSE

--    获取推送消息的Service

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kol.pes.dao.MeTimeReportDao;
import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;

@Service("timeReportService")
public class MeTimeReportServiceImpl implements MeTimeReportService {
	
	@Autowired
	@Qualifier("timeReportDao")
	private MeTimeReportDao timeReportDao;


	@Override
	public DataMeTimeReportDescInfoAndActiveListInfo timeReportGetDescInfoWhenStart(String assetCode, String schedDate,
			String reportType, String wipId, String staffNo, String workClassCode, String opCode) {
		try {
			return this.timeReportDao.timeReportGetDescInfoWhenStart( assetCode, schedDate, reportType, wipId, staffNo, workClassCode, opCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataMeTimeReportProduceInfoAndSeqList timeReportGetProduceInfoAndSeqListByProduceId(String wipId, String assetId) {
		try {
			return this.timeReportDao.timeReportGetProduceInfoAndSeqListByProduceId(wipId, assetId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DataCodeMessageItem timeReportComplete(String p_sched_date, 
													String assetCode, String staffNo,
													String reportType, String wipId, 
													String opCode, String seqNum, 
													String activityName, 
													String completeNum,
													String scrapNum, 
													String addTime, String addTimeReason, String workClassCode) {
		try {
			return this.timeReportDao.timeReportComplete(p_sched_date,  assetCode,  staffNo,
														 reportType,  wipId,  opCode,  seqNum,  activityName,  completeNum,
														 scrapNum, addTime, addTimeReason, workClassCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DataMeProcedure holidayOnOff(String staffNo) {
		try {
			return this.timeReportDao.holidayOnOff(staffNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
