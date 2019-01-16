package com.kol.pes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import com.kol.pes.item.femaleworker.DataGroupInfoItem;
import com.kol.pes.item.femaleworker.DataOrgInfoItem;
import com.kol.pes.item.femaleworker.DataWipInfoItem;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kol.pes.item.DataDateShift;
import com.kol.pes.item.DataLoginItem;
import com.kol.pes.item.DataMeAssetSeqInfo;
import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataMeProduceItem;
import com.kol.pes.item.DataMeSearchSeqItem;
import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.item.DataMeStartedSeqItem;
import com.kol.pes.item.DataQaNeedFillItem;
import com.kol.pes.item.DataQaReqItem;
import com.kol.pes.item.DataQaValueItem;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;
import com.kol.pes.utils.QRCodeUtil;


@Repository("mesDao")
public class MesDaoImpl implements MesDao {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	
	
	
	//登录，返回登录人的信息
	public DataLoginItem login(String userId, String password) throws SQLException {
		if(CommonUtil.isValidNumber(userId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String selectUserSql = "select staff_no, staff_name, pwd from kol_pes_staffs where staff_no = '"+userId+"'";
			LogUtil.log("LoginDaoImpl:selectUserSql="+selectUserSql);
			return runner.query(selectUserSql, new ResultSetHandler<DataLoginItem>() {
	
				public DataLoginItem handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					while(rs.next()) { 
						DataLoginItem user = new DataLoginItem();
						user.staffNo = rs.getString("STAFF_NO");
						user.staffName = CommonUtil.noNullString(rs.getString("STAFF_NAME"));
						user.pwd = rs.getString("PWD");
						LogUtil.log("user.staffName="+user.staffName+", user.pwd="+user.pwd);
						return user;
					}
					return null;
				}
			});
		}
		return null;
	}
	
	public boolean setPassword(String userId, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "update apps.per_all_people_f set attribute2 = '"+password+"' where employee_number = '"+userId+"' and nvl(effective_end_date,sysdate) > sysdate";
		int res = runner.update(sql);
		return res > 0;
	}

