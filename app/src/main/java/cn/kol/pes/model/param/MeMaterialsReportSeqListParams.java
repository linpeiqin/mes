package cn.kol.pes.model.param;

public class MeMaterialsReportSeqListParams extends KoHttpParams {
	
	public MeMaterialsReportSeqListParams(String wipId) {
		
		setParam("uri", "materials_report_seq_list");
		
		setParam("wipId", wipId);
	}
}
