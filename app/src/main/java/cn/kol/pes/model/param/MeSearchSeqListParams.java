package cn.kol.pes.model.param;

public class MeSearchSeqListParams extends KoHttpParams {
	
	public MeSearchSeqListParams(String wipId) {
		
		setParam("uri", "search_seq_list");
		
		setParam("wipId", wipId);
	}
}
