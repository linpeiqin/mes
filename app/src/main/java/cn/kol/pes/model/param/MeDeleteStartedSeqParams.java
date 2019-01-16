package cn.kol.pes.model.param;

public class MeDeleteStartedSeqParams extends KoHttpParams {
	
	public MeDeleteStartedSeqParams(String trxId) {
		
		setParam("uri", "delete_started_seq");
		
		setParam("trxId", trxId);
	}
}
