package cn.kol.pes.model.param;

public class MeMaterialsReportGetDescParams extends KoHttpParams {
	
	public MeMaterialsReportGetDescParams(String wipId, String type, String seqNum) {
		
		setParam("uri", "materials_report_get_desc");
		
		setParam("wipId", wipId);
		setParam("type", type);
		setParam("seqNum", seqNum);
	}
}
