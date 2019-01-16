/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.item.femaleworker.MmDeclareTimeItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmDeclareTimeFinishParams extends KoHttpParams {

	public MmDeclareTimeFinishParams(MmDeclareTimeItem item){
		
		setParam("uri", "declare_time_finish_4f");
		
		setParam("organizationId", item.organizationId);
		setParam("jobTransactionId", item.jobTransactionId);
		setParam("operationSeqNum", item.operationSeqNum);
		setParam("niOperationCode", item.niOperationCode);
		setParam("niOperationDesc", item.niOperationDesc);
		setParam("assetCode", item.assetCode);
		setParam("quantity", item.quantity);
		setParam("startPullTime", item.startPullTime);
		setParam("endPullTime", item.endPullTime);
		setParam("workTime", item.workTime);
		setParam("transactionUom", item.transactionUom);
		setParam("perQuantity", item.perQuantity);
		setParam("subgoodsQuantity", item.subgoodsQuantity);
		setParam("goodsQuantity", item.goodsQuantity);
		setParam("goodsWasteQuantity", item.goodsWasteQuantity);
		setParam("returnWasteQuantity", item.returnWasteQuantity);
		setParam("inputQuantity", item.inputQuantity);
		setParam("wasteInputQuantity", item.wasteInputQuantity);
		setParam("reasonCode", item.reasonCode);
		setParam("reasonRemark", item.reasonRemark);

	}
}
