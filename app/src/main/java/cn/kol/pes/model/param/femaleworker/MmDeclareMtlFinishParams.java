/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.item.femaleworker.MmDeclareMtlItem;
import cn.kol.pes.model.item.femaleworker.MmDeclareTimeItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmDeclareMtlFinishParams extends KoHttpParams {

	public MmDeclareMtlFinishParams(MmDeclareMtlItem item){
		
		setParam("uri", "declare_mtl_finish_4f");
		
		setParam("organizationId", item.organizationId);
		setParam("jobTransactionId", item.jobTransactionId);
		setParam("moveTransactionId", item.moveTransactionId);
		setParam("inventoryItemId", item.inventoryItemId);
		setParam("transactionUom", item.transactionUom);
		setParam("transactionQuantity", item.transactionQuantity);
		setParam("remark", item.remark);
		setParam("transactionItemType", item.transactionItemType);
		setParam("transactionType", item.transactionType);
	}
}
