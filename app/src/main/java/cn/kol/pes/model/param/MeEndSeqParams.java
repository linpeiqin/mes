package cn.kol.pes.model.param;

import cn.kol.common.util.KoDataUtil;

public class MeEndSeqParams extends KoHttpParams {
	
	public MeEndSeqParams(String trxId, String wipId, String opCode, String planId, String staffNo, String inputQty, String scrapQty, String endTime, String qaListString, String workClassCode, String seqNum, String schedDate) {
		
		setParam("uri", "end_seq");
		
		setParam("trxId", trxId);
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		
		setParam("planId", planId);
		setParam("staffNo", staffNo);
		if(KoDataUtil.isStringNotNull(inputQty)) {
			setParam("inputQty", inputQty);
		}
		setParam("scrapQty", scrapQty);
		setParam("endTime", endTime);
		setParam("qaListString", qaListString);
		
		setParam("workClassCode", workClassCode);
		setParam("seqNum", seqNum);
		setParam("schedDate", schedDate);
	}
}
