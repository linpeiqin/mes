package cn.kol.pes.model.param;

public class MeGetStartedSeqListParams extends KoHttpParams {
	
	public MeGetStartedSeqListParams(String assetId) {
		
		setParam("uri", "get_started_seq_list");
		
		setParam("assetId", assetId);
	}
}