	@Override
	public List<DataMeProduceItem> getProduceListByProjectNum(String projectNum) throws SQLException {
		if(CommonUtil.isStringNotNull(projectNum)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String selectSql = "select njw.wip_entity_id,njw.wip_entity_name || ' ' || njw.attribute6 || ' ' || njw.item_number || ' ' || njw.description job_desc from ni_job_sheets njs, ni_job_wips njw Where njs.org_id = njw.org_id And njs.job_id = njw.job_id And njs.ver_no = njw.ver_no And njs.is_latest = 'Y' and nvl(njs.cancel_flag, 'N') = 'N' and nvl(njs.completed_status, 0) not in ('2', '3') and (njs.status = 'A' or njs.ver_no > 1) and njs.job_id >= 100001 and njs.org_id = 85 and njw.status in ('1', '3', '6') and njs.job_no = '"+projectNum+"'";
			LogUtil.log("getProduceListByProjectNum()="+selectSql);
			return runner.query(selectSql, new ResultSetHandler<List<DataMeProduceItem>>() {
	
				public List<DataMeProduceItem> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					List<DataMeProduceItem> tempList = new ArrayList<DataMeProduceItem>();
					while(rs.next()) { 
						DataMeProduceItem item = new DataMeProduceItem();
						item.wipEntityId = CommonUtil.noNullString(rs.getString("wip_entity_id"));
						item.jobDesc = CommonUtil.noNullString(rs.getString("job_desc"));

						tempList.add(item);
					}
					return tempList;
				}
			});
		}
		return null;
	}

	@Override
	public DataMeAssetSeqInfo getAssetSeqInfoByAssetId(String assetId) throws SQLException {
		LogUtil.log("getAssetSeqInfoByAssetId():assetCode="+assetId);
		if(CommonUtil.isStringNotNull(assetId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String selectSql = "select resource_code, resource_id, description, attribute4 from bom_resources where organization_id = 85 and resource_code = '"+assetId+"'";
			
			return runner.query(selectSql, new ResultSetHandler<DataMeAssetSeqInfo>() {
				
				public DataMeAssetSeqInfo handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }

					while(rs.next()) { 
						DataMeAssetSeqInfo item = new DataMeAssetSeqInfo();
						item.resourceId = CommonUtil.noNullString(rs.getString("resource_id"));
						item.resourceCode = CommonUtil.noNullString(rs.getString("resource_code"));
						item.description = CommonUtil.noNullString(rs.getString("description"));
						item.attribute4 = CommonUtil.noNullString(rs.getString("attribute4"));
						return item;
					}
					return null;
				}
			});
		}
		return null;
	}

	@Override
	public List<DataMeSeqInfoData> getSeqInfoBySeqId(String assetCode, String wipId) throws SQLException {
		if(CommonUtil.isStringNotNull(assetCode)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			String selectSql = "select nvl(wo.operation_seq_num,0) seqnum, b.operation_code, b.operation_description  from bom_department_resources_v a,   bom_standard_operations_v b,   bom_departments_v c ,   (select operation_seq_num, standard_operation_id, organization_id from wip_operations where wip_entity_id = "+wipId+") wo where a.resource_code = '"+assetCode+"' and wo.standard_operation_id(+) = b.standard_operation_id and a.department_id = b.department_id and b.organization_id = 85 and b.organization_id = c.organization_id and b.department_id = c.department_id and a.attribute1 is null and b.attribute8 is null and b.ATTRIBUTE9 = 'Y'";
			
			//String selectSql = "select b.operation_code, b.operation_description from bom_department_resources_v a, bom_standard_operations_v b,   bom_departments_v c where a.resource_code = '"+assetCode+"' and a.department_id = b.department_id and b.organization_id = 85 and b.organization_id = c.organization_id and b.department_id = c.department_id and a.attribute1 is null and b.attribute8 is null and b.attribute9 = 'Y'";
			LogUtil.log("getSeqInfoBySeqId():selectSql="+selectSql);
			return runner.query(selectSql, new ResultSetHandler<List<DataMeSeqInfoData>>() {
				
				public List<DataMeSeqInfoData> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					
					List<DataMeSeqInfoData> tempList = new ArrayList<DataMeSeqInfoData>();

					while(rs.next()) { 
						DataMeSeqInfoData item = new DataMeSeqInfoData();
						item.seqNum = CommonUtil.noNullString(rs.getString("seqnum"));
						item.operationCode = CommonUtil.noNullString(rs.getString("operation_code"));
						item.operationDescription = CommonUtil.noNullString(rs.getString("operation_description"));
						
						tempList.add(item);
					}
					return tempList;
				}
			});
		}
		return null;
	}

	@Override
	public DataMeProcedure getDescInfoWhenSeqSelected(String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String staffNo, String seqNum)
			throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.operationStart(?,?,?,?,?,?,?,?)}");
		
		cstmt.setInt(1, Integer.valueOf(wipId));
		cstmt.setString(2, opCode);
		cstmt.setString(3, assetId);
		cstmt.setString(4, schedDate);
		
		
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(6, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(7, java.sql.Types.VARCHAR);
		
		cstmt.setString(8, seqNum);
		
		cstmt.execute();
		
		String display = cstmt.getString(5);
		int errorCode = cstmt.getInt(6);
		String errorMsg = cstmt.getString(7);
		
		LogUtil.log("getDescInfoWhenSeqSelected():display="+display+", seqnum ="+seqNum+" errorCode="+errorCode+", errorMsg="+errorMsg);
		cstmt.close();
		con.close();
		
		DataMeProcedure dataItem = new DataMeProcedure();
		dataItem.display = CommonUtil.noNullString(display);
		dataItem.errorCode = errorCode;
		dataItem.errorMsg = CommonUtil.noNullString(errorMsg);
		
		return dataItem;
	}

	@Override
	public DataMeProcedure getDescInfoWhenStartingSeqClicked(String wipId, String opCode, String assetId, String schedDate, String seqNum)
			throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.operationStarting(?,?,?,?,?,?,?,?,?,?)}");
		
		cstmt.setInt(1, Integer.valueOf(wipId));
		cstmt.setString(2, opCode);
		cstmt.setString(3, assetId);
		cstmt.setString(4, schedDate);
		
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);//p_def_start 
		cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);//p_display
		cstmt.registerOutParameter(7, java.sql.Types.INTEGER);//p_avail_qty
		
		cstmt.registerOutParameter(8, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
		
		cstmt.setString(10, seqNum);
		
		cstmt.execute();
		
		String defaultStartDate = cstmt.getString(5);
		String display = cstmt.getString(6);
		int availQty = cstmt.getInt(7);
		int errorCode = cstmt.getInt(8);
		String errorMsg = cstmt.getString(9);
		
		LogUtil.log("getDescInfoWhenStartingSeqClicked():display="+display+", defaultStartDate="+defaultStartDate+", availQty="+availQty+", errorCode="+errorCode+", errorMsg="+errorMsg);
		cstmt.close();
		con.close();
		
		DataMeProcedure dataItem = new DataMeProcedure();
		dataItem.defaultStartDate = CommonUtil.noNullString(defaultStartDate);
		dataItem.display = CommonUtil.noNullString(display);
		dataItem.availQty = availQty;
		dataItem.errorCode = errorCode;
		dataItem.errorMsg = CommonUtil.noNullString(errorMsg);
		
		dataItem.databaseTime = CommonUtil.noNullString(getDatabaseTime());
		
		return dataItem;
	}
	
	private String getDatabaseTime() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String selectSql = "select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') as t from dual";
		LogUtil.log("getDatabaseTime()="+selectSql);
		return runner.query(selectSql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				while(rs.next()) { 
					return CommonUtil.noNullString(rs.getString("t"));
				}
				return null;
			}
		});
	}
	
	public DataMeProcedure startSeq(String staffNo, String inputQty, String startOpTime, String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String seqNum, String pafteropSeqNum, String workClassCode) throws SQLException {

		final String trxId = insertWhenStartAnOp(new Date(Calendar.getInstance().getTimeInMillis()), staffNo, new Date(Calendar.getInstance().getTimeInMillis()), staffNo, wipId, opCode, inputQty, assetId, new Date(CommonUtil.convertStringToCal(startOpTime).getTimeInMillis()),seqNum, workClassCode);
		LogUtil.log("startSeq():trxId="+trxId);
		if(trxId!=null) {
			
			Connection con = this.dataSource.getConnection();
			CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.operationStarted(?,?,?,?,?,?,?,?,?,?)}");
			
			cstmt.setInt(1, Integer.valueOf(wipId));
			cstmt.setString(2, opCode);
			cstmt.setString(3, assetId);
			cstmt.setString(4, schedDate);
			cstmt.setString(5, pafteropSeqNum);
			cstmt.setString(6, staffNo);
			
			cstmt.registerOutParameter(7, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(8, java.sql.Types.VARCHAR);
			cstmt.setString(9, seqNum);
			cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);
			
			cstmt.execute();
			
			int errorCode = cstmt.getInt(7);
			String errorMsg = cstmt.getString(8);
			int xSeqNum = cstmt.getInt(10);
			
			LogUtil.log("startSeq():errorCode="+errorCode+", errorMsg="+errorMsg+", workClassCode="+workClassCode);
			cstmt.close();
			con.close();
			
			DataMeProcedure dataItem = new DataMeProcedure();
			dataItem.errorCode = errorCode;
			dataItem.errorMsg = errorMsg;
			
			if(errorCode>=0 && errorCode<2) {
				try {
					QRCodeUtil.encode(trxId, "/tmp/qrcode/");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			updateStartSeqSeqNum(trxId, String.valueOf(xSeqNum));
			
			return dataItem;
		}
		
		return null;
	}
	
	private boolean updateStartSeqSeqNum(String trxId, String xSeqNum) throws SQLException {

		QueryRunner runner = new QueryRunner(this.dataSource);
		
		Object[] sqlParam = new Object[] {xSeqNum};
		String sql = "update kol_pes_move_txn_result set operation_seq_num=? where TRANSACTION_ID="+trxId;
		int res = runner.update(sql, sqlParam);
		LogUtil.log("updateStartSeqSeqNum():sql="+sql+", trxId="+trxId+", xSeqNum="+xSeqNum);
		return res>0;
	}
	
	public List<DataMeStartedSeqItem> getStartedSeqList(String assetId) throws SQLException {
		if(CommonUtil.isStringNotNull(assetId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String selectSql = "select (select max(njs.job_no) || '(' || max(njw.wip_entity_name) || ')' from ni_job_sheets njs, ni_job_wips njw where njs.job_id = njw.job_id and njs.ver_no = njw.ver_no and njs.org_id = njw.org_id And njs.is_latest = 'Y' and njw.wip_entity_id = r.wip_entity_id) || ' ' || case when r.workclasscode = 'DAY' then '日班' else '晚班' end job, r.fm_operation_code, r.transaction_id, r.wip_entity_id, r.op_start, r.trx_quantity, r.operation_seq_num from kol_pes_move_txn_result r where r.asset_id = "+assetId+" and r.interfaced = 0 order by transaction_id desc";
			LogUtil.log("getStartedSeqList()="+selectSql);
			return runner.query(selectSql, new ResultSetHandler<List<DataMeStartedSeqItem>>() {
	
				public List<DataMeStartedSeqItem> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					List<DataMeStartedSeqItem> tempList = new ArrayList<DataMeStartedSeqItem>();
					while(rs.next()) { 
						DataMeStartedSeqItem item = new DataMeStartedSeqItem();
						item.wipId = CommonUtil.noNullString(rs.getString("wip_entity_id"));
						item.transactionId = CommonUtil.noNullString(rs.getString("transaction_id"));
						item.job = CommonUtil.noNullString(rs.getString("job"));
						item.fmOperationCode = CommonUtil.noNullString(rs.getString("fm_operation_code"));
						item.seqNum = CommonUtil.noNullString(rs.getString("operation_seq_num"));
						item.opStart = CommonUtil.noNullString(rs.getString("op_start"));
						item.trxQuantity = CommonUtil.noNullString(rs.getString("trx_quantity"));

						tempList.add(item);
						break;
					}
					return tempList;
				}
			});
		}
		return null;
	}
	
	public boolean deleteStartedSeq(String trxId) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "delete from kol_pes_move_txn_result where transaction_id ="+trxId;
		int res = runner.update(sql);
		return res > 0;
	}
	
	public DataMeProcedure endOp(String trxId, String wipId, String opCode, String planId, String staffNo, String inputQty, String scrapQty, String endTime, List<DataQaReqItem> qaDataList, String workClassCode, String seqNum, String schedDate) throws SQLException {
		//插入自定义质量管理表
		final int qaInsertRes = insertQaItems(trxId, CommonUtil.getNowDate(), staffNo, CommonUtil.getNowDate(), staffNo, qaDataList, planId);
		int opEndRes = 0;
		//如果好货数不为空，不知道他要干什么
		if(CommonUtil.isStringNotNull(inputQty)) {
			opEndRes = updateWhenEndOp(trxId, Integer.valueOf(inputQty), Integer.valueOf(scrapQty), new Date(CommonUtil.convertStringToCal(endTime).getTimeInMillis()), staffNo, CommonUtil.getNowDate(), seqNum);
		}
		else {
			opEndRes = updateWhenEndOp(trxId, Integer.valueOf(scrapQty), new Date(CommonUtil.convertStringToCal(endTime).getTimeInMillis()), staffNo, CommonUtil.getNowDate(), seqNum);
		}
		
		if(qaInsertRes>0 && opEndRes>0) {
			//当质量管理，和自动以移动表插入成功，则
			DataMeProcedure proRes = endSeqProcedure(trxId, wipId, opCode, staffNo, workClassCode, schedDate);
			if(proRes.errorCode>1) {
				deleteQaReslutWhenOpEndFail(trxId);
				updateResetOpEndedToNull(trxId);
			}
			return proRes;
		}
		return null;
	}
	
	public List<DataMeSearchSeqItem> searchSeqList(String wipId) throws SQLException {
		if(CommonUtil.isStringNotNull(wipId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String selectSql = "select TRANSACTION_ID ID, FM_OPERATION_CODE 工序号, TRX_QUANTITY 数量, SCRAP_QUANTITY 损耗, last_update_date 更新日期 from kol_pes_move_txn_result where wip_entity_id = "+wipId;
			LogUtil.log("searchSeqList()="+selectSql);
			return runner.query(selectSql, new ResultSetHandler<List<DataMeSearchSeqItem>>() {
	
				public List<DataMeSearchSeqItem> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					List<DataMeSearchSeqItem> tempList = new ArrayList<DataMeSearchSeqItem>();
					while(rs.next()) { 
						DataMeSearchSeqItem item = new DataMeSearchSeqItem();
						item.trxId = CommonUtil.noNullString(rs.getString("ID"));
						item.opCode = CommonUtil.noNullString(rs.getString("工序号"));
						item.trxQty = CommonUtil.noNullString(rs.getString("数量"));
						item.scrapQty = CommonUtil.noNullString(rs.getString("损耗"));
						item.updateDate = CommonUtil.noNullString(rs.getString("更新日期"));

						tempList.add(item);
						break;
					}
					return tempList;
				}
			});
		}
		return null;
	}
	
	public String receiveOrderGetQtyById(String id, String staffNum) throws SQLException {
		if(CommonUtil.isStringNotNull(id) && CommonUtil.isStringNotNull(staffNum)) {
//			QueryRunner runner = new QueryRunner(this.dataSource);
//			String selectSql = "select TRX_QUANTITY from kol_pes_move_txn_result where transaction_id ="+id;
//			
//			return runner.query(selectSql, new ResultSetHandler<String>() {
//				
//				public String handle(ResultSet rs) throws SQLException {
//					if (rs == null) {
//			            return null;
//			        }
//
//					while(rs.next()) { 
//						return CommonUtil.noNullString(rs.getString("TRX_QUANTITY"));
//					}
//					return null;
//				}
//			});
			
			Connection con = this.dataSource.getConnection();
			CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.getAcceptQty(?,?,?,?,?)}");
			
			cstmt.setInt(1, Integer.valueOf(id));
			cstmt.setString(2, staffNum);

			cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			
			cstmt.execute();

			int trxQty = cstmt.getInt(3);
			int errorCode = cstmt.getInt(4);
			String errorMsg = cstmt.getString(5);
			
			LogUtil.log("endSeq():errorCode="+errorCode+", errorMsg="+errorMsg+", trxQty="+trxQty);
			cstmt.close();
			con.close();
			
			if(errorCode>=0 && errorCode<2) {
				return String.valueOf(trxQty);
			}
		}
		return null;
	}
	
	public boolean receiveOrderSureReceive(String id, String qty, String staffNo) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "update kol_pes_move_txn_result set accept_qty = "+qty+", last_updated_by ='"+staffNo+"', last_update_date =sysdate where transaction_id ="+id;
		int res = runner.update(sql);
		return res > 0;
	}

	public boolean receiveOrderSureReject(String id, String staffNo) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "update kol_pes_move_txn_result set accept_qty = 0, last_updated_by ='"+staffNo+"', last_update_date =sysdate where transaction_id = "+id;
		int res = runner.update(sql);
		return res > 0;
	}
	
	
	
	private DataMeProcedure endSeqProcedure(String trxId, String wipId, String opCode, String staffNo, String workClassCode, String schedDate) throws SQLException {
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.operationComplete(?,?,?,?,?,?,?)}");
		
		cstmt.setInt(1, Integer.valueOf(trxId));
		cstmt.setInt(2, Integer.valueOf(wipId));
		cstmt.setString(3, opCode);
		cstmt.setString(4, workClassCode);

		cstmt.registerOutParameter(5, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
		
		cstmt.setString(7, schedDate);
		
		cstmt.execute();

		int errorCode = cstmt.getInt(5);
		String errorMsg = cstmt.getString(6);
		
		LogUtil.log("endSeq():errorCode="+errorCode+", errorMsg="+errorMsg);
		cstmt.close();
		con.close();
		
		DataMeProcedure dataItem = new DataMeProcedure();
		dataItem.errorCode = errorCode;
		dataItem.errorMsg = errorMsg;
		
		return dataItem;
	}
	
	//生成ID
	private String getTransactionId() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		Object[] resultArray = runner.query("select PES_MOVE_TXN_S.nextval from dual", new ArrayHandler());
		
		if (resultArray != null && resultArray.length > 0) {
            return resultArray[0].toString();
		}
		return null;
	}
	
	private String insertWhenStartAnOp(Date CREATION_DATE, String CREATED_BY,
									Date LAST_UPDATE_DATE, String LAST_UPDATED_BY, 
									String WIP_ENTITY_ID, String FM_OPERATION_CODE, String TRX_QUANTITY,
									String assetId, Date OP_START, String seqNum, String workClassCode) throws SQLException {

		final String transactionId = getTransactionId();

		QueryRunner runner = new QueryRunner(this.dataSource);
		
		Object[] sqlParam = new Object[] {transactionId.trim(), CommonUtil.formatDateTime(CREATION_DATE), CREATED_BY.trim(), 
										  CommonUtil.formatDateTime(LAST_UPDATE_DATE), LAST_UPDATED_BY.trim(),
							  			  WIP_ENTITY_ID, FM_OPERATION_CODE, TRX_QUANTITY.trim(), 
							  			  assetId, CommonUtil.formatDateTime(OP_START), seqNum, workClassCode};
		
		int res = runner.update("insert into kol_pes_move_txn_result(TRANSACTION_ID, CREATION_DATE, CREATED_BY,"+
							    "LAST_UPDATE_DATE, LAST_UPDATED_BY, WIP_ENTITY_ID,"+
							    "FM_OPERATION_CODE, TRX_QUANTITY,"+
							    "ASSET_ID, OP_START, SCRAP_QUANTITY, operation_seq_num, workClassCode) values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'), 0, ?, ?)", sqlParam);
	
		if(res > 0) {
			return transactionId;
		}
		return null;
	}

	//完成工序时的数据库操作
	private int updateWhenEndOp(String transactionId, int SCRAP_QUANTITY, Date OP_END, String LAST_UPDATED_BY, Date LAST_UPDATE_DATE, String seqNum) throws SQLException {
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("update kol_pes_move_txn_result set ");
		sbSql.append("SCRAP_QUANTITY=?,");
		sbSql.append("LAST_UPDATED_BY=?,");
		sbSql.append("LAST_UPDATE_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
		sbSql.append("operation_seq_num=?,");
		sbSql.append("OP_END=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
		sbSql.append(" where TRANSACTION_ID=?");
		
		Object[] params = new Object[] {
						SCRAP_QUANTITY,
						LAST_UPDATED_BY.trim(),
						CommonUtil.formatDateTime(LAST_UPDATE_DATE),
						seqNum,
						CommonUtil.formatDateTime(OP_END),
						transactionId.trim()
					   };
		
		LogUtil.log("updateWhenEndOp():sql="+sbSql.toString());
		LogUtil.log("updateWhenEndOp():SCRAP_QUANTITY="+SCRAP_QUANTITY+", LAST_UPDATED_BY="+LAST_UPDATED_BY+", CommonUtil.formatDateTime(LAST_UPDATE_DATE)="+CommonUtil.formatDateTime(LAST_UPDATE_DATE)+", CommonUtil.formatDateTime(OP_END)="+CommonUtil.formatDateTime(OP_END)+", transactionId="+transactionId);
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		int resRows = runner.update(sbSql.toString(), params);
		
		return resRows;
	}
	
	//完成工序时的数据库操作
		private int updateWhenEndOp(String transactionId, int inputQty, int SCRAP_QUANTITY, Date OP_END, String LAST_UPDATED_BY, Date LAST_UPDATE_DATE, String seqNum) throws SQLException {
			
			StringBuilder sbSql = new StringBuilder();
			sbSql.append("update kol_pes_move_txn_result set trx_quantity=?,");
			sbSql.append("SCRAP_QUANTITY=?,");
			sbSql.append("LAST_UPDATED_BY=?,");
			sbSql.append("LAST_UPDATE_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
			sbSql.append("operation_seq_num=?,");
			sbSql.append("OP_END=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			sbSql.append(" where TRANSACTION_ID=?");
			
			Object[] params = new Object[] {
							inputQty,
							SCRAP_QUANTITY,
							LAST_UPDATED_BY.trim(),
							CommonUtil.formatDateTime(LAST_UPDATE_DATE),
							seqNum,
							CommonUtil.formatDateTime(OP_END),
							transactionId.trim()
						   };
			
			LogUtil.log("updateWhenEndOp()2:sql="+sbSql.toString());
			LogUtil.log("updateWhenEndOp()2:inputQty="+inputQty+", SCRAP_QUANTITY="+SCRAP_QUANTITY+", LAST_UPDATED_BY="+LAST_UPDATED_BY+", CommonUtil.formatDateTime(LAST_UPDATE_DATE)="+CommonUtil.formatDateTime(LAST_UPDATE_DATE)+", CommonUtil.formatDateTime(OP_END)="+CommonUtil.formatDateTime(OP_END)+", transactionId="+transactionId);
			
			QueryRunner runner = new QueryRunner(this.dataSource);
			int resRows = runner.update(sbSql.toString(), params);
			
			return resRows;
		}
	
	public List<DataQaNeedFillItem> getQaList(String opCode, String orgId) throws SQLException {
		final String planId = getPlanIdByOpCode(opCode, orgId);
		if(planId != null) {
			return getQaListToFillByPlanId(planId, opCode);
		}
		return null;
	}
	
	private String getPlanIdByOpCode(String opCode, String organizationId) throws SQLException {
		
		String sql = "select plan_id from kol_pes_op_plan where standard_operation_code='"+opCode+"'"+" and ORGANIZATION_ID="+organizationId+" and operator_meaning='equals'";
		
		LogUtil.log("getPlanIdByOpCode()equals:sql="+sql);
		
		QueryRunner runner = new QueryRunner(this.dataSource);

		String plan_id = runner.query(sql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				while(rs.next()) { 
					return rs.getString("plan_id");
				}
				return null;
			}
		});
		return plan_id;
	}
	
	public List<DataQaNeedFillItem> getQaListToFillByPlanId(final String planId, final String opCode) throws SQLException {

		String sql = "select * from (select c.plan_id, c.char_id, c.char_name, c.result_column_name, c.PROMPT_SEQUENCE, c.PROMPT, nvl(o.mandatory_flag, c.mandatory_flag) mandatory_flag, c.read_only_flag, nvl(o.displayed_flag, c.displayed_flag) displayed_flag, c.datatype_meaning, c.display_length, c.decimal_precision, c.derived_flag from kol_pes_plan_char c, kol_pes_plan_char_op o  where c.plan_id = " + planId + " and c.plan_id = o.plan_id (+) and c.char_id = o.char_id (+) and o.op_code (+) = '" + opCode +"') where displayed_flag = 1 and char_id not in (select assigned_char_id from kol_pes_char_action where kol_pes_char_action.plan_id = " + planId + " and (message like '%[%' or assign_type='S')) order by PROMPT_SEQUENCE";
		
		LogUtil.log("getQaListToFill():sql="+sql);
		
		final List<DataQaValueItem> qaValueList = getQaValueListByPlanId(planId);

		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<List<DataQaNeedFillItem>>() {

			public List<DataQaNeedFillItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				
				List<DataQaNeedFillItem> tempList = new ArrayList<DataQaNeedFillItem>();
				
				while(rs.next()) { 
					DataQaNeedFillItem qa = new DataQaNeedFillItem();
					qa.charId = rs.getString("CHAR_ID");
					qa.charName = rs.getString("CHAR_NAME");
					qa.prompt = rs.getString("PROMPT");
					qa.resultColumnName = rs.getString("RESULT_COLUMN_NAME");
					qa.datatypeMeaning = rs.getString("DATATYPE_MEANING");
					qa.derivedFlag = rs.getString("DERIVED_FLAG");
					qa.mandatoryFlag = rs.getString("MANDATORY_FLAG");
					qa.readOnlyFlag = rs.getString("READ_ONLY_FLAG");
					
					qa.planId = planId;
					
					if(qaValueList!=null && !"250".equals(qa.charId) && !"268".equals(qa.charId)) {//机器编号和操作员编号不使用可选数据，特殊处理一下
						for(DataQaValueItem qaValue : qaValueList) {
							if(qaValue!=null && qa.charId.equals(qaValue.charId)) {
								qa.addqaValueList(qaValue.shortCode, qaValue.description);
							}
						}
					}
					
					LogUtil.log("qa.prompt="+qa.prompt);
					
					tempList.add(qa);
				}
				
				return tempList;
			}
		});
	}
	
	//获取有固定值可供填写的质量管理计划项的值
	private List<DataQaValueItem> getQaValueListByPlanId(String planId) throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "select * from kol_pes_plan_char_value where plan_id="+planId;
		
		LogUtil.log("getQaValueListByPlanId():sql="+sql);
		
		return runner.query(sql, new ResultSetHandler<List<DataQaValueItem>>() {

			public List<DataQaValueItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				
				List<DataQaValueItem> qaValueListTemp = new ArrayList<DataQaValueItem>();
				
				while(rs.next()) {
					qaValueListTemp.add(new DataQaValueItem(rs.getString("char_id"), 
															rs.getString("short_code"), 
															rs.getString("description")));
				}
				
				return qaValueListTemp;
			}
		});
	}
	
	//插入质量管理数据
	public int insertQaItems(String transactionId, Date CREATION_DATE, String CREATED_BY,
							 Date LAST_UPDATE_DATE, String LAST_UPDATED_BY,
							 List<DataQaReqItem> qaDataList, String planId) throws SQLException {
		
		if(planId==null || planId.length()==0) {
			return 1;//如果没有质量管理数据，则不需要插表。为了迎合Action中的判断，返回1
		}
		
		if(isQaDataExist(transactionId, planId)) {
			deleteQaReslutWhenOpEndFail(transactionId);
		}
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		StringBuilder sqlSb = new StringBuilder();
		
		sqlSb.append("insert into kol_pes_qa_result(TRANSACTION_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PLAN_ID");
		
		for(DataQaReqItem qa : qaDataList) {
			if(qa.resultColumnName!=null) {
				if ("19".equals(qa.charId)) {
					qa.resultColumnName = "WIP_ENTITY_NAME";//特殊处理一下
				}
				sqlSb.append(", ").append(qa.resultColumnName);
			}
		}
		
		sqlSb.append(") values(");
		sqlSb.append(transactionId).
		append(", to_date('").append(CommonUtil.formatDateTime(CREATION_DATE)).append("','yyyy-mm-dd hh24:mi:ss')").
		append(", '").append(CREATED_BY).
		append("', to_date('").append(CommonUtil.formatDateTime(LAST_UPDATE_DATE)).append("','yyyy-mm-dd hh24:mi:ss')").
		append(", '").append(LAST_UPDATED_BY).
		append("', ").append(planId);
		
		for(DataQaReqItem qa : qaDataList) {
			if(qa.resultColumnName!=null) {
				sqlSb.append(", '").append(CommonUtil.removeYinhaoInString(qa.value)).append("'");
			}
		}
		
		sqlSb.append(")");
		
		LogUtil.log("insertQaItems():"+sqlSb.toString());

		return runner.update(sqlSb.toString());
	}
	
	private boolean isQaDataExist(String transId, String planId) throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		String sql = "select TRANSACTION_ID from kol_pes_qa_result where TRANSACTION_ID="+transId+" and PLAN_ID="+planId;
		
		String res = runner.query(sql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				
				while(rs.next()) {
					return (rs.getString("TRANSACTION_ID"));
				}
				
				return null;
			}
		});
		
		return res!=null && res.length()>0;
	}
	
