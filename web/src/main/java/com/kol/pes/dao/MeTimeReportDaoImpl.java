package com.kol.pes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataIntPair;
import com.kol.pes.item.DataMeActiveItem;
import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;


@Repository("timeReportDao")
public class MeTimeReportDaoImpl implements MeTimeReportDao {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	
	public DataMeTimeReportDescInfoAndActiveListInfo timeReportGetDescInfoWhenStart(String assetCode, String schedDate, String reportType, String wipId, String staffNo, String workClassCode, String opCode) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.openTimeReport(?,?,?,?,?,?,?,?,?,?,?)}");
		
		cstmt.setString(1, assetCode);
		cstmt.setString(2, schedDate);
		cstmt.setInt(3, Integer.valueOf(reportType));
		cstmt.setInt(4, wipId!=null?Integer.valueOf(wipId):0);
		cstmt.setString(5, workClassCode);
		cstmt.setString(6, opCode);
		
		cstmt.registerOutParameter(7, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(8, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
		
		cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(11, java.sql.Types.VARCHAR);
		
		cstmt.execute();
		
		int completeQty = cstmt.getInt(7);
		int scrapQty = cstmt.getInt(8);
		String display = cstmt.getString(9);
		int errorCode = cstmt.getInt(10);
		String errorMsg = cstmt.getString(11);
		
		cstmt.close();
		con.close();
		
		DataMeTimeReportDescInfoAndActiveListInfo dataItem = new DataMeTimeReportDescInfoAndActiveListInfo();
		
		dataItem.completeQty = completeQty;
		dataItem.scrapQty = scrapQty;
		dataItem.display = CommonUtil.noNullString(display);
		
		dataItem.errCode = errorCode;
		dataItem.errMsg = CommonUtil.noNullString(errorMsg);
		
		if(errorCode>=0 && errorCode<2) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String sqlActive = "select activity,Description from cst_activities_v a where created_by > 1 and (disable_date is null or disable_date > sysdate) And  activity not in ('D03', 'D04', 'D05', 'D09','D10', 'C01', 'E01')";
			if("2".equals(reportType)) {
				sqlActive = "select activity,Description from cst_activities_v a where created_by > 1 and (disable_date is null or disable_date > sysdate) And activity in ('D01','D02','D03','D04','D05','D06','D07','D08','D09','D10','C01','E01')";
			}
			dataItem.activeList = runner.query(sqlActive, new ResultSetHandler<List<DataMeActiveItem>>() {

				public List<DataMeActiveItem> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					
					List<DataMeActiveItem> opCodeList = new ArrayList<DataMeActiveItem>();
					
					while(rs.next()) { 
						DataMeActiveItem item = new DataMeActiveItem();
						item.activity = rs.getString("activity");
						item.description = rs.getString("description");
						opCodeList.add(item);
					}
					
					return opCodeList;
				}
			});
			LogUtil.log("timeReportGetDescInfoWhenStart():"+sqlActive);
		}
		
		dataItem.reasonList = getReasonList(reportType, staffNo);
		
		LogUtil.log("timeReportGetDescInfoWhenStart():errorCode="+errorCode+", errorMsg="+errorMsg+", dataItem.completeQty="+dataItem.completeQty);
		return dataItem;
	}
	
	private List<String> getReasonList(String reportType, String staffNo) throws SQLException {
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.genericBuildList(?,?,?,?)}");
		
		cstmt.setString(1, reportType);
		cstmt.setString(2, staffNo);
		cstmt.setString(3, null);

		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
		
		cstmt.execute();
	
		String display = cstmt.getString(4);

		cstmt.close();
		con.close();
		LogUtil.log("getReasonList:display="+display);
		if(CommonUtil.isStringNotNull(display)) {
			return Arrays.asList(display.split("\\|"));
		}
		
		return new ArrayList<String>();
		
//		genericBuildList (
		//        p_list_code in  varchar2   --列表类型 --1 (生产时间新增原因) 2 (非工程时间新增原因)
