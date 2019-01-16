package cn.kol.pes.model.param;

import cn.kol.common.util.KoDataUtil;

public class MeTimeReportCompleteParams extends KoHttpParams {
	
	public MeTimeReportCompleteParams(String schedDate, String assetCode, String staffNo, 
							    		String reportType, String wipId, String opCode, 
							    		String seqNum, String activityName, 
							    		String completeNum, String scrapNum, 
							    		String addTime, String addTimeReason, String workClassCode) {
		
		setParam("uri", "time_report_complete");
		
		setParam("schedDate", schedDate);
		setParam("assetCode", assetCode);
		setParam("staffNo", staffNo);
		setParam("reportType", reportType);
		
		setParam("wipId", wipId);
		if(KoDataUtil.isStringNotNull(opCode)) {
			setParam("opCode", opCode);
		}
		if(KoDataUtil.isStringNotNull(seqNum)) {
			setParam("seqNum", seqNum);
		}
		setParam("activityName", activityName);
		setParam("completeNum", completeNum);
		setParam("scrapNum", scrapNum);
		setParam("addTime", addTime);
		if(KoDataUtil.isStringNotNull(addTimeReason)) {
			setParam("addTimeReason", addTimeReason);
		}
		
		setParam("workClassCode", workClassCode);
	}
}