//	//删除工序结束失败的质量管理计划项
//	private int deleteQaReslutWhenOpEndFail(String transactionId, String PLAN_ID) throws SQLException {
//		String sql = "delete from kol_pes_qa_result where transaction_id="+transactionId.trim()+" and PLAN_ID="+PLAN_ID;
//		
//		QueryRunner runner = new QueryRunner(this.dataSource);
//		int resRows = runner.update(sql);
//		LogUtil.log("deleteQaReslutWhenOpEndFail:resRows="+resRows+", sql="+sql);
//		return resRows;
//	}
	
	//将工序的endDate更新为空
	public int updateResetOpEndedToNull(String transactionId) throws SQLException {
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("update kol_pes_move_txn_result set ");
		sbSql.append("SCRAP_QUANTITY=0,");
		sbSql.append("OP_END=null");
		sbSql.append(" where TRANSACTION_ID=?");
		
		Object[] params = new Object[] {transactionId.trim()};
		
		LogUtil.log("updateResetOpEndedToNull():sql="+sbSql.toString());
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		int resRows = runner.update(sbSql.toString(), params);
		LogUtil.log("updateResetOpEndedToNull:resRows="+resRows);
		
		deleteQaReslutWhenOpEndFail(transactionId);
		
		return resRows;
	}
		
	//删除工序结束失败的质量管理计划项
	private int deleteQaReslutWhenOpEndFail(String transactionId) throws SQLException {
		String sql = "delete from kol_pes_qa_result where transaction_id="+transactionId.trim();
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		int resRows = runner.update(sql);
		LogUtil.log("deleteQaReslutWhenOpEndFail:resRows="+resRows+", sql="+sql);
		return resRows;
	}
	
	
	public DataDateShift getDateShift() throws SQLException {
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.getCurrentShift(?,?)}");
		
		cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		
		cstmt.execute();

		String date = cstmt.getString(1);
		String shift = cstmt.getString(2);
		
		LogUtil.log("getDateShift():date="+date+", shift="+shift);
		cstmt.close();
		con.close();
		
		DataDateShift dataItem = new DataDateShift();
		dataItem.date = date;
		dataItem.shift = shift;
		
		return dataItem;
	}
	
	public List<DataMeSeqInfoData> getPafteropList(String wipId) throws SQLException {
		if(CommonUtil.isStringNotNull(wipId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			String selectSql = "select wo.operation_seq_num, bso.operation_code, bso.operation_description from wip_operations wo, bom_standard_operations bso where wo.standard_operation_id = bso.standard_operation_id and wo.organization_id = bso.organization_id and wo.quantity_waiting_to_move > 0 and wo.wip_entity_id = " + wipId;
			
			//String selectSql = "select bso.operation_code, bso.operation_description from wip_operations wo, bom_standard_operations bso where wo.standard_operation_id = bso.standard_operation_id and wo.organization_id = bso.organization_id and wo.quantity_waiting_to_move > 0 and wo.wip_entity_id = " + wipId;
			LogUtil.log("getPafteropList()="+selectSql);
			return runner.query(selectSql, new ResultSetHandler<List<DataMeSeqInfoData>>() {
	
				public List<DataMeSeqInfoData> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					List<DataMeSeqInfoData> tempList = new ArrayList<DataMeSeqInfoData>();
					
					while(rs.next()) { 
						DataMeSeqInfoData item = new DataMeSeqInfoData();
						item.seqNum = CommonUtil.noNullString(rs.getString("operation_seq_num"));
						item.operationCode = CommonUtil.noNullString(rs.getString("operation_code"));
						item.operationDescription = CommonUtil.noNullString(rs.getString("operation_description"));
						tempList.add(item);
					}
					
					return tempList;
				}
			});
		}
		return null;
	}
	/*
	*
	* 女工获取组别
	* */
	@Override
	public List<DataGroupInfoItem> getGroupList4F(String assetNumber) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		//String sql = "select r.resource_id, lookup_master_code, r.description from ni_lookup l, bom_resources r where lookup_detail_code = "+staffNo+"  and (lookup_type_code = 'NI MES Machines')   and l.lookup_master_code = r.resource_code  and r.organization_id = 85  order by r.resource_code";
		String sql = "SELECT N2.LOOKUP_DETAIL_CODE," +
		"       N2.LOOKUP_DETAIL_MEANING " +
				"FROM   NI_LOOKUP N1," +
				"       NI_LOOKUP N2 " +
				"WHERE  N2.LOOKUP_DETAIL_CODE LIKE N1.LOOKUP_DETAIL_CODE || '%'" +
				"       AND N2.LOOKUP_TYPE_CODE = 'NI_HAND_KIND_GROUP'" +
				"       AND N2.LOOKUP_LEVEL = 2" +
				"       AND N2.ENABLED_FLAG = 'Y'" +
				"       AND N1.LOOKUP_TYPE_CODE = 'NI_HAND_USER_PRIVILEGE'" +
				"       AND N1.LOOKUP_LEVEL = 2" +
				"       AND N1.LOOKUP_MASTER_CODE = '"+assetNumber+"'"+
				"       AND N1.ENABLED_FLAG = 'Y'";
		LogUtil.log("getGroupList4F():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<DataGroupInfoItem>>() {

			public List<DataGroupInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<DataGroupInfoItem> tempList = new ArrayList<DataGroupInfoItem>();

				while(rs.next()) {
					DataGroupInfoItem data = new DataGroupInfoItem();
					data.groupName = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_CODE"));
					data.groupCode = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_MEANING"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	@Override
	public List<DataOrgInfoItem> getOrgList() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT ORG.ORGANIZATION_ID, " +
				"       ORG.ORGANIZATION_CODE, " +
				"       ORG.ORGANIZATION_NAME " +
				"  FROM ORG_ORGANIZATION_DEFINITIONS ORG " +
				" ORDER BY ORG.ORGANIZATION_ID ASC";
		LogUtil.log("getOrgList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<DataOrgInfoItem>>() {

			public List<DataOrgInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<DataOrgInfoItem> tempList = new ArrayList<DataOrgInfoItem>();

				while(rs.next()) {
					DataOrgInfoItem data = new DataOrgInfoItem();
					data.organizationCode = CommonUtil.noNullString(rs.getString("ORGANIZATION_CODE"));
					data.organizationId = CommonUtil.noNullString(rs.getString("ORGANIZATION_ID"));
					data.organizationName = CommonUtil.noNullString(rs.getString("ORGANIZATION_NAME"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	@Override
	public List<DataWipInfoItem> getWipList4F(String jobNo,String organizationId) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT V.JOB_ID," +
				"       V.JOB_NO," +
				"       V.JOB_NAME, " +
				"       V.WIP_ENTITY_NAME,  " +
				"       V.WIP_ENTITY_ID,  " +
				"       V.INVENTORY_ITEM_ID,  " +
				"       V.ITEM_NUMBER, " +
				"       V.ITEM_DESC,  " +
				"       V.OPERATION_CODE, " +
				"       V.OPERATION_SEQ_NUM,  " +
				"       V.OPERATION_DESC,  " +
				"       V.WIP_ENTITY_NAME||' '||V.ATTRIBUTE6||' '||V.ITEM_NUMBER||' '||V.ITEM_DESC JOB_DESC " +
				"FROM   NI_HAND_SCH_OTH_MES_V V " +
				"WHERE  V.ORG_ID = '"+organizationId+"'" +
				"AND V.JOB_NO = '"+jobNo+"'";
		LogUtil.log("getWipList4F():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<DataWipInfoItem>>() {

			public List<DataWipInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<DataWipInfoItem> tempList = new ArrayList<DataWipInfoItem>();

				while(rs.next()) {
					DataWipInfoItem data = new DataWipInfoItem();
					data.jobId = CommonUtil.noNullString(rs.getString("JOB_ID"));
					data.jobNo = CommonUtil.noNullString(rs.getString("JOB_NO"));
					data.jobName = CommonUtil.noNullString(rs.getString("JOB_NAME"));
					data.wipEntityName = CommonUtil.noNullString(rs.getString("WIP_ENTITY_NAME"));
					data.wipEntityId = CommonUtil.noNullString(rs.getString("WIP_ENTITY_ID"));
					data.inventoryItemId = CommonUtil.noNullString(rs.getString("INVENTORY_ITEM_ID"));
					data.itemNumber = CommonUtil.noNullString(rs.getString("ITEM_NUMBER"));
					data.itemDesc = CommonUtil.noNullString(rs.getString("ITEM_DESC"));
					data.operationCode = CommonUtil.noNullString(rs.getString("OPERATION_CODE"));
					data.operationSeqNum = CommonUtil.noNullString(rs.getString("OPERATION_SEQ_NUM"));
					data.operationDesc = CommonUtil.noNullString(rs.getString("OPERATION_DESC"));
					data.jobDesc = CommonUtil.noNullString(rs.getString("JOB_DESC"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}


}
