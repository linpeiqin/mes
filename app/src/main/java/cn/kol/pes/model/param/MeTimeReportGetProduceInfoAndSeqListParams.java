package cn.kol.pes.model.param;

public class MeTimeReportGetProduceInfoAndSeqListParams extends KoHttpParams {
	
	public MeTimeReportGetProduceInfoAndSeqListParams(String wipId, String assetId) {
		
		setParam("uri", "time_report_get_produce_info_and_seq_list");
		
		setParam("wipId", wipId);
		setParam("assetId", assetId);
	}
}
