package cn.kol.pes.model.param;

public class MeReceiveOrderRejectParams extends KoHttpParams {
	
	public MeReceiveOrderRejectParams(String id, String staffNo) {
		
		setParam("uri", "receive_order_reject");
		
		setParam("trxId", id);
		setParam("staffNo", staffNo);
	}
}
