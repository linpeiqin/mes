package cn.kol.pes.model.param;

public class MeGetSeqInfoBySeqIdParams extends KoHttpParams {
	
	public MeGetSeqInfoBySeqIdParams(String assetCode, String wipId) {
		
		setParam("uri", "get_seq_info_by_asset_code");

		setParam("assetCode", assetCode);
		setParam("wipId", wipId);
	}
}
