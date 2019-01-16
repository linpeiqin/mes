package cn.kol.pes.model.param;

import cn.kol.common.util.KoDataUtil;

public class MeTimeReportGetDescInfoWhenStartParams extends KoHttpParams {
	
	public MeTimeReportGetDescInfoWhenStartParams(String assetCode, String schedDate, String reportType, String wipId, String staffNo, String workClassCode, String opCode) {
		
		setParam("uri", "time_report_get_desc_info");
		
		setParam("assetCode", assetCode);
		setParam("schedDate", schedDate);
		setParam("reportType", reportType);
		if(KoDataUtil.isStringNotNull(wipId)) {
			setParam("wipId", wipId);
		}
		
		setParam("staffNo", staffNo);
		setParam("workClassCode", workClassCode);
		setParam("opCode", opCode);
	}
}
