/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.item.femaleworker.MmDeclareTimeItem;
import cn.kol.pes.model.item.femaleworker.MmNonDeclareTimeItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmNonDeclareTimeFinishParams extends KoHttpParams {

	public MmNonDeclareTimeFinishParams(MmNonDeclareTimeItem item){
		
		setParam("uri", "non_declare_time_finish_4f");
		
		setParam("organizationId", item.organizationId);
		setParam("scheduleDate", item.scheduleDate);
		setParam("workGroup", item.workGroup);
		setParam("dayOrNight", item.dayOrNight);
		setParam("groupMaster", item.groupMaster);
		setParam("jobId", item.jobId);
		setParam("wipEntityId", item.wipEntityId);
		setParam("inventoryItemId", item.inventoryItemId);
		setParam("quantity", item.quantity);
		setParam("workTime", item.workTime);
		setParam("reasonCode", item.reasonCode);
		setParam("reasonRemark", item.reasonRemark);
		setParam("goodsQuantity", item.goodsQuantity);
		setParam("goodsWasteQuantity", item.goodsWasteQuantity);


	}
}
