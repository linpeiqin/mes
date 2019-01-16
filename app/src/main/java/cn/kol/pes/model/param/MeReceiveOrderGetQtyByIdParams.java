package cn.kol.pes.model.param;

public class MeReceiveOrderGetQtyByIdParams extends KoHttpParams {
	
	public MeReceiveOrderGetQtyByIdParams(String id, String staffNum) {
		
		setParam("uri", "receive_order_get_qty_by_id");
		
		setParam("trxId", id);
		setParam("staffNum", staffNum);
	}
}
