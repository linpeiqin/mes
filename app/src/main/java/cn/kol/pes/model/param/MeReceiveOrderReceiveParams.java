package cn.kol.pes.model.param;

public class MeReceiveOrderReceiveParams extends KoHttpParams {
	
	public MeReceiveOrderReceiveParams(String id, String qty, String staffNo) {
		
		setParam("uri", "receive_order_receive");
		
		setParam("trxId", id);
		setParam("qty", qty);
		setParam("staffNo", staffNo);
	}
}
