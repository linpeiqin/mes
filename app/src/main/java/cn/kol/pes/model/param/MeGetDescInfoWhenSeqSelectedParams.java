package cn.kol.pes.model.param;

public class MeGetDescInfoWhenSeqSelectedParams extends KoHttpParams {
	
	public MeGetDescInfoWhenSeqSelectedParams(String wipId, String opCode, String assetId, String schedDate, String pAfterOp, String staffNo, String seqNum) {
		
		setParam("uri", "get_desc_info_when_seq_selected");
		
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		setParam("assetId", assetId);
		setParam("schedDate", schedDate);
		setParam("pAfterOp", pAfterOp);
		setParam("staffNo", staffNo);
		setParam("seqNum", seqNum);
	}
}
