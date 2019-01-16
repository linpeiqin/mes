package cn.kol.pes.model.param;

public class MeMaterialsReportGetNewNumParams extends KoHttpParams {
	
	public MeMaterialsReportGetNewNumParams(String wipId, String keyWords) {
		
		setParam("uri", "materials_report_get_materials_num");
		
		setParam("type", "2");
		setParam("wipId", wipId);
		setParam("keyWords", keyWords);
	}
}
