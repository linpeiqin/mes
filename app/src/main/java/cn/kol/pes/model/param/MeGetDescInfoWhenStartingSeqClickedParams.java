package cn.kol.pes.model.param;

public class MeGetDescInfoWhenStartingSeqClickedParams extends KoHttpParams {
	
	public MeGetDescInfoWhenStartingSeqClickedParams(String wipId, String opCode, String assetId, String schedDate, String seqNum) {
		
		setParam("uri", "get_desc_info_when_starting_seq_clicked");
		
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		setParam("assetId", assetId);
		setParam("schedDate", schedDate);
		setParam("seqNum", seqNum);
	}
}
