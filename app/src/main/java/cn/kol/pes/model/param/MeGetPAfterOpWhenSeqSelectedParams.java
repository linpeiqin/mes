package cn.kol.pes.model.param;

public class MeGetPAfterOpWhenSeqSelectedParams extends KoHttpParams {
	
	public MeGetPAfterOpWhenSeqSelectedParams(String wipId) {
		
		setParam("uri", "get_pafterop_when_seq_selected");
		
		setParam("wipId", wipId);
	}
}