//				, p_staff_no          in varchar2     --员工号 
//				, p_gen_no            in varchar2     --通用号 备用：目前放null
//				, p_display           out nocopy varchar2)   --返回列表内容，以|符号分隔

	}
	
	public DataMeTimeReportProduceInfoAndSeqList timeReportGetProduceInfoAndSeqListByProduceId(String wipId, String assetId) throws SQLException {
		
		if(CommonUtil.isStringNotNull(wipId) && CommonUtil.isStringNotNull(assetId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			String selectSeqSql = "select wo.operation_seq_num, bso.operation_code, bso.operation_description from wip_operations wo, bom_standard_operations bso where wo.standard_operation_id = bso.standard_operation_id and wo.organization_id = bso.organization_id and wo.wip_entity_id = "+wipId;
			LogUtil.log("timeReportGetProduceInfoAndSeqListByProduceId():selectSeqSql="+selectSeqSql);
			
			DataMeTimeReportProduceInfoAndSeqList data = new DataMeTimeReportProduceInfoAndSeqList();

			data.seqList = runner.query(selectSeqSql, new ResultSetHandler<List<DataMeSeqInfoData>>() {
				
				public List<DataMeSeqInfoData> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					
					List<DataMeSeqInfoData> tempList = new ArrayList<DataMeSeqInfoData>();

					while(rs.next()) { 
						DataMeSeqInfoData item = new DataMeSeqInfoData();
						item.operationCode = CommonUtil.noNullString(rs.getString("operation_code"));
						item.seqNum = CommonUtil.noNullString(rs.getString("operation_seq_num"));
						item.operationDescription = CommonUtil.noNullString(rs.getString("operation_description"));
						tempList.add(item);
					}
					return tempList;
				}
			});
			
			String sqlGetTrxScrap = "select sum(trx_quantity) trx_qty, sum(scrap_quantity) scrap_qty from kol_pes_move_txn_result where wip_entity_id = "+wipId+" and asset_id = "+assetId;
			LogUtil.log("timeReportGetProduceInfoAndSeqListByProduceId():sqlGetTrxScrap="+sqlGetTrxScrap);
			
			DataIntPair v = runner.query(sqlGetTrxScrap, new ResultSetHandler<DataIntPair>() {
				
				public DataIntPair handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return new DataIntPair();
			        }
					
					DataIntPair tempV = new DataIntPair();
					
					while(rs.next()) { 
						 tempV.v1= rs.getInt("trx_qty");
						 tempV.v2 = rs.getInt("scrap_qty");
					}
					
					return tempV;
				}
			});
			
			data.trxQty = v.v1;
			data.scrapQty = v.v2;
			
			return data;
		}
		return null;
	}
	
	public DataCodeMessageItem timeReportComplete(String p_sched_date, String assetCode, String staffNo, 
						    		String reportType, String wipId, String opCode, 
						    		String seqNum, String activityName, 
						    		String completeNum, String scrapNum, 
						    		String addTime, String addTimeReason, String workClassCode) throws SQLException {
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.completeReportTime(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		
		cstmt.setString(1, p_sched_date);
		cstmt.setString(2, assetCode);
		cstmt.setFloat(3, Float.valueOf(addTime));
		cstmt.setString(4, staffNo);
		cstmt.setInt(5, Integer.valueOf(reportType));
		cstmt.setInt(6, Integer.valueOf(wipId));
		cstmt.setString(7, opCode);
		cstmt.setInt(8, Integer.valueOf(seqNum));
		cstmt.setString(9, activityName);
		cstmt.setInt(10, Integer.valueOf(completeNum));
		cstmt.setInt(11, Integer.valueOf(scrapNum));
		cstmt.setString(12, addTimeReason);
		cstmt.setString(13, workClassCode);
		
		cstmt.registerOutParameter(14, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(15, java.sql.Types.VARCHAR);
		
		cstmt.execute();
		
		DataCodeMessageItem data = new DataCodeMessageItem();
		data.code = cstmt.getInt(14);
		data.message = cstmt.getString(15);
		
		cstmt.close();
		con.close();
		
		LogUtil.log("timeReportComplete():data.code="+data.code+", data.message="+data.message);
		
		return data;
		
//			 p_sched_date  in  varchar2 
//	       , p_asset_code        in  varchar2       --设备号
//	       , p_work_time         in  number         --新增时间
//	       , p_staff_no          in  varchar2       --用户登入名(e.g. 000037)
//	       , p_report_type       in  number         --1：生产时间 2：非工程时间
//	       , p_wip_entity_id     in  number         --生产单ID (if p_report_type = 2(非工程时间),应是null)
//	       , p_operation_code    in  varchar2       --工序 (e.g. E114, if p_report_type = 2(非工程时间),应是null)
//	       , p_operation_seq_num in  number         --工序次序号 (e.g. 30 if p_report_type = 2(非工程时间),应是null)
//	       , p_activity_name     in  varchar2       --活动代号
//	       , p_trx_qty           in  number         --好货数
//	       , p_scrap_qty         in  number         --损耗数
//	       , p_remark            in  varchar2       --新增原因
//	       , errcode             out nocopy number
//	       , errmsg              out nocopy varchar2);
	}
	
	public DataMeProcedure holidayOnOff(String staffNo) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.kol_pes_util_pkg.setHoliday_onoff(?,?,?)}");
		cstmt.setString(1, staffNo);
		cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
		cstmt.execute();
		
		int errorCode = cstmt.getInt(2);
		String errorMsg = cstmt.getString(3);
		LogUtil.log("errorCode="+errorCode+", errorMsg="+errorMsg);
		
		cstmt.close();
		con.close();

		DataMeProcedure dataItem = new DataMeProcedure();
		dataItem.errorCode = errorCode;
		dataItem.errorMsg = CommonUtil.noNullString(errorMsg);
		
		return dataItem;
	}
}
