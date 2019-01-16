package cn.kol.pes.model.param;

public class MeMaterialsReportGetStandardNumParams extends KoHttpParams {
	
	public MeMaterialsReportGetStandardNumParams(String wipId, String seqNum) {
		
		setParam("uri", "materials_report_get_materials_num");
		
		setParam("type", "1");
		setParam("wipId", wipId);
		setParam("seqNum", seqNum);
	}
}
