package cn.kol.pes.model.param;

import cn.kol.common.util.KoDataUtil;

public class MeMaterialsReportCompleteParams extends KoHttpParams {
	
	public MeMaterialsReportCompleteParams(String assetCode, String staffNo, String reportType, 
											String wipId, String opCode, String seqNum, 
											String itemId, String trxQty, String remark, String schedDate) {
		
		setParam("uri", "materials_report_complete");
		
		setParam("assetCode", assetCode);
		setParam("staffNo", staffNo);
		setParam("reportType", reportType);
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		setParam("seqNum", seqNum);
		setParam("itemId", itemId);
		setParam("trxQty", trxQty);
		if(KoDataUtil.isStringNotNull(remark)) {
			setParam("remark", remark);
		}
		setParam("schedDate", schedDate);
	}
}
