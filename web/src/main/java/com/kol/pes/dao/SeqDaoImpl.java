/*-----------------------------------------------------------

-- PURPOSE

--    处理工序的数据库操作类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.dao;

import com.kol.pes.item.*;
import com.kol.pes.item.femaleworker.*;
import com.kol.pes.pojo.*;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Repository("seqDao")
public class SeqDaoImpl implements SeqDao {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("osJobDao")
	private OsJobDao osJobDao;

	
	public String getMachineReportTime(String assetCode, String scheduleDate, String workClassCode) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call apps.kol_pes_util_pkg.getMachineReportedTime(?,?,?,?)}");
		
		cstmt.setString(1, assetCode);
		cstmt.setString(2, scheduleDate);
		cstmt.setString(3, workClassCode);
		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);

		cstmt.execute();
		
		String display = CommonUtil.noNullString(cstmt.getString(4));
		LogUtil.log("getMachineReportTime():assetCode="+assetCode+", scheduleDate="+scheduleDate+", workClassCode="+workClassCode+", display="+display);
		cstmt.close();
		con.close();
		
		return display;
	}
	
	public List<DataProcessItem> getProcessList(String resCode, String scheduleDate, String shift) throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.buildJobList(?,?,?,?,?,?)}");
		
		cstmt.setString(1, resCode);
		cstmt.setString(2, scheduleDate);
		cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
		cstmt.setString(6, shift);
		cstmt.execute();
		
		String display = CommonUtil.noNullString(cstmt.getString(3));
		int errCode = cstmt.getInt(4);
		String errMsg = CommonUtil.noNullString(cstmt.getString(5));
		
		cstmt.close();
		con.close();
		
		LogUtil.log("getProcessList():errCode="+errCode+", errMsg="+errMsg+", resCode="+resCode+", scheduleDate="+scheduleDate+", display="+display);
		
		if(errCode == 0 && CommonUtil.isStringNotNull(display)) {
			
			List<DataProcessItem> tempList = new ArrayList<DataProcessItem>();
			
			String[] lineArr = display.split("\\|");
			
			if(lineArr!=null && lineArr.length>0) {
				for(String line : lineArr) {
					if(CommonUtil.isStringNotNull(line)) {
						String[] cumArr = line.split("\\~");
						LogUtil.log("getProcessList():cumArr.length="+cumArr.length+", line="+line);
						if(cumArr!=null && cumArr.length>=12) {
							DataProcessItem data = new DataProcessItem();
							data.projectNum = CommonUtil.noNullString(cumArr[0]);
							data.processNum = CommonUtil.noNullString(cumArr[1]);
							data.publishCode = CommonUtil.noNullString(cumArr[2]);
							data.productCode = CommonUtil.noNullString(cumArr[3]);
							data.productDesc = CommonUtil.noNullString(cumArr[4]);
							data.wipId = CommonUtil.noNullString(cumArr[5]);
							data.opCode = CommonUtil.noNullString(cumArr[6]);
							data.seqNum = CommonUtil.noNullString(cumArr[7]);
							data.seqDesc = CommonUtil.noNullString(cumArr[8]);
							data.planNum = CommonUtil.noNullInt(cumArr[9]);
							data.timeNeeded = CommonUtil.noNullString(cumArr[10]);
							data.reportedQty = CommonUtil.noNullString(cumArr[11]);
							tempList.add(data);
						}
					}
				}
			}
			
			return tempList;
		}
		
		return null;
	}
	 
	//根据commonSequenceId获取工序列表
	public List<DataSeqItem> findSeq(int seqId) throws SQLException {
		
		String sql = "select * from kol_pes_op_seq where routing_sequence_id = "+seqId+"order by operation_seq_num asc";
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<List<DataSeqItem>>() {

			public List<DataSeqItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				
				List<DataSeqItem> tempList = new ArrayList<DataSeqItem>();
				
				while(rs.next()) { 
					DataSeqItem data = new DataSeqItem();
					data.routingSeqId = rs.getInt("ROUTING_SEQUENCE_ID");
					data.operationSequenceId = rs.getString("OPERATION_SEQUENCE_ID");
					data.standardSequenceId =  rs.getString("STANDARD_OPERATION_ID");
					data.operationSeqNum = rs.getInt("OPERATION_SEQ_NUM");
					data.standardOperationCode = rs.getString("STANDARD_OPERATION_CODE");
					data.departmentCode = rs.getString("DEPARTMENT_CODE");
					data.departmentId = rs.getString("DEPARTMENT_ID");
					data.operationDescription = rs.getString("OPERATION_DESCRIPTION");
					
					if(data.operationDescription!=null && data.operationDescription.contains("&")) {
						data.operationDescription = data.operationDescription.replace("&", "&amp;");
					}
					
					tempList.add(data);
				}
				
				LogUtil.log("SeqDaoImpl:tempList.size()="+tempList.size());
				
				return tempList;
			}
		});
	}
	
	//生成ID
	public String getTransactionId() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		Object[] resultArray = runner.query("select PES_MOVE_TXN_S.nextval from dual", new ArrayHandler());
		
		//testTransactionId();
		
		if (resultArray != null && resultArray.length > 0) {
            return resultArray[0].toString();
		}
		
		return null;
	}
	
	private String getOpCodeByOpId(int routing_sequence_id, String curOperationId) throws NumberFormatException, SQLException {

		String sql = "select STANDARD_OPERATION_CODE from kol_pes_op_seq where routing_sequence_id="+routing_sequence_id+" and STANDARD_OPERATION_ID="+curOperationId+" order by operation_seq_num asc";
		
		LogUtil.log("getOpCodeByOpId():sql="+sql);
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }

				while(rs.next()) {
					return rs.getString("STANDARD_OPERATION_CODE");
				}

				return null;
			}
		});
	}
	
	//根据工单id获取工单的未完成数量
	private int getWipIncompleteQuantityByWipId(int wipEntityId) throws SQLException {
		String sql = "select incomplete_quantity from kol_pes_os_job where wip_entity_id="+wipEntityId;
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<Integer>() {

			public Integer handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return -1;
		        }
			
				while(rs.next()) { 
					return rs.getInt("incomplete_quantity");
				}
				
				return -1;
			}
		});
	}
	
	//当完成一个工序的最后一个拆分加工时，需要知道这个工序之前所有拆分的坏品数之和。这个函数就是获取这个值的
	public String getTotalScrapQuantityOfOtherPart(String wipId, String opCode) throws SQLException {

		String sql = "select sum(scrap_quantity) as aaa from kol_pes_move_txn_result where wip_entity_id="+wipId+" and fm_operation_code='"+opCode+"' and interfaced=0 and op_end is not null";
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		int scrapTotal = runner.query(sql, new ResultSetHandler<Integer>() {

			public Integer handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return 0;
		        }
			
				while(rs.next()) { 
					return rs.getInt("aaa");
				}
				
				return 0;
			}
		});
		
		LogUtil.log("getTotalScrapQuantityOfOtherPart():sql="+sql);
		LogUtil.log("getTotalScrapQuantityOfOtherPart():scrapTotal="+scrapTotal);

		return String.valueOf(scrapTotal);
	}

	@Override
	public String getMachineReportTime4f(String resCode, String scheduleDate, String shift) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call cux_niwip_handwork_pkg.get_machine_reported_time(?,?,?,?)}");

		cstmt.setString(1, resCode);
		cstmt.setString(2, scheduleDate);
		cstmt.setString(3, shift);
		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);

		cstmt.execute();

		String display = CommonUtil.noNullString(cstmt.getString(4));
		LogUtil.log("getMachineReportTime4f():assetCode="+resCode+", scheduleDate="+scheduleDate+", workClassCode="+shift+", display="+display);
		cstmt.close();
		con.close();

		return display;
	}

	public String isExJob(String organizationId,String jobId){
		return null;
	}
	@Override
	public String[] handworkDeclareJob(String organizationId,String jobId,String wipEntityId,String inventoryItemId,String scheduleDate,String workGroup,String workMonitor,String workClassCode)  throws SQLException{
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call CUX_NIWIP_HANDWORK_PKG.HANDWORK_DECLARE_JOB_IFACE(?,?,?,?,?,?,?,?,?,?)}");

		cstmt.setString(1, organizationId);
		cstmt.setString(2, jobId);
		cstmt.setString(3, wipEntityId);
		cstmt.setString(4, inventoryItemId);
		cstmt.setString(5, scheduleDate);
		cstmt.setString(6, workGroup);
		cstmt.setString(7, workMonitor);
		cstmt.setString(8, workClassCode);
		cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);

		cstmt.execute();
		String codes[] = new String[2];
		codes[0] = CommonUtil.noNullString(cstmt.getString(9));
		codes[1] = CommonUtil.noNullString(cstmt.getString(10));
		LogUtil.log("handworkDeclareJob():organizationId="+organizationId+", scheduleDate="+scheduleDate+", workMonitor="+workMonitor+", codes="+codes);
		cstmt.close();
		con.close();

		return codes;
	}

	/*
	 *
	 * 女工获取工序
	 * */
	@Override
	public List<RoutingInfoItem> getFemaleRouting() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT N2.LOOKUP_DETAIL_CODE, " +
				"       N2.LOOKUP_DETAIL_MEANING, " +
				"       N1.LOOKUP_MASTER_MEANING  " +
				"FROM   NI_LOOKUP N2, " +
				"       NI_LOOKUP N1 " +
				"WHERE  N2.LOOKUP_TYPE_CODE = N1.LOOKUP_TYPE_CODE " +
				"       AND N2.LOOKUP_MASTER_CODE = N1.LOOKUP_MASTER_CODE " +
				"       AND N2.LOOKUP_LEVEL = 2 " +
				"       AND N2.ENABLED_FLAG = 'Y' " +
				"       AND N1.LOOKUP_TYPE_CODE = 'NI_HAND_KIND_OPT' " +
				"       AND N1.LOOKUP_LEVEL = 1 " +
				"       AND N1.ENABLED_FLAG = 'Y'";
		LogUtil.log("getFemaleRouting():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<RoutingInfoItem>>() {

			public List<RoutingInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<RoutingInfoItem> tempList = new ArrayList<RoutingInfoItem>();

				while(rs.next()) {
					RoutingInfoItem data = new RoutingInfoItem();
					data.routingCode = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_CODE"));
					data.routingName = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_MEANING"));
					data.routingType = CommonUtil.noNullString(rs.getString("LOOKUP_MASTER_MEANING"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	/*
	 *
	 * 出勤状况
	 * */
	@Override
	public String[] attendanceFinish(AttendanceReq attendanceReq) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call CUX_NIWIP_HANDWORK_PKG.HANDWORK_DECLARE_DUTY_IFACE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

		cstmt.setString(1, attendanceReq.organizationId);
		cstmt.setString(2, attendanceReq.workGroup);
		cstmt.setString(3, attendanceReq.workMonitor);
		cstmt.setString(4, attendanceReq.workClassCode);
		cstmt.setString(5, attendanceReq.scheduleDate);
		cstmt.setString(6, attendanceReq.forecastWorkMan);
		cstmt.setString(7, attendanceReq.factWorkMan);
		cstmt.setString(8, attendanceReq.leaveQuantity);
		cstmt.setString(9, attendanceReq.absentQuantity);
		cstmt.setString(10, attendanceReq.yearOfRestDay);
		cstmt.setString(11, attendanceReq.turnOfRestDay);
		cstmt.setString(12, attendanceReq.autoAemissionQuantity);
		cstmt.setString(13, attendanceReq.fireQuantity);
		cstmt.setString(14, attendanceReq.outQuantity);
		cstmt.setString(15, attendanceReq.newInQuantity);
		cstmt.setString(16, attendanceReq.jobTime);
		cstmt.setString(17, attendanceReq.jobOvertime);
		cstmt.setString(18, attendanceReq.nonjobTime);
		cstmt.setString(19, attendanceReq.nonjobOvertime);
		cstmt.setString(20, attendanceReq.remark);
		cstmt.registerOutParameter(21, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(22, java.sql.Types.VARCHAR);

		cstmt.execute();
		String codes[] = new String[2];
		codes[0] = CommonUtil.noNullString(cstmt.getString(21));
		codes[1] = CommonUtil.noNullString(cstmt.getString(22));
		LogUtil.log("attendanceFinish():organizationId="+attendanceReq.organizationId+", workGroup="+ attendanceReq.workGroup+", workMonitor="+attendanceReq.workMonitor+", codes="+codes);
		cstmt.close();
		con.close();

		return codes;
	}
	/*
	 *
	 * 生产状况
	 * */
	@Override
	public String[] declareTimeFinish(DeclareTimeReq declareTimeReq) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call CUX_NIWIP_HANDWORK_PKG.HANDWORK_DECLARE_TIME_IFACE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

		cstmt.setString(1, declareTimeReq.organizationId);
		cstmt.setString(2, declareTimeReq.jobTransactionId);
		cstmt.setString(3, declareTimeReq.operationSeqNum);
		cstmt.setString(4, declareTimeReq.niOperationCode);
		cstmt.setString(5, declareTimeReq.niOperationDesc);
		cstmt.setString(6, declareTimeReq.assetCode);
		cstmt.setString(7, declareTimeReq.quantity);
		cstmt.setString(8, declareTimeReq.startPullTime);
		cstmt.setString(9, declareTimeReq.endPullTime);
		cstmt.setString(10, declareTimeReq.workTime);
		cstmt.setString(11, declareTimeReq.transactionUom);
		cstmt.setString(12, declareTimeReq.perQuantity);
		cstmt.setString(13, declareTimeReq.subgoodsQuantity);
		cstmt.setString(14, declareTimeReq.goodsQuantity);
		cstmt.setString(15, declareTimeReq.goodsWasteQuantity);
		cstmt.setString(16, declareTimeReq.returnWasteQuantity);
		cstmt.setString(17, declareTimeReq.inputQuantity);
		cstmt.setString(18, declareTimeReq.wasteInputQuantity);
		cstmt.setString(19, declareTimeReq.reasonCode);
		cstmt.setString(20, declareTimeReq.reasonRemark);
		cstmt.registerOutParameter(21, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(22, java.sql.Types.VARCHAR);

		cstmt.execute();
		String codes[] = new String[2];
		codes[0] = CommonUtil.noNullString(cstmt.getString(21));
		codes[1] = CommonUtil.noNullString(cstmt.getString(22));
		LogUtil.log("declareTimeFinish():organizationId="+declareTimeReq.organizationId+", jobTransactionId="+declareTimeReq.jobTransactionId+", operationSeqNum="+declareTimeReq.operationSeqNum+", codes="+codes);
		cstmt.close();
		con.close();

		return codes;
	}
	/*
	 *
	 * 非生产状况
	 * */
	@Override
	public String[] nonDeclareTimeFinish(NonDeclareTimeReq nonDeclareTimeReq) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call CUX_NIWIP_HANDWORK_PKG.HANDWORK_DECLARE_TIME_IFACE2(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

		cstmt.setString(1, nonDeclareTimeReq.organizationId);
		cstmt.setString(2, nonDeclareTimeReq.workGroup);
		cstmt.setString(3, nonDeclareTimeReq.groupMaster);
		cstmt.setString(4, nonDeclareTimeReq.dayOrNight);
		cstmt.setString(5, nonDeclareTimeReq.scheduleDate);
		cstmt.setString(6, nonDeclareTimeReq.jobId);
		cstmt.setString(7, nonDeclareTimeReq.wipEntityId);
		cstmt.setString(8, nonDeclareTimeReq.inventoryItemId);
		cstmt.setString(9, nonDeclareTimeReq.quantity);
		cstmt.setString(10, nonDeclareTimeReq.workTime);
		cstmt.setString(11, nonDeclareTimeReq.reasonCode);
		cstmt.setString(12, nonDeclareTimeReq.reasonRemark);
		cstmt.setString(13, nonDeclareTimeReq.goodsQuantity);
		cstmt.setString(14, nonDeclareTimeReq.goodsWasteQuantity);

		cstmt.registerOutParameter(15, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(16, java.sql.Types.VARCHAR);

		cstmt.execute();
		String codes[] = new String[2];
		codes[0] = CommonUtil.noNullString(cstmt.getString(15));
		codes[1] = CommonUtil.noNullString(cstmt.getString(16));
		LogUtil.log("nonDeclareTimeFinish():organizationId="+ nonDeclareTimeReq.organizationId+", wipEntityId="+ nonDeclareTimeReq.wipEntityId+", inventoryItemId="+ nonDeclareTimeReq.inventoryItemId+", codes="+codes);
		cstmt.close();
		con.close();

		return codes;
	}

	@Override
	public List<ReasonCodeInfoItem> getNonReasonCodeList() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT LOOKUP_MASTER_CODE,LOOKUP_MASTER_MEANING  " +
				"FROM NI_LOOKUP " +
				"WHERE LOOKUP_TYPE_CODE = 'NI_HAND_KIND_REASON' " +
				"AND LOOKUP_LEVEL = 1 " +
				"AND ENABLED_FLAG = 'Y' ";
		LogUtil.log("getNonReasonCodeList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<ReasonCodeInfoItem>>() {

			public List<ReasonCodeInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<ReasonCodeInfoItem> tempList = new ArrayList<ReasonCodeInfoItem>();

				while(rs.next()) {
					ReasonCodeInfoItem data = new ReasonCodeInfoItem();
					data.reasonCode = CommonUtil.noNullString(rs.getString("LOOKUP_MASTER_CODE"));
					data.reasonName = CommonUtil.noNullString(rs.getString("LOOKUP_MASTER_MEANING"));
					data.reasonDesc = CommonUtil.noNullString(rs.getString("LOOKUP_MASTER_MEANING"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	@Override
	public List<MtlPlanInfoItem> getMtlPlanList(String wipEntityId, String operationSeqNum, String organizationId) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT WIP_ENTITY_ID," +
				"       OPERATION_SEQ_NUM, " +
				"       ORGANIZATION_ID," +
				"       INVENTORY_ITEM_ID," +
				"       CONCATENATED_SEGMENTS," +
				"       ITEM_DESCRIPTION, " +
				"       REQUIRED_QUANTITY, " +
				"       ITEM_PRIMARY_UOM_CODE " +
				"FROM   WIP_REQUIREMENT_OPERATIONS_V " +
				"WHERE  WIP_ENTITY_ID = " + wipEntityId +
				"       AND OPERATION_SEQ_NUM = " + operationSeqNum +
				"       AND ORGANIZATION_ID = " +organizationId;
		LogUtil.log("getMtlPlanList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<MtlPlanInfoItem>>() {

			public List<MtlPlanInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<MtlPlanInfoItem> tempList = new ArrayList<MtlPlanInfoItem>();

				while(rs.next()) {
					MtlPlanInfoItem data = new MtlPlanInfoItem();
					data.wipEntityId = CommonUtil.noNullString(rs.getString("WIP_ENTITY_ID"));
					data.operationSeqNum = CommonUtil.noNullString(rs.getString("OPERATION_SEQ_NUM"));
					data.organizationId = CommonUtil.noNullString(rs.getString("ORGANIZATION_ID"));
					data.inventoryItemId = CommonUtil.noNullString(rs.getString("INVENTORY_ITEM_ID"));
					data.concatenatedSegments = CommonUtil.noNullString(rs.getString("CONCATENATED_SEGMENTS"));
					data.itemDescription = CommonUtil.noNullString(rs.getString("ITEM_DESCRIPTION"));
					data.requiredQuantity = CommonUtil.noNullString(rs.getString("REQUIRED_QUANTITY"));
					data.itemPrimaryUomCode = CommonUtil.noNullString(rs.getString("ITEM_PRIMARY_UOM_CODE"));
					getMtlPlanInfoOther(data);
					tempList.add(data);
				}
				return tempList;
			}
		});
	}
	public void getMtlPlanInfoOther(MtlPlanInfoItem data) throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{CALL NI_LOCALE_DECLARE_INTERFACE.RETRIEVE_WIP_MATERIAL_INFO(?,?,?,?,?,?,?)}");

		cstmt.setString(1, data.wipEntityId);
		cstmt.setString(2, data.operationSeqNum);
		cstmt.setString(3, data.inventoryItemId);
		cstmt.setString(4, data.organizationId);
		cstmt.setString(5, "DGWP01");

		cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(7, java.sql.Types.VARCHAR);
		cstmt.execute();
		data.issued = CommonUtil.noNullString(cstmt.getString(6));
		data.available = CommonUtil.noNullString(cstmt.getString(7));
		LogUtil.log("getMtlPlanInfoOther():organizationId="+data.organizationId+", wipEntityId="+data.wipEntityId+", inventoryItemId="+data.inventoryItemId+", issued="+data.issued+", available="+data.available);
		cstmt.close();
		con.close();
	}
	@Override
	public List<MtlPlanInfoItem> getMtlNewList(final String wipEntityId, String mtlNo, final String organizationId) throws SQLException  {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT MSI.ORGANIZATION_ID,  " +
				"       MSI.INVENTORY_ITEM_ID,  " +
				"       MSI.SEGMENT2 || '-' || MSI.SEGMENT3 || '-' || MSI.SEGMENT4 CONCATENATED_SEGMENTS,  " +
				"       MSI.DESCRIPTION,  " +
				"       MSI.PRIMARY_UOM_CODE  " +
				"  FROM MTL_SYSTEM_ITEMS_B MSI " +
				" WHERE MSI.ORGANIZATION_ID = " + organizationId +
				"   AND (MSI.SEGMENT2 || '-' || MSI.SEGMENT3 || '-' || MSI.SEGMENT4) LIKE " +
				"       '%"+mtlNo+"%' ";
		LogUtil.log("getMtlPlanList():sql="+sql);
		System.out.println(sql);
		return runner.query(sql, new ResultSetHandler<List<MtlPlanInfoItem>>() {

			public List<MtlPlanInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<MtlPlanInfoItem> tempList = new ArrayList<MtlPlanInfoItem>();

				while(rs.next()) {
					MtlPlanInfoItem data = new MtlPlanInfoItem();
					data.wipEntityId = wipEntityId;
					data.operationSeqNum = "";
					data.organizationId = organizationId;
					data.inventoryItemId = CommonUtil.noNullString(rs.getString("INVENTORY_ITEM_ID"));
					data.concatenatedSegments = CommonUtil.noNullString(rs.getString("CONCATENATED_SEGMENTS"));
					data.itemDescription = CommonUtil.noNullString(rs.getString("DESCRIPTION"));
					data.itemPrimaryUomCode = CommonUtil.noNullString(rs.getString("PRIMARY_UOM_CODE"));
					getMtlNewInfoOther(data);
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	public void getMtlNewInfoOther(MtlPlanInfoItem data) throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{CALL CUX_NIWIP_HANDWORK_PKG.GET_ITEM_QTY_INFO(?,?,?,?,?,?)}");

		cstmt.setString(1, data.organizationId);
		cstmt.setString(2, data.wipEntityId);
		cstmt.setString(3, data.inventoryItemId);

		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
		cstmt.execute();
		data.requiredQuantity = CommonUtil.noNullString(cstmt.getString(4));
		data.issued = CommonUtil.noNullString(cstmt.getString(5));
		data.available = CommonUtil.noNullString(cstmt.getString(6));

		LogUtil.log("getMtlPlanInfoOther():organizationId="+data.organizationId+", wipEntityId="+data.wipEntityId+", inventoryItemId="+data.inventoryItemId+", issued="+data.issued+", available="+data.available);
		cstmt.close();
		con.close();
	}
    @Override
    public String[] declareMtlFinish(DeclareMtlReq declareMtlReq) throws SQLException{

        Connection con = this.dataSource.getConnection();
        CallableStatement cstmt = con.prepareCall("{call CUX_NIWIP_HANDWORK_PKG.HANDWORK_DECLARE_MTL_IFACE(?,?,?,?,?,?,?,?,?,?,?)}");

        cstmt.setString(1, declareMtlReq.organizationId);
        cstmt.setString(2, declareMtlReq.jobTransactionId);
        cstmt.setString(3, declareMtlReq.moveTransactionId);
        cstmt.setString(4, declareMtlReq.inventoryItemId);
        cstmt.setString(5, declareMtlReq.transactionUom);
        cstmt.setString(6, declareMtlReq.transactionQuantity);
        cstmt.setString(7, declareMtlReq.remark);
        cstmt.setString(8, declareMtlReq.transactionItemType);
        cstmt.setString(9, declareMtlReq.transactionType);
        cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);
        cstmt.registerOutParameter(11, java.sql.Types.VARCHAR);

        cstmt.execute();
        String codes[] = new String[2];
        codes[0] = CommonUtil.noNullString(cstmt.getString(10));
        codes[1] = CommonUtil.noNullString(cstmt.getString(11));
        LogUtil.log("declareMtlFinish():organizationId="+ declareMtlReq.organizationId+", jobTransactionId="+ declareMtlReq.jobTransactionId+", inventoryItemId="+ declareMtlReq.inventoryItemId+", codes="+codes);
        cstmt.close();
        con.close();

        return codes;
    }

	@Override
	public List<DeclareTimeInfoItem> getDeclareTimeList(DeclareTimeListReq declareTimeListReq)throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT NHDT.ORGANIZATION_ID, " +
				"       NHDT.JOB_TRANSACTION_ID, " +
				"       NHDT.MOVE_TRANSACTION_ID, " +
				"       NHDT.JOB_ID, " +
				"       NHDT.JOB_NO, " +
				"       NHDT.WIP_ENTITY_ID, " +
				"       NHDT.WIP_ENTITY_NAME, " +
				"       NHDT.INVENTORY_ITEM_ID, " +
				"       NHDT.SCHEDULE_TRANSACTION_ID, " +
				"       NHDT.OPERATION_CODE, " +
				"       NHDT.OPERATION_DESC, " +
				"       NHDT.OPERATION_SEQ_NUM, " +
				"       NHDT.STD_OPERATION_CODE, " +
				"       NHDT.STD_OPERATION_DESC, " +
				"       NHDT.WORK_MAN_QUANTITY, " +
				"       NHDT.START_PULL_TIME, " +
				"       NHDT.END_PULL_TIME, " +
				"       NHDT.WORK_TIME, " +
				"       NHDT.TRANSACTION_UOM, " +
				"       NHDT.PER_QUANTITY, " +
				"       NHDT.SUBGOODS_QUANTITY, " +
				"       NHDT.GOODS_QUANTITY, " +
				"       NHDT.GOODS_WASTE_QUANTITY, " +
				"       NHDT.RETURN_WASTE_QUANTITY, " +
				"       NHDT.INPUT_QUANTITY, " +
				"       NHDT.WASTE_INPUT_QUANTITY, " +
				"       NHDT.REASON_CODE, " +
				"       NHDT.REASON_REMARK  " +
				"  FROM NIWIP_HANDWORK_DECLARE_JOB   NHDJ," +
				"       NIWIP_HANDWORK_DECLARE_CLASS NHDC," +
				"       NIWIP_HANDWORK_DECLARE_TIME  NHDT " +
				" WHERE NHDJ.ORGANIZATION_ID = NHDC.ORGANIZATION_ID " +
				"   AND NHDJ.SCHEDULE_TRANSACTION_ID = NHDC.SCHEDULE_TRANSACTION_ID " +
				"   AND NHDT.ORGANIZATION_ID = NHDJ.ORGANIZATION_ID " +
				"   AND NHDT.SCHEDULE_TRANSACTION_ID = NHDJ.SCHEDULE_TRANSACTION_ID " +
				"   AND NHDT.JOB_TRANSACTION_ID = NHDJ.JOB_TRANSACTION_ID " +
				"   AND NHDT.JOB_TIME_FLAG = 'Y' " +
				"   AND NHDJ.ORGANIZATION_ID = " + declareTimeListReq.organizationId+
				"   AND NHDJ.JOB_ID =  " + declareTimeListReq.jobId+
				"   AND NHDJ.WIP_ENTITY_ID = " + declareTimeListReq.wipEntityId+
				"   AND NHDJ.INVENTORY_ITEM_ID =  " + declareTimeListReq.inventoryItemId+
				"   AND NHDC.SCHEDULE_DATE =  TO_DATE('" + declareTimeListReq.scheduleDate+"','yyyy/mm/dd')"+
				"   AND NHDC.WORK_GROUP =  '" + declareTimeListReq.workGroup+ "'" +
				"   AND NHDC.WORK_MONITOR =  '" + declareTimeListReq.workMonitor+ "'" +
				"   AND NHDC.WORK_CLASS_CODE =  '" + declareTimeListReq.workClassCode + "'" ;
		LogUtil.log("getDeclareTimeList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<DeclareTimeInfoItem>>() {

			public List<DeclareTimeInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<DeclareTimeInfoItem> tempList = new ArrayList<DeclareTimeInfoItem>();

				while(rs.next()) {
					DeclareTimeInfoItem data = new DeclareTimeInfoItem();
					data.organizationId = CommonUtil.noNullString(rs.getString("ORGANIZATION_ID"));
					data.jobTransactionId = CommonUtil.noNullString(rs.getString("JOB_TRANSACTION_ID"));
					data.moveTransactionId = CommonUtil.noNullString(rs.getString("MOVE_TRANSACTION_ID"));
					data.operationDesc = CommonUtil.noNullString(rs.getString("STD_OPERATION_DESC"));
					data.operationSeqNum = CommonUtil.noNullString(rs.getString("OPERATION_SEQ_NUM"));
					data.niOperationCode = CommonUtil.noNullString(rs.getString("OPERATION_CODE"));
					data.niOperationDesc = CommonUtil.noNullString(rs.getString("OPERATION_DESC"));
					data.quantity = CommonUtil.noNullString(rs.getString("WORK_MAN_QUANTITY"));
					data.startPullTime = CommonUtil.noNullString(rs.getString("START_PULL_TIME"));
					data.endPullTime = CommonUtil.noNullString(rs.getString("END_PULL_TIME"));
					data.workTime = CommonUtil.noNullString(rs.getString("WORK_TIME"));
					data.transactionUom = CommonUtil.noNullString(rs.getString("TRANSACTION_UOM"));
					data.perQuantity = CommonUtil.noNullString(rs.getString("PER_QUANTITY"));
					data.subgoodsQuantity = CommonUtil.noNullString(rs.getString("SUBGOODS_QUANTITY"));
					data.goodsQuantity = CommonUtil.noNullString(rs.getString("GOODS_QUANTITY"));
					data.goodsWasteQuantity = CommonUtil.noNullString(rs.getString("GOODS_WASTE_QUANTITY"));
					data.returnWasteQuantity = CommonUtil.noNullString(rs.getString("RETURN_WASTE_QUANTITY"));
					data.inputQuantity = CommonUtil.noNullString(rs.getString("INPUT_QUANTITY"));
					data.wasteInputQuantity = CommonUtil.noNullString(rs.getString("WASTE_INPUT_QUANTITY"));
					data.reasonCode = CommonUtil.noNullString(rs.getString("REASON_CODE"));
					data.reasonRemark = CommonUtil.noNullString(rs.getString("REASON_REMARK"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	@Override
	public List<NonDeclareTimeInfoItem> getNonDeclareTimeList(NonDeclareTimeListReq nonDeclareTimeListReq) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT NHDT.ORGANIZATION_ID,  " +
				"       NHDT.JOB_TRANSACTION_ID,  " +
				"       NHDT.MOVE_TRANSACTION_ID,  " +
				"       NHDT.JOB_ID,  " +
				"       NHDT.JOB_NO,  " +
				"       NHDT.WIP_ENTITY_ID,  " +
				"       NHDT.WIP_ENTITY_NAME,  " +
				"       NHDT.INVENTORY_ITEM_ID,  " +
				"       NHDT.SCHEDULE_TRANSACTION_ID,  " +
				"       NHDT.WORK_MAN_QUANTITY,  " +
				"       NHDT.WORK_TIME,  " +
				"       NHDT.GOODS_QUANTITY,  " +
				"       NHDT.GOODS_WASTE_QUANTITY,  " +
				"       NHDT.REASON_CODE,  " +
				"       NHDT.REASON_REMARK,  " +
				"       NHDT.JOB_TIME_FLAG   " +
				"  FROM   " +
				"       NIWIP_HANDWORK_DECLARE_CLASS NHDC,  " +
				"       NIWIP_HANDWORK_DECLARE_TIME  NHDT  " +
				" WHERE NHDT.ORGANIZATION_ID = NHDC.ORGANIZATION_ID  " +
				"   AND NHDT.SCHEDULE_TRANSACTION_ID = NHDC.SCHEDULE_TRANSACTION_ID  " +
				"   AND NHDT.ORGANIZATION_ID = NHDC.ORGANIZATION_ID  " +
				"   AND NHDT.JOB_TIME_FLAG = 'N'  " +
				"   AND NHDT.ORGANIZATION_ID = '" + nonDeclareTimeListReq.organizationId +"'"+
				/*"   AND NHDT.JOB_ID = '" + nonDeclareTimeListReq.jobId +"'"+
				"   AND NHDT.WIP_ENTITY_ID = '" + nonDeclareTimeListReq.wipEntityId +"'"+
				"   AND NHDT.INVENTORY_ITEM_ID = '" + nonDeclareTimeListReq.inventoryItemId +"'"+*/
				"   AND NHDC.SCHEDULE_DATE = TO_DATE('"+ nonDeclareTimeListReq.scheduleDate +"', 'yyyy/mm/dd') "+
				"   AND NHDC.WORK_GROUP = '" + nonDeclareTimeListReq.workGroup +"'"+
				"   AND NHDC.WORK_MONITOR = '" + nonDeclareTimeListReq.workMonitor +"'"+
				"   AND NHDC.WORK_CLASS_CODE = '" + nonDeclareTimeListReq.workClassCode +"'";
		LogUtil.log("getNonDeclareTimeList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<NonDeclareTimeInfoItem>>() {

			public List<NonDeclareTimeInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<NonDeclareTimeInfoItem> tempList = new ArrayList<NonDeclareTimeInfoItem>();

				while(rs.next()) {
					NonDeclareTimeInfoItem data = new NonDeclareTimeInfoItem();
					data.organizationId = CommonUtil.noNullString(rs.getString("ORGANIZATION_ID"));
					data.jobTransactionId = CommonUtil.noNullString(rs.getString("JOB_TRANSACTION_ID"));
					data.moveTransactionId = CommonUtil.noNullString(rs.getString("MOVE_TRANSACTION_ID"));
					data.jobId = CommonUtil.noNullString(rs.getString("JOB_ID"));
					data.jobNo = CommonUtil.noNullString(rs.getString("JOB_NO"));
					data.wipEntityId = CommonUtil.noNullString(rs.getString("WIP_ENTITY_ID"));
					data.wipEntityName = CommonUtil.noNullString(rs.getString("WIP_ENTITY_NAME"));
					data.inventoryItemId = CommonUtil.noNullString(rs.getString("INVENTORY_ITEM_ID"));
					data.scheduleTransactionId = CommonUtil.noNullString(rs.getString("SCHEDULE_TRANSACTION_ID"));
					data.quantity = CommonUtil.noNullString(rs.getString("WORK_MAN_QUANTITY"));
					data.workTime = CommonUtil.noNullString(rs.getString("WORK_TIME"));
					data.goodsQuantity = CommonUtil.noNullString(rs.getString("GOODS_QUANTITY"));
					data.goodsWasteQuantity = CommonUtil.noNullString(rs.getString("GOODS_WASTE_QUANTITY"));
					data.reasonCode = CommonUtil.noNullString(rs.getString("REASON_CODE"));
					data.reasonRemark = CommonUtil.noNullString(rs.getString("REASON_REMARK"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	@Override
	public List<DeclareMtlInfoItem> getDeclareMtlList(DeclareMtlListReq declareMtlListReq) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT NHDM.MTL_TRANSACTION_ID, " +
				"       NHDM.SCHEDULE_TRANSACTION_ID, " +
				"       NHDM.INVENTORY_ITEM_ID, " +
				"       MSI.SEGMENT2 || '-' || MSI.SEGMENT3 || '-' || MSI.SEGMENT4 CONCATENATED_SEGMENTS, " +
				"       MSI.DESCRIPTION, " +
				"       NHDM.TRANSACTION_QUANTITY, " +
				"       NHDM.TRANSACTION_UOM, " +
				"       NHDM.TRANSACTION_ITEM_TYPE, " +
				"       NHDM.REMARK, " +
				"		 NHDM.TRANSACTION_TYPE "+
				"  FROM NIWIP_HANDWORK_DECLARE_MTL NHDM, MTL_SYSTEM_ITEMS_B MSI " +
				" WHERE NHDM.ORGANIZATION_ID = MSI.ORGANIZATION_ID " +
				"   AND NHDM.INVENTORY_ITEM_ID = MSI.INVENTORY_ITEM_ID " +
				"   AND NHDM.ORGANIZATION_ID = '" + declareMtlListReq.organizationId +"'"+
				"   AND NHDM.JOB_TRANSACTION_ID = '" + declareMtlListReq.jobTransactionId +"'"+
				"   AND NHDM.MOVE_TRANSACTION_ID = '" + declareMtlListReq.moveTransactionId +"'";
		LogUtil.log("getNonDeclareTimeList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<DeclareMtlInfoItem>>() {

			public List<DeclareMtlInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<DeclareMtlInfoItem> tempList = new ArrayList<DeclareMtlInfoItem>();

				while(rs.next()) {
					DeclareMtlInfoItem data = new DeclareMtlInfoItem();
					data.mtlTransactionId = CommonUtil.noNullString(rs.getString("MTL_TRANSACTION_ID"));
					data.scheduleTransactionId = CommonUtil.noNullString(rs.getString("SCHEDULE_TRANSACTION_ID"));
					data.inventoryItemId = CommonUtil.noNullString(rs.getString("INVENTORY_ITEM_ID"));
					data.concatenatedSegments = CommonUtil.noNullString(rs.getString("CONCATENATED_SEGMENTS"));
					data.description = CommonUtil.noNullString(rs.getString("DESCRIPTION"));
					data.transactionQuantity = CommonUtil.noNullString(rs.getString("TRANSACTION_QUANTITY"));
					data.transactionUom = CommonUtil.noNullString(rs.getString("TRANSACTION_UOM"));
					data.transactionItemType = CommonUtil.noNullString(rs.getString("TRANSACTION_ITEM_TYPE"));
					data.transactionType = CommonUtil.noNullString(rs.getString("TRANSACTION_TYPE"));
					data.remark = CommonUtil.noNullString(rs.getString("REMARK"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}

	@Override
	public List<AttendanceInfoItem> getAttendanceList(AttendanceListReq attendanceListReq) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT NHDD.FORECAST_WORK_MAN, " +
				"       NHDD.FACT_WORK_MAN, " +
				"       NHDD.LEAVE_QUANTITY, " +
				"       NHDD.ABSENT_QUANTITY, " +
				"       NHDD.YEAR_OF_REST_DAY, " +
				"       NHDD.TURN_OF_REST_DAY, " +
				"       NHDD.AUTO_DEMISSION_QUANTITY, " +
				"       NHDD.FIRE_QUANTITY, " +
				"       NHDD.OUT_QUANTITY, " +
				"       NHDD.NEW_IN_QUANTITY, " +
				"       NHDD.JOB_TIME, " +
				"       NHDD.JOB_OVERTIME, " +
				"       NHDD.NONJOB_TIME, " +
				"       NHDD.NONJOB_OVERTIME, " +
				"       NHDD.REMARK " +
				"  FROM NIWIP_HANDWORK_DECLARE_DUTY NHDD, NIWIP_HANDWORK_DECLARE_CLASS NHDC " +
				" WHERE NHDD.SCHEDULE_TRANSACTION_ID = NHDC.SCHEDULE_TRANSACTION_ID " +
				"   AND NHDD.ORGANIZATION_ID = NHDC.ORGANIZATION_ID " +
				"   AND NHDD.ORGANIZATION_ID = '" + attendanceListReq.organizationId +"'"+
				"   AND NHDC.SCHEDULE_DATE = TO_DATE('"+ attendanceListReq.scheduleDate +"', 'yyyy/mm/dd') " +
				"   AND NHDC.WORK_GROUP = '" + attendanceListReq.workGroup +"'"+
				"   AND NHDC.WORK_MONITOR = '" + attendanceListReq.workMonitor +"'"+
				"   AND NHDC.WORK_CLASS_CODE = '" + attendanceListReq.workClassCode +"'";
		LogUtil.log("getAttendanceList():sql="+sql);
		return runner.query(sql, new ResultSetHandler<List<AttendanceInfoItem>>() {

			public List<AttendanceInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<AttendanceInfoItem> tempList = new ArrayList<AttendanceInfoItem>();

				while(rs.next()) {
					AttendanceInfoItem data = new AttendanceInfoItem();
					data.forecastWorkMan = CommonUtil.noNullString(rs.getString("FORECAST_WORK_MAN"));
					data.factWorkMan = CommonUtil.noNullString(rs.getString("FACT_WORK_MAN"));
					data.leaveQuantity = CommonUtil.noNullString(rs.getString("LEAVE_QUANTITY"));
					data.absentQuantity = CommonUtil.noNullString(rs.getString("ABSENT_QUANTITY"));
					data.yearOfRestDay = CommonUtil.noNullString(rs.getString("YEAR_OF_REST_DAY"));
					data.turnOfRestDay = CommonUtil.noNullString(rs.getString("TURN_OF_REST_DAY"));
					data.autoAemissionQuantity = CommonUtil.noNullString(rs.getString("AUTO_DEMISSION_QUANTITY"));
					data.fireQuantity = CommonUtil.noNullString(rs.getString("FIRE_QUANTITY"));
					data.outQuantity = CommonUtil.noNullString(rs.getString("OUT_QUANTITY"));
					data.newInQuantity = CommonUtil.noNullString(rs.getString("NEW_IN_QUANTITY"));
					data.jobTime = CommonUtil.noNullString(rs.getString("JOB_TIME"));
					data.jobOvertime = CommonUtil.noNullString(rs.getString("JOB_OVERTIME"));
					data.nonjobTime = CommonUtil.noNullString(rs.getString("NONJOB_TIME"));
					data.nonjobOvertime = CommonUtil.noNullString(rs.getString("NONJOB_OVERTIME"));
					data.remark = CommonUtil.noNullString(rs.getString("REMARK"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}




	@Override
	public List<ReasonCodeInfoItem> getReasonCodeList() throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "SELECT N2.LOOKUP_DETAIL_CODE,  " +
				"       N2.LOOKUP_DETAIL_MEANING, " +
				"       N1.LOOKUP_MASTER_MEANING,  " +
				"       N2.LOOKUP_DETAIL_MEANING LOOKUP_DETAIL_MEANING2  " +
				"  FROM NI_LOOKUP N2, NI_LOOKUP N1  " +
				" WHERE N2.LOOKUP_TYPE_CODE = N1.LOOKUP_TYPE_CODE  " +
				"   AND N2.LOOKUP_MASTER_CODE = N1.LOOKUP_MASTER_CODE  " +
				"   AND N2.LOOKUP_LEVEL = 2  " +
				"   AND N2.ENABLED_FLAG = 'Y'  " +
				"   AND N1.LOOKUP_TYPE_CODE = 'NI_HAND_WASTE_REASON'  " +
				"   AND N1.LOOKUP_LEVEL = 1 " +
				"   AND N1.ENABLED_FLAG = 'Y'";
		LogUtil.log("getReasonCodeList():sql="+sql);

		return runner.query(sql, new ResultSetHandler<List<ReasonCodeInfoItem>>() {

			public List<ReasonCodeInfoItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}
				List<ReasonCodeInfoItem> tempList = new ArrayList<ReasonCodeInfoItem>();

				while(rs.next()) {
					ReasonCodeInfoItem data = new ReasonCodeInfoItem();
					data.reasonCode = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_CODE"));
					data.reasonName = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_MEANING"));
					data.reasonDesc = CommonUtil.noNullString(rs.getString("LOOKUP_DETAIL_MEANING2"));
					tempList.add(data);
				}
				return tempList;
			}
		});
	}



	@Override
	public List<DataProcessItem> getProcessList4F(final MainProcessReq mainProcessReq) throws SQLException {
		String sql = "SELECT V.JOB_ID,  " +
				"       V.JOB_NO,   " +
				"       V.VER_NO,   " +
				"       NJW.DESCRIPTION JOB_DESC,   " +
				"       V.WIP_ENTITY_NAME,   " +
				"       V.WIP_ENTITY_ID,   " +
				"       V.CLASS_CODE ATTRIBUTE6,   " +
				"       V.INVENTORY_ITEM_ID,    " +
				"       V.ITEM_NUMBER,  " +
				"       V.ITEM_DESC,   " +
				"       MSI.PRIMARY_UOM_CODE,   " +
				"       V.SCHEDULE_DATE,   " +
				"       DECODE(V.RATE,  " +
				"              0,  " +
				"              NULL,  " +
				"              CEIL(SUM(V.SCH_WORKER_QTY) * 10 * 3600 / V.RATE)) SCHEDULE_QUANTITY,  " +
				"       SUM(V.SCH_WORKER_QTY) * 10 TIME_NEED,   " +
				"       V.OPERATION_CODE,  " +
				"       V.OPERATION_SEQ_NUM,  " +
				"       V.OPERATION_DESC  " +
				"  FROM NI_HAND_SCH_MES_V V, NI_JOB_WIPS NJW,  " +
				"  MTL_SYSTEM_ITEMS_B MSI  " +
				" WHERE V.PRODUCT_PLACE IN  " +
				"       (SELECT DECODE(LOOKUP_MASTER_MEANING,  " +
				"                      NULL,  " +
				"                      LOOKUP_MASTER_CODE,  " +
				"                      LOOKUP_MASTER_MEANING)  " +
				"          FROM NI_LOOKUP  " +
				"         WHERE LOOKUP_LEVEL = 1  " +
				"           AND LOOKUP_TYPE_CODE = 'NI MES Machines'  " +
				"           AND ENABLED_FLAG = 'Y'  " +
				"           AND LOOKUP_MASTER_CODE = '"+mainProcessReq.resCode+"'" +
				"           AND V.ORG_ID = '"+mainProcessReq.organizationId+"' ) " +
				"   AND V.SCHEDULE_DATE = TO_DATE('"+mainProcessReq.scheduleDate +"', 'yyyy/mm/dd') "+
				"   AND V.FINISH_FLAG = 'N'  " +
				"   AND V.ORG_ID = NJW.ORG_ID  " +
				"   AND V.JOB_ID = NJW.JOB_ID  " +
				"   AND V.WIP_ENTITY_ID = NJW.WIP_ENTITY_ID  " +
				"   AND V.VER_NO = NJW.VER_NO  " +
				"   AND V.ORG_ID = MSI.ORGANIZATION_ID  " +
				"   AND V.INVENTORY_ITEM_ID =  MSI.INVENTORY_ITEM_ID  " +
				" GROUP BY V.JOB_ID,  " +
				"          V.JOB_NO,  " +
				"          V.VER_NO,  " +
				"          NJW.DESCRIPTION,  " +
				"          V.WIP_ENTITY_NAME,  " +
				"          V.WIP_ENTITY_ID,  " +
				"          V.CLASS_CODE,  " +
				"          V.INVENTORY_ITEM_ID,  " +
				"          V.ITEM_NUMBER,  " +
				"          V.ITEM_DESC,  " +
				"          MSI.PRIMARY_UOM_CODE,   " +
				"          V.SCHEDULE_DATE,  " +
				"          V.RATE,  " +
				"          V.OPERATION_CODE,  " +
				"          V.OPERATION_SEQ_NUM,  " +
				"          V.OPERATION_DESC,  " +
				"          V.PMC_DATE  " +
				" ORDER BY V.CLASS_CODE, V.PMC_DATE, V.JOB_NO";
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<List<DataProcessItem>>() {

			public List<DataProcessItem> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
					return null;
				}

				List<DataProcessItem> tempList = new ArrayList<DataProcessItem>();

				while(rs.next()) {
					DataProcessItem data = new DataProcessItem();
					data.jobId =  rs.getString("JOB_ID");
					data.productId =  rs.getString("INVENTORY_ITEM_ID");
					data.primaryUomCode =  rs.getString("PRIMARY_UOM_CODE");
					data.scheduleDate =  rs.getString("SCHEDULE_DATE");
					data.projectNum = rs.getString("JOB_NO");
					data.processNum =  rs.getString("WIP_ENTITY_NAME");
					data.publishCode =  rs.getString("VER_NO");
					data.jobDesc =  rs.getString("JOB_DESC");
					data.productCode =  rs.getString("ITEM_NUMBER");
					data.productDesc =  rs.getString("ITEM_DESC");
					data.wipId =  rs.getString("WIP_ENTITY_ID");
					data.opCode =  rs.getString("OPERATION_CODE");
					data.seqNum =  rs.getString("OPERATION_SEQ_NUM");
					data.seqDesc =  rs.getString("OPERATION_DESC");
					data.planNum =  rs.getString("SCHEDULE_QUANTITY");
					data.timeNeeded =  rs.getString("TIME_NEED");
					setProcessOtherInfo(data,mainProcessReq);
					tempList.add(data);
				}

				LogUtil.log("getProcessList4F:tempList.size()="+tempList.size());

				return tempList;
			}
		});
	}
	private  void setProcessOtherInfo(DataProcessItem data, MainProcessReq mainProcessReq) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{CALL CUX_NIWIP_HANDWORK_PKG.GET_BUILD_JOB_LIST(?,?,?,?,?,?,?,?,?,?,?,?)}");

		cstmt.setString(1, mainProcessReq.organizationId);
		cstmt.setString(2, data.jobId);
		cstmt.setString(3, data.wipId);
		cstmt.setString(4, data.productId);
		cstmt.setString(5, mainProcessReq.scheduleDate);
		cstmt.setString(6, mainProcessReq.workGroup);
		cstmt.setString(7, mainProcessReq.workMonitor);
		cstmt.setString(8, mainProcessReq.workClassCode);
		cstmt.setString(9, data.seqNum);

		cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(11, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(12, java.sql.Types.VARCHAR);
		cstmt.execute();
		data.reportedQty = CommonUtil.noNullString(cstmt.getString(10)) + "/" + CommonUtil.noNullString(cstmt.getString(11));
		data.isReport = CommonUtil.noNullString(cstmt.getString(12));
		if (data.isReport.equals("1")){
			data.processNum += "    ***已报数***";
		}
		LogUtil.log("setProcessOtherInfo():organizationId="+mainProcessReq.organizationId+", wipEntityId="+data.wipId+", inventoryItemId="+data.productId+", reportedQty="+data.reportedQty+", isReport="+data.isReport);
		cstmt.close();
		con.close();

	}
	//开始某道工序之前，需要计算最大可投入数。由于存在未完成工序和工序未全数加工完是不允许跳工序，所以计算最大投入数简单了
	//canJump是判断主管操作时，允许跳工序的情况
	public int getLeftCountForStartOp(String wipName, int wipEntityId, int routing_sequence_id, String opCode, String curOperationId, boolean canJump) throws SQLException {
		
		int incompleteQuan = getWipIncompleteQuantityByWipId(wipEntityId);
		
		String sql = "select sum(trx_quantity) as total from KOL_PES_MOVE_TXN_RESULT where interfaced = 0 and wip_entity_id = "+wipEntityId;
		QueryRunner runner = new QueryRunner(this.dataSource);
		int trxTotal = runner.query(sql, new ResultSetHandler<Integer>() {

			public Integer handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return 0;
		        }
			
				while(rs.next()) { 
					return rs.getInt("total");
				}
				
				return 0;
			}
		});
		
		LogUtil.log("getLeftCountForStartOp:opCode="+opCode+", trxTotal="+trxTotal+", incompleteQuan="+incompleteQuan);

		return incompleteQuan-trxTotal;
		
//		if(incompleteQuan >= 0) {
//			List<DataTxnSeqItem> seqListTemp = findTxnSeqList(wipEntityId, null);
//			int trxQuanTotal = getAllTxnSeqTrxQuantity(seqListTemp);
//			int trxQuanNotEnd = getNotEndsTxnSeqTrxQuantity(seqListTemp);
//			
//			LogUtil.log("getLeftCountForStartOp:opCode="+opCode+", trxQuanTotal="+trxQuanTotal+", trxQuanNotEnd="+trxQuanNotEnd+", incompleteQuan="+incompleteQuan);
//			
//			if(trxQuanTotal>incompleteQuan) {
//				return incompleteQuan;
//			}
//			else if(trxQuanTotal==incompleteQuan) {
//				
//				String curOpCode = getOpCodeByOpId(routing_sequence_id, curOperationId);
//				String lastWorkingOpCode = getCurReallyWorkingOpCode(wipEntityId);
//				LogUtil.log("getLeftCountForStartOp:curOpCode="+curOpCode+", lastWorkingOpCode="+lastWorkingOpCode);
//				if(lastWorkingOpCode!=null && lastWorkingOpCode.equals(curOpCode)) {
//					return incompleteQuan-trxQuanTotal;
//				}
//				else if(trxQuanNotEnd>0) {
//					return incompleteQuan-trxQuanTotal;
//				}
//				else {
//					return incompleteQuan;
//				}
//			}
//			else {
//				return incompleteQuan-trxQuanTotal;
//			}
//		}
//		
//		
//		return 0;
	}
	
	//获取工序的加工设备列表
	public List<DataAssetInfoItem> getOpAssetList(String opCode) throws SQLException {
		String sql = "select asset_id, tag_number, description from kol_pes_asset where op_code='"+opCode+"'";// and asset_id != -1
		LogUtil.log("getOpAssetList():sql="+sql);
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<List<DataAssetInfoItem>>() {

			public List<DataAssetInfoItem> handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
				
				List<DataAssetInfoItem> opAssetList = new ArrayList<DataAssetInfoItem>();
				
				while(rs.next()) { 
					DataAssetInfoItem data = new DataAssetInfoItem();

					data.description = rs.getString("DESCRIPTION");

					opAssetList.add(data);
				}

				return opAssetList;
			}
		});
	}
	
	//获取输入的txn工序列表的输入数量之和
	private int getAllTxnSeqTrxQuantity(List<DataTxnSeqItem> seqList) {
		int allTrxQ = 0;
		if(seqList!=null && seqList.size()>0) {
			for(DataTxnSeqItem seq : seqList) {
				if(seq!=null && seq.trxQuantity>=0) {
					allTrxQ = allTrxQ + seq.trxQuantity;
				}
			}
		}
		
		return allTrxQ;
	}
	
	//获取输入的txn工序列表的未完成工序的输入数量之和
	private int getNotEndsTxnSeqTrxQuantity(List<DataTxnSeqItem> seqList) {
		int allTrxQ = 0;
		if(seqList!=null && seqList.size()>0) {
			for(DataTxnSeqItem seq : seqList) {
				if(seq!=null && seq.trxQuantity>=0 && (seq.opEnd==null||seq.opEnd.trim().length()==0)) {
					allTrxQ = allTrxQ + seq.trxQuantity;
				}
			}
		}
		
		return allTrxQ;
	}

	//根据已知的工单号和工序号，获取某元件某道工序的加工情况
	private List<DataTxnSeqItem> findTxnSeqList(int wipEntityId, String opCode) throws SQLException {
		
		String sql = "select FM_OPERATION_CODE,TRX_QUANTITY,OP_END from kol_pes_move_txn_result where WIP_ENTITY_ID = "+wipEntityId+" and FM_OPERATION_CODE='"+opCode+"' and (interfaced = 0 or interfaced = 1) order by transaction_id desc";
		if(opCode == null) {//工单号为空时，检索这个工单的所有工序
			sql = "select FM_OPERATION_CODE,TRX_QUANTITY,OP_END from kol_pes_move_txn_result where WIP_ENTITY_ID="+wipEntityId+" and (interfaced = 0 or interfaced = 1) order by transaction_id desc";
		}
		
		LogUtil.log("findTxnSeqList():sql="+sql);
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<List<DataTxnSeqItem>>() {

			public List<DataTxnSeqItem> handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
				
				List<DataTxnSeqItem> tempList = new ArrayList<DataTxnSeqItem>();

				String firstOpCode = null;
				
				while(rs.next()) { 
					DataTxnSeqItem data = new DataTxnSeqItem();
//					data.transactionId = rs.getString("TRANSACTION_ID");
//					data.creationDate = CommonUtil.formatDateTime(rs.getTimestamp("CREATION_DATE"));
//					data.createdBy = rs.getString("CREATED_BY");
//					data.lastUpdateDate = CommonUtil.formatDateTime(rs.getTimestamp("LAST_UPDATE_DATE"));
//					data.lastUpdatedBy = rs.getString("LAST_UPDATED_BY");
//					data.wipEntityId = rs.getString("WIP_ENTITY_ID");
					data.fmOperationCode = rs.getString("FM_OPERATION_CODE");
					data.trxQuantity = rs.getInt("TRX_QUANTITY");
//					data.scrapQuantity = rs.getString("SCRAP_QUANTITY");
//					data.assetId = rs.getString("ASSET_ID");
//					data.opStart = CommonUtil.formatDateTime(rs.getTimestamp("OP_START"));
					data.opEnd = CommonUtil.formatDateTime(rs.getTimestamp("OP_END"));
//					data.interfaced = rs.getInt("INTERFACED");
					
					if(firstOpCode == null) {//记录最顶端的工单code
						firstOpCode = data.fmOperationCode;
					}
					
					LogUtil.log("findTxnSeqList():data.fmOperationCode="+data.fmOperationCode);
					
					if(!firstOpCode.equals(data.fmOperationCode)) {//当一个新的工序出现时，结束统计。因为只有排在最前面的几个工序是我们需要的
						return tempList;
					}
					
					tempList.add(data);
				}

				return tempList;
			}
		});
	}
	
	//在开始一个工序前，判断是否有这个工单的其它工序还没有加工完，如果有，则不能启动一个新的不同工序
	public String isOtherOpNotEndBeforeStartTheNewOne(int WIP_ENTITY_ID, String THIS_OPERATION_CODE) throws SQLException {
		
		String sql = "select FM_OPERATION_CODE from kol_pes_move_txn_result where WIP_ENTITY_ID = "+WIP_ENTITY_ID+" and FM_OPERATION_CODE!='"+THIS_OPERATION_CODE+"' and OP_END is null";
		LogUtil.log("isOtherOpNotEndBeforeStartTheNewOne():sql="+sql);
		QueryRunner runner = new QueryRunner(this.dataSource);
		return runner.query(sql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
			
				while(rs.next()) { 
					return rs.getString("FM_OPERATION_CODE");
				}
				
				return null;
			}
		});
	}
	
	//在开始一个新的不同工序前，判断当前cur_operation_id对应的工序是否全数完成，如没有则不能开启新工序
	public boolean isCurrentOpCompletedBeforeStartTheNewOne(String wipName, int WIP_ENTITY_ID, int routing_sequence_id, String curOperationId, String opCodeWantStart) throws SQLException {
		
		LogUtil.log("isCurrentOpCompletedBeforeStartTheNewOne()");
		
		String curWorkingOpCode = getCurReallyWorkingOpCode(WIP_ENTITY_ID);
		
		LogUtil.log("isCurrentOpCompletedBeforeStartTheNewOne():curWorkingOpCode="+curWorkingOpCode+", opCodeWantStart="+opCodeWantStart);
		
		if(curWorkingOpCode==null || opCodeWantStart.equals(curWorkingOpCode)) {//如果要开启的工序是cur_operation_id对应的工序，则直接返回true
			return true;
		}
		
		return isCurrentOpCompletedAfterItemSelected(WIP_ENTITY_ID);
	}
	
	//工序开始前运行一个存储过程，确定下能否开启这个工序
	public DataAvailQuanErrorCodeMsgItem runProcedureBeforeStartOp(int WIP_ENTITY_ID, String FM_OPERATION_CODE) throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call PES.KOL_PES_UTIL_PKG.operationStart(?,?,?,?,?)}");
		cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
		cstmt.setInt(1, WIP_ENTITY_ID);
		cstmt.setString(2, FM_OPERATION_CODE);
		cstmt.execute();
		int availQty = cstmt.getInt(3);
		int errorCode = cstmt.getInt(4);
		String errorMsg = cstmt.getString(5);
		LogUtil.log("runProcedureBeforeStartOp():WIP_ENTITY_ID="+WIP_ENTITY_ID+", FM_OPERATION_CODE="+FM_OPERATION_CODE+", errorCode="+errorCode+", availQty="+availQty+", errorMsg="+errorMsg);
		cstmt.close();
		con.close();
		
		DataAvailQuanErrorCodeMsgItem dataItem = new DataAvailQuanErrorCodeMsgItem();
		dataItem.availQty = availQty;
		dataItem.errorCode = errorCode;
		dataItem.errorMsg = errorMsg;
		
		return dataItem;
	}
	
	//进入完成工序界面时运行一个存储过程，确定下能否完成这个工序
	public DataCodeMessageItem runProcedureBeforeEndOp(int WIP_ENTITY_ID, String FM_OPERATION_CODE) throws SQLException {

		DataCodeMessageItem item = new DataCodeMessageItem();
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call PES.KOL_PES_UTIL_PKG.operationBeforeComplete(?,?,?,?)}");
		cstmt.setInt(1, WIP_ENTITY_ID);
		cstmt.setString(2, FM_OPERATION_CODE);
		cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
		cstmt.execute();
		int resErrCode = cstmt.getInt(3);
		LogUtil.log("runProcedureBeforeEndOp():WIP_ENTITY_ID="+WIP_ENTITY_ID+", FM_OPERATION_CODE="+FM_OPERATION_CODE+", cstmt.getInt(3)="+resErrCode+", errorMsg="+cstmt.getString(4));
		
		item.code = resErrCode;
		item.message = CommonUtil.noNullString(cstmt.getString(4));
		
		cstmt.close();
		con.close();
		
		return item;
	}
	
	//工序开始前运行一个存储过程，确定下能否开启这个工序
	public int runProcedureAfterStartOp(int WIP_ENTITY_ID, String FM_OPERATION_CODE) throws SQLException {

		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call PES.KOL_PES_UTIL_PKG.operationStarted(?,?,?)}");
		cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
		cstmt.setInt(1, WIP_ENTITY_ID);
		cstmt.setString(2, FM_OPERATION_CODE);
		cstmt.execute();
		int resErrCode = cstmt.getInt(3);
		LogUtil.log("runProcedureAfterStartOp():cstmt.getInt(3)="+resErrCode);
		cstmt.close();
		con.close();
		
		return resErrCode;
	}
	
	//开始工序操作时，插相应数据
	public int insertWhenStartAnOp(String transactionId, Date CREATION_DATE, String CREATED_BY,
									Date LAST_UPDATE_DATE, String LAST_UPDATED_BY, int WIP_ENTITY_ID,
									String FM_OPERATION_CODE, String TRX_QUANTITY, String SCRAP_QUANTITY,
									String assetId1, String assetId2, String assetId3, Date OP_START, int routing_sequence_id, boolean canJump) throws SQLException {
		    
			if(assetId1 == null) {
				assetId1 = String.valueOf(-1);
			}
		
			QueryRunner runner = new QueryRunner(this.dataSource);

			Object[] sqlParam = new Object[] {transactionId.trim(), CommonUtil.formatDateTime(CREATION_DATE), CREATED_BY.trim(), CommonUtil.formatDateTime(LAST_UPDATE_DATE), LAST_UPDATED_BY.trim(),
											  WIP_ENTITY_ID, FM_OPERATION_CODE, TRX_QUANTITY.trim(), SCRAP_QUANTITY.trim(), assetId1, assetId2, assetId3, CommonUtil.formatDateTime(OP_START)};
			
//			///////////寻找move_txn没有正确备份的原因
//			try {
//				runner.update("insert into kol_pes_move_txn_result_log(TRANSACTION_ID, CREATION_DATE, CREATED_BY,"+
//						 "LAST_UPDATE_DATE, LAST_UPDATED_BY, WIP_ENTITY_ID,"+
//						 "FM_OPERATION_CODE, TRX_QUANTITY, SCRAP_QUANTITY,"+
//						 "ASSET_ID,ASSET_ID1,ASSET_ID2, OP_START) values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))", sqlParam);
//				LogUtil.log("insert into kol_pes_move_txn_result_log");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			////////////////////////////////////
			
			return runner.update("insert into kol_pes_move_txn_result(TRANSACTION_ID, CREATION_DATE, CREATED_BY,"+
								 "LAST_UPDATE_DATE, LAST_UPDATED_BY, WIP_ENTITY_ID,"+
								 "FM_OPERATION_CODE, TRX_QUANTITY, SCRAP_QUANTITY,"+
								 "ASSET_ID,ASSET_ID1,ASSET_ID2, OP_START) values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))", sqlParam);
	}
	
	//完成工序时的数据库操作
	public int updateWhenEndAnOp(String transactionId, int SCRAP_QUANTITY, Date OP_END, String LAST_UPDATED_BY, Date LAST_UPDATE_DATE) throws SQLException {
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("update kol_pes_move_txn_result set ");
		sbSql.append("SCRAP_QUANTITY=?,");
		sbSql.append("LAST_UPDATED_BY=?,");
		sbSql.append("LAST_UPDATE_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),");
		sbSql.append("OP_END=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
		sbSql.append(" where TRANSACTION_ID=?");
		
		Object[] params = new Object[] {
										SCRAP_QUANTITY,
										LAST_UPDATED_BY.trim(),
										CommonUtil.formatDateTime(LAST_UPDATE_DATE),
										CommonUtil.formatDateTime(OP_END),
										transactionId.trim()
									   };
		
		LogUtil.log("updateWhenEndAnOp():sql="+sbSql.toString());
		LogUtil.log("updateWhenEndAnOp():SCRAP_QUANTITY="+SCRAP_QUANTITY);
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		int resRows = runner.update(sbSql.toString(), params);
		
//		///////////寻找move_txn没有正确备份的原因////
//		try {
//			StringBuilder sbSqlTest = new StringBuilder();
//			sbSql.append("insert into kol_pes_move_txn_result_log(");
//			sbSql.append("SCRAP_QUANTITY,");
//			sbSql.append("LAST_UPDATED_BY,");
//			sbSql.append("LAST_UPDATE_DATE,");
//			sbSql.append("OP_END, TRANSACTION_ID) ");
//			sbSql.append("values(?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)");
//			
//			runner.update(sbSqlTest.toString(), params);
//			LogUtil.log("update kol_pes_move_txn_result_log set");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		////////////////////////////////////////
		
		return resRows;
	}
	
	//插入需要推送的坏品超标消息
	private void insertOpPushMessage(String transactionId) throws SQLException {
		
		DataSeqStartedItem tempSeq = getOpStartedItemByTransId(transactionId);
		
		List<DataOpFailNotifyItem> opFialList = getOpCodeListNeedPushMsg();
		
		if(tempSeq!=null && opFialList!=null && opFialList.size()>0) {
			
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			for(DataOpFailNotifyItem failItem : opFialList) {
				
				if(failItem!=null && failItem.opCode!=null && failItem.opCode.equals(tempSeq.fmOperationCode) &&
				   tempSeq.scrapQuantity!=null && CommonUtil.isValidNumber(tempSeq.scrapQuantity) &&
				   tempSeq.trxQuantity!=null && CommonUtil.isValidNumber(tempSeq.trxQuantity)) {
					
					float scrapPercent = (Float.valueOf(tempSeq.scrapQuantity)/Float.valueOf(tempSeq.trxQuantity));
					
					LogUtil.log("scrapPercent="+scrapPercent+", failItem.scrapPercent="+failItem.scrapPercent);
					
					if(Float.valueOf(failItem.scrapPercent) < scrapPercent) {//如果坏品比例超出system的上线则需要推送
						
						String insSql = "insert into kol_pes_op_scrap_push_msg "
										+ "(TRANSACTION_ID, WIP_ENTITY_ID, CREATION_DATE, CREATED_BY, OP_CODE, KEEP_PUSH_DAYS, "
										+ "TRX_QUANTITY, SCRAP_QUANTITY)"
										+" values(?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?)";
				
						Object[] sqlParam = new Object[] {tempSeq.transactionId,tempSeq.wipEntityId,
														  CommonUtil.formatDateTime(new Date(Calendar.getInstance().getTimeInMillis())),
														  tempSeq.lastUpdateBy,tempSeq.fmOperationCode, 2, 
														  tempSeq.trxQuantity, tempSeq.scrapQuantity};
						runner.update(insSql, sqlParam);
						
						LogUtil.log("insertOpPushMessage:"+insSql);
					}
					break;
				}
			}
		}
	}
	
	//获取transId获取工序的详细信息
	private DataSeqStartedItem getOpStartedItemByTransId(String transactionId) throws SQLException {
		
		String sql  = "select * from kol_pes_move_txn_result where TRANSACTION_ID="+transactionId;
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		return runner.query(sql, new ResultSetHandler<DataSeqStartedItem>() {

			public DataSeqStartedItem handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }

				while(rs.next()) { 
					DataSeqStartedItem data = new DataSeqStartedItem();
					data.transactionId = String.valueOf(rs.getInt("TRANSACTION_ID"));
					data.wipEntityId = rs.getString("WIP_ENTITY_ID");
					data.lastUpdateBy = rs.getString("LAST_UPDATED_BY");
					data.fmOperationCode = rs.getString("FM_OPERATION_CODE");
					data.trxQuantity = rs.getString("TRX_QUANTITY");
					data.scrapQuantity = rs.getString("SCRAP_QUANTITY");
					data.opStartDate = CommonUtil.formatDateTime(rs.getTimestamp("OP_START"));
					data.opEndDate = CommonUtil.noNullString(CommonUtil.formatDateTime(rs.getTimestamp("OP_END")));
					
					return data;
				}

				return null;
			}
		});
	}
	
	//从system表格中拿到哪些工序需要消息推送
	private List<DataOpFailNotifyItem> getOpCodeListNeedPushMsg() throws SQLException {
		
		String sql = "select * from kol_pes_system_param where SOURCE='OPERATION_FAIL_NOTIFY'";
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		return runner.query(sql, new ResultSetHandler<List<DataOpFailNotifyItem>>() {

			public List<DataOpFailNotifyItem> handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
				
				List<DataOpFailNotifyItem> tempList = new ArrayList<DataOpFailNotifyItem>();

				while(rs.next()) { 
					DataOpFailNotifyItem data = new DataOpFailNotifyItem();
					data.opCode = rs.getString("PROFILE_NAME");
					data.scrapPercent = rs.getString("PROFILE_VALUE");
					data.accessLevel = rs.getString("ACCESS_LEVEL");
					
					tempList.add(data);
				}

				return tempList;
			}
		});
	}
	
	//工序完成时运行一个存储过程，告诉系统更新下工单的状态
	public DataSeqProcedureItem runSQLNoticeJobStatus(String transactionId) throws SQLException {
		
		DataSeqStartedItem tempSeq = getOpStartedItemByTransId(transactionId);
		
		if(tempSeq!=null) {
			Connection con = this.dataSource.getConnection();
			CallableStatement cstmt = con.prepareCall("{call PES.KOL_PES_UTIL_PKG.operationComplete(?,?,?,?,?)}");
			
			LogUtil.log("transactionId="+transactionId+", tempSeq.wipEntityId="+tempSeq.wipEntityId+", tempSeq.fmOperationCode="+tempSeq.fmOperationCode);
			cstmt.setInt(1, Integer.valueOf(transactionId.trim()));
			cstmt.setInt(2, Integer.valueOf(tempSeq.wipEntityId.trim()));
			cstmt.setString(3, tempSeq.fmOperationCode);
			
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
			
			cstmt.execute();
			
			int resErrCode = cstmt.getInt(4);
			String resErrMsg = cstmt.getString(5);
			
		
			LogUtil.log("runSQLNoticeJobStatus():resErrCode="+resErrCode+", resErrMsg="+resErrMsg);
			cstmt.close();
			con.close();
			
			if(resErrCode == 0) {
				insertOpPushMessage(transactionId);
			}
			
			return new DataSeqProcedureItem(resErrCode, resErrMsg);
		}
		
		return new DataSeqProcedureItem();
		//declare errorcode number; begin PES.KOL_PES_UTIL_PKG.operationComplete(1077836, 'CD00', errorcode); end;
	}
	
	public int getOpTrxId(String transactionId) throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sqlGetOpTrxId = "select count(1) as have from wsm_lot_move_txn_interface where source_line_id = "+transactionId;
	
		return runner.query(sqlGetOpTrxId, new ResultSetHandler<Integer>() {

			public Integer handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return 0;
		        }
			
				while(rs.next()) { 
					return rs.getInt("have");
				}
				
				return 0;
			}
		});
	}
	
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
	
	//判断当前工序是否都结束了
	public boolean isCurrentOpCompletedAfterItemSelected(int WIP_ENTITY_ID) throws SQLException {

		QueryRunner runner = new QueryRunner(this.dataSource);
		String sqlGetOpCode = "select fm_operation_code from kol_pes_move_txn_result where WIP_ENTITY_ID="+WIP_ENTITY_ID+" and interfaced = 0";
	
		String opCode = runner.query(sqlGetOpCode, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
			
				while(rs.next()) { 
					return rs.getString("fm_operation_code");
				}
				
				return null;
			}
		});
		LogUtil.log("isCurrentOpCompletedAfterItemSelected():opCode="+opCode);
		
		return !(opCode!=null && opCode.length()>0);
	}
	
	//考虑到跳工序的情况，判断下到底现在是那个工序正在生产
	public String getCurReallyWorkingOpCode(int wipEntityId) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sqlGetOpCode = "select fm_operation_code from kol_pes_move_txn_result where WIP_ENTITY_ID="+wipEntityId+" and (interfaced = 0 or interfaced = 1) order by transaction_id desc";
	
		String opCode = runner.query(sqlGetOpCode, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
			
				while(rs.next()) { 
					return rs.getString("fm_operation_code");
				}
				
				return null;
			}
		});
		LogUtil.log("getCurReallyWorkingOpCode():opCode="+opCode);
		return opCode;
	}
	
	
	//判断完成工序时是否加了质量管理计划
	public boolean isQaFilledWhenEndingAnOp(String transId) throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		String sql = "select count(transaction_id) as aaa from kol_pes_qa_result where transaction_id="+transId;
	
		LogUtil.log("isQaFilledWhenEndingAnOp:sql="+sql);
		
		return runner.query(sql, new ResultSetHandler<Boolean>() {

			public Boolean handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return false;
		        }
				
				while(rs.next()) {
					return rs.getInt("aaa")>0;
				}
				
				return false;
			}
		});
	}
	
	//获取某个工单的某一工序的未完成开启数目，判断是否需要填写质量收集计划用
	public boolean isLastUncompleteOpNumForWip(String wipId, String opCode, boolean canJump) throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		String sql = "select count(transaction_id)as aaa from kol_pes_move_txn_result where wip_entity_id="+wipId+" and fm_operation_code='"+opCode+"' and op_end is null";
	
		int uncompleteSeqNum = runner.query(sql, new ResultSetHandler<Integer>() {

			public Integer handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return 0;
		        }
				
				while(rs.next()) {
					return rs.getInt("aaa");
				}
				
				return 0;
			}
		});
		
		List<String> tempList = getWipNameAndSeqId(wipId);
		
		int leftQuan = getLeftCountForStartOp(tempList.get(0), Integer.valueOf(wipId), Integer.valueOf(tempList.get(1)), opCode, tempList.get(2), canJump);
	
		LogUtil.log("isLastUncompleteOpNumForWip:sql="+sql+"\n uncompleteSeqNum="+uncompleteSeqNum+", leftQuan="+leftQuan);
		
		return uncompleteSeqNum<=1 && leftQuan<=0;
	}
	
	//获取工单名称和工序id
	private List<String> getWipNameAndSeqId(String wipEntityId) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		String sql = "select wip_entity_name, common_routing_sequence_id from kol_pes_os_job where wip_entity_id="+wipEntityId;
	
		return runner.query(sql, new ResultSetHandler<List<String>>() {

			public List<String> handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }
				
				List<String> tempList = new ArrayList<String>();
				
				while(rs.next()) {
					tempList.add(rs.getString("wip_entity_name"));
					tempList.add(rs.getString("common_routing_sequence_id"));
					tempList.add("cur_operation_id");
					
					return tempList;
				}
				
				return tempList;
			}
		});
	}
	
	
	
	//获取某个员工开启的所有工序，检索参数为staffNo或AssetTagNumber
	public List<DataSeqStartedItem> getSeqStartedList(String staffNoOrWipNum) throws SQLException {
		
		if(CommonUtil.isStringNotNull(staffNoOrWipNum)) {
		
			String sql = "select r.*, staf.staff_name, akk.*, j.*, s.operation_description from kol_pes_move_txn_result r, " +
						 "(select asset_id, tag_number, description from kol_pes_asset group by asset_id, tag_number, description) akk, " +
						 "kol_pes_os_job j, kol_pes_staffs staf, kol_pes_op_seq s where r.op_end is null and r.asset_id = akk.asset_id and j.wip_entity_id = r.wip_entity_id " + 
						 "and (r.created_by = "+staffNoOrWipNum+" or r.wip_entity_id = "+staffNoOrWipNum+") and r.fm_operation_code = s.standard_operation_code " +
						 "and j.common_routing_sequence_id = s.routing_sequence_id and staf.staff_no = r.created_by order by r.transaction_id desc";
			
			LogUtil.log("getSeqStartedList:sql="+sql);
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			return runner.query(sql, new ResultSetHandler<List<DataSeqStartedItem>>() {
	
				public List<DataSeqStartedItem> handle(ResultSet rs) throws SQLException { 
					if (rs == null) {
			            return null;
			        }
					
					List<DataSeqStartedItem> tempList = new ArrayList<DataSeqStartedItem>();
	
					while(rs.next()) { 
						DataSeqStartedItem data = new DataSeqStartedItem();
						data.transactionId = String.valueOf(rs.getInt("TRANSACTION_ID"));
						data.wipEntityId = rs.getString("WIP_ENTITY_ID");
						data.wipEntityName = rs.getString("WIP_ENTITY_NAME");
						data.creationDate = CommonUtil.formatDateTime(rs.getTimestamp("CREATION_DATE"));
						data.createdBy = rs.getString("CREATED_BY");
						data.lastUpdateDate = CommonUtil.formatDateTime(rs.getTimestamp("LAST_UPDATE_DATE"));
						data.lastUpdateBy = rs.getString("STAFF_NAME");
						data.fmOperationCode = rs.getString("FM_OPERATION_CODE");
						data.opDesc = CommonUtil.noNullString(rs.getString("OPERATION_DESCRIPTION"));//OP_DSCR
						data.trxQuantity = rs.getString("TRX_QUANTITY");
						data.assetDesc = rs.getString("DESCRIPTION");
						data.opStartDate = CommonUtil.formatDateTime(rs.getTimestamp("OP_START"));
						data.opEndDate = CommonUtil.noNullString(CommonUtil.formatDateTime(rs.getTimestamp("OP_END")));
						data.assettagNumber = rs.getString("TAG_NUMBER");
						
						//job
						data.saItem = rs.getString("SA_ITEM");
						data.saItemDesc = rs.getString("SA_ITEM_DESC");
						
						data.dffCpnNumber = "";//CommonUtil.noNullString(rs.getString("DFF_CPN_NUMBER"));
						
						data.dffCustomerspec = "";//CommonUtil.noNullString(rs.getString("DFF_CPN_NUMBER"));
						data.dffMfgSpec = CommonUtil.noNullString(rs.getString("DFF_MFG_SPEC"));
						
						data.custNumber = "";//CommonUtil.noNullString(rs.getString("CUST_NUMBER"));
						
						data.incompleteQuantity = 0;//rs.getInt("INCOMPLETE_QUANTITY");
						data.startQuantity = rs.getInt("START_QUANTITY");
						data.quantityCompleted = rs.getInt("QUANTITY_COMPLETED");
						data.quantityScrapped = rs.getInt("QUANTITY_SCRAPPED");
						
						data.primaryItemId = rs.getInt("PRIMARY_ITEM_ID");
						data.commonRoutingSequenceId = rs.getString("COMMON_ROUTING_SEQUENCE_ID");
						
						data.curOperationId = "";//rs.getString("CUR_OPERATION_ID");
						data.organizationId = rs.getString("ORGANIZATION_ID");
						
						data.pctComplete = "";
						
						tempList.add(data);
					}
	
					return tempList;
				}
			});
		}
		
		return null;
	}
	
	//获取某个工单的工序加工情况，呼应客户端首界面的“查询工单”功能
	public List<DataSeqStartedItem> getSeqAllListByWipId(String wipId) throws SQLException {
		
		String sql = "select kol_pes_move_txn_result.*, kol_pes_staffs.STAFF_NAME, a.*, kol_pes_os_job.*, kol_pes_op_seq.OPERATION_DESCRIPTION from "+
					 "kol_pes_move_txn_result,(select asset_id, tag_number,DESCRIPTION from kol_pes_asset group by asset_id, tag_number,DESCRIPTION) a,kol_pes_os_job,kol_pes_staffs,kol_pes_op_seq "+
					 "where "+
					 "kol_pes_move_txn_result.asset_id=a.asset_id and "+ 
					 "kol_pes_os_job.wip_entity_id=kol_pes_move_txn_result.wip_entity_id and "+
					 "kol_pes_move_txn_result.wip_entity_id="+wipId+" and "+
					 "kol_pes_move_txn_result.fm_operation_code=kol_pes_op_seq.standard_operation_code and "+
					 "kol_pes_os_job.common_routing_sequence_id=kol_pes_op_seq.routing_sequence_id and "+
					 "kol_pes_staffs.STAFF_NO=kol_pes_move_txn_result.created_by order by transaction_id desc";
		
		LogUtil.log("getSeqAllListByWipId:sql="+sql);
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		
		return runner.query(sql, new ResultSetHandler<List<DataSeqStartedItem>>() {

			public List<DataSeqStartedItem> handle(ResultSet rs) throws SQLException { 
				if (rs == null) {
		            return null;
		        }
				
				List<DataSeqStartedItem> tempList = new ArrayList<DataSeqStartedItem>();

				while(rs.next()) { 
					DataSeqStartedItem data = new DataSeqStartedItem();
					data.transactionId = String.valueOf(rs.getInt("TRANSACTION_ID"));
					data.wipEntityId = rs.getString("WIP_ENTITY_ID");
					data.wipEntityName = rs.getString("WIP_ENTITY_NAME");
					data.creationDate = CommonUtil.formatDateTime(rs.getTimestamp("CREATION_DATE"));
					data.createdBy = rs.getString("CREATED_BY");
					data.lastUpdateDate = CommonUtil.formatDateTime(rs.getTimestamp("LAST_UPDATE_DATE"));
					data.lastUpdateBy = rs.getString("STAFF_NAME");
					data.fmOperationCode = rs.getString("FM_OPERATION_CODE");
					data.opDesc = rs.getString("OPERATION_DESCRIPTION");
					data.trxQuantity = rs.getString("TRX_QUANTITY");
					data.scrapQuantity =  CommonUtil.noNullString(rs.getString("SCRAP_QUANTITY"));
					data.assetDesc = rs.getString("DESCRIPTION");
					data.opStartDate = CommonUtil.formatDateTime(rs.getTimestamp("OP_START"));
					data.opEndDate = CommonUtil.noNullString(CommonUtil.formatDateTime(rs.getTimestamp("OP_END")));
					data.assettagNumber = rs.getString("TAG_NUMBER");
					data.interfaced = rs.getString("interfaced");
					
					//job
					data.saItem = rs.getString("SA_ITEM");
					data.saItemDesc = rs.getString("SA_ITEM_DESC");
					
					data.dffCpnNumber = CommonUtil.noNullString(rs.getString("DFF_CPN_NUMBER"));
					
					data.dffCustomerspec = CommonUtil.noNullString(rs.getString("DFF_CPN_NUMBER"));
					data.dffMfgSpec = CommonUtil.noNullString(rs.getString("DFF_MFG_SPEC"));
					
					data.custNumber = CommonUtil.noNullString(rs.getString("CUST_NUMBER"));
					
					data.incompleteQuantity = rs.getInt("INCOMPLETE_QUANTITY");
					data.startQuantity = rs.getInt("START_QUANTITY");
					data.quantityCompleted = rs.getInt("QUANTITY_COMPLETED");
					data.quantityScrapped = rs.getInt("QUANTITY_SCRAPPED");
					
					data.primaryItemId = rs.getInt("PRIMARY_ITEM_ID");
					data.commonRoutingSequenceId = rs.getString("COMMON_ROUTING_SEQUENCE_ID");
					
					data.curOperationId = CommonUtil.noNullString(rs.getString("CUR_OPERATION_ID"));
					data.organizationId = CommonUtil.noNullString(rs.getString("ORGANIZATION_ID"));
					
					tempList.add(data);
				}

				return tempList;
			}
		});
	}
	
	//删除一个工序的函数 
	public int deleteOpByTransId(String transactionId) throws SQLException {
		
		String sql = "delete kol_pes_move_txn_result where TRANSACTION_ID="+transactionId; 
		
		LogUtil.log("deleteOpByTransId():sql="+sql);
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		int resRows = runner.update(sql);
		LogUtil.log("deleteOpByTransId="+resRows);
		return resRows;
	}
	
	public String getTimeBufferForOpStart() throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "select profile_value from kol_pes_system_param where source='MOVE_TXN_TIME_BUFFER' and profile_name='OP_START'";
		
		return runner.query(sql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }

				while(rs.next()) {
					return rs.getString("profile_value");
				}
				
				return null;
			}
		});
	}
	
	public String getTimeBufferForOpEnd() throws SQLException {
		
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "select profile_value from kol_pes_system_param where source='MOVE_TXN_TIME_BUFFER' and profile_name='OP_END'";
		
		return runner.query(sql, new ResultSetHandler<String>() {

			public String handle(ResultSet rs) throws SQLException {
				if (rs == null) {
		            return null;
		        }

				while(rs.next()) {
					return rs.getString("profile_value");
				}
				
				return null;
			}
		});
	}
}

