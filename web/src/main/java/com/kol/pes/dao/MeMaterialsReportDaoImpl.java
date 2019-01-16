package com.kol.pes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kol.pes.item.DataCodeMessageItem;
import com.kol.pes.item.DataMeMaterialsNumItem;
import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;


@Repository("materialsReportDao")
public class MeMaterialsReportDaoImpl implements MeMaterialsReportDao {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	
	public DataMeTimeReportProduceInfoAndSeqList seqListByProduceId(String wipId) throws SQLException {
		
		if(CommonUtil.isStringNotNull(wipId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			String selectSeqSql = "select wo.operation_seq_num, bso.operation_code from wip_operations wo, bom_standard_operations bso where wo.standard_operation_id = bso.standard_operation_id and wo.organization_id = bso.organization_id  and wo.wip_entity_id ="+wipId;
			LogUtil.log("seqListByProduceId():selectSeqSql="+selectSeqSql);
			
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
						item.operationDescription = CommonUtil.noNullString(rs.getString("operation_seq_num"));
						tempList.add(item);
					}
					return tempList;
				}
			});
			
			return data;
		}
		return null;
	}
	
	
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo(String wipId, int type, String seqNum) throws SQLException {
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.getIssuedItem(?,?,?,?)}");
		
		cstmt.setInt(1, wipId!=null?Integer.valueOf(wipId):0);
		cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
		cstmt.setInt(3, Integer.valueOf(seqNum));
		cstmt.setInt(4, type);
		cstmt.execute();
		
		String display = cstmt.getString(2);
		
		cstmt.close();
		con.close();
		
		DataMeTimeReportDescInfoAndActiveListInfo dataItem = new DataMeTimeReportDescInfoAndActiveListInfo();
		
		dataItem.display = CommonUtil.noNullString(display);

		LogUtil.log("getDescInfo(): dataItem.display="+dataItem.display);
		return dataItem;
	}
	
	public List<DataMeMaterialsNumItem> getMaterialsNum(String type, String wipId, String seqNum, String keyWords) throws SQLException {
		if(CommonUtil.isStringNotNull(wipId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			
			String selectSeqSql = "select wro.inventory_item_id, msi.segment1 || '-' || msi.segment2 || '-' || msi.segment3 || '-' || msi.segment4 item, substr(msi.description, 1, 50) dscr from wip_requirement_operations wro, mtl_system_items msi where wro.inventory_item_id = msi.inventory_item_id and wro.organization_id = msi.organization_id and wro.wip_entity_id = "+wipId+" and wro.operation_seq_num = "+seqNum;
			
			if(!"1".equals(type)) {
				selectSeqSql = "select inventory_item_id, msi.segment1 || '-' || msi.segment2 || '-' || msi.segment3 || '-' || msi.segment4 item, substr(msi.description, 1, 50) dscr from mtl_system_items msi where msi.segment1 || '-' || msi.segment2 || '-' || msi.segment3 || '-' || msi.segment4 like '"+keyWords+"%' and organization_id = 85";
			}
			
			LogUtil.log("getMaterialsNum():selectSeqSql="+selectSeqSql);
			
			return runner.query(selectSeqSql, new ResultSetHandler<List<DataMeMaterialsNumItem>>() {
				
				public List<DataMeMaterialsNumItem> handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					
					List<DataMeMaterialsNumItem> tempList = new ArrayList<DataMeMaterialsNumItem>();

					while(rs.next()) { 
						DataMeMaterialsNumItem item = new DataMeMaterialsNumItem();
						item.item = CommonUtil.noNullString(rs.getString("item"));
						item.itemId = CommonUtil.noNullString(rs.getString("inventory_item_id"));
						item.desc = CommonUtil.noNullString(rs.getString("dscr"));
						tempList.add(item);
					}
					return tempList;
				}
			});
		}
		return null;
	}
	
	public DataMeTimeReportDescInfoAndActiveListInfo getMaterialsDescInfo(String wipId, String itemId) throws SQLException {
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.getItemDisplay(?,?,?)}");
		
		cstmt.setInt(1, wipId!=null?Integer.valueOf(wipId):0);
		cstmt.setInt(2, itemId!=null?Integer.valueOf(itemId):0);
		cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);

		cstmt.execute();
		
		String display = cstmt.getString(3);
		
		cstmt.close();
		con.close();
		
		DataMeTimeReportDescInfoAndActiveListInfo dataItem = new DataMeTimeReportDescInfoAndActiveListInfo();
		
		dataItem.display = CommonUtil.noNullString(display);

		LogUtil.log("getMaterialsDescInfo(): dataItem.display="+dataItem.display);
		return dataItem;
	}
	
	public DataCodeMessageItem materialsReportComplete(String assetCode, String staffNo, 
														String reportType, String wipId, 
														String opCode, String seqNum, 
														String itemId, String trxQty, 
														String remark, String schedDate) throws SQLException {
		
		Connection con = this.dataSource.getConnection();
		CallableStatement cstmt = con.prepareCall("{call APPS.KOL_PES_UTIL_PKG.completeReportMaterial(?,?,?,?,?,?,?,?,?,?,?,?)}");
		
		cstmt.setString(1, assetCode);
		cstmt.setString(2, staffNo);
		cstmt.setInt(3, Integer.valueOf(reportType));
		cstmt.setInt(4, Integer.valueOf(wipId));
		cstmt.setString(5, opCode);
		cstmt.setInt(6, Integer.valueOf(seqNum));
		cstmt.setInt(7, Integer.valueOf(itemId));
		cstmt.setDouble(8, Double.valueOf(trxQty));
		cstmt.setString(9, remark);

		cstmt.registerOutParameter(10, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(11, java.sql.Types.VARCHAR);
		
		cstmt.setString(12, schedDate);
		
		cstmt.execute();
		
		DataCodeMessageItem data = new DataCodeMessageItem();
		data.code = cstmt.getInt(10);
		data.message = cstmt.getString(11);
		
		cstmt.close();
		con.close();
		
		LogUtil.log("materialsReportComplete():data.code="+data.code+", data.message="+data.message);
		
		return data;
	}
	
	
}
