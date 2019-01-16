package cn.kol.pes.model.param;

public class MeStartSeqParams extends KoHttpParams {
	
	public MeStartSeqParams(String staffNo, String inputQty, String startOpTime, String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String seqNum, String pafteropSeqNum, String workClassCode) {
		
		setParam("uri", "start_seq");
		
		setParam("staffNo", staffNo);
		setParam("inputQty", inputQty);
		setParam("startOpTime", startOpTime);
		
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		setParam("assetId", assetId);
		setParam("schedDate", schedDate);
		setParam("pAfterOp", pAfterOp);
		setParam("seqNum", seqNum);
		setParam("pafteropSeqNum", pafteropSeqNum);
		setParam("workClassCode", workClassCode);
		
	}
}
