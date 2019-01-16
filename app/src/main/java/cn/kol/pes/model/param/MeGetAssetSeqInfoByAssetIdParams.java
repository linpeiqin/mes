package cn.kol.pes.model.param;

public class MeGetAssetSeqInfoByAssetIdParams extends KoHttpParams {
	
	public MeGetAssetSeqInfoByAssetIdParams(String assetId) {
		
		setParam("uri", "get_asset_seq_info_by_asset_id");
		
		setParam("assetId", assetId);
	}
}
