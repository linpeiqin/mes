package cn.kol.pes.model.param;

public class MeMaterialsReportGetNewMaterialDescParams extends KoHttpParams {
	
	public MeMaterialsReportGetNewMaterialDescParams(String wipId, String itemId) {
		
		setParam("uri", "materials_report_get_new_material_desc");
		
		setParam("wipId", wipId);
		setParam("itemId", itemId);
	}
}
