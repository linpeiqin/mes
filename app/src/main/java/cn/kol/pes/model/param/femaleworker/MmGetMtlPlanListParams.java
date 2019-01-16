/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetMtlPlanListParams extends KoHttpParams {

	public MmGetMtlPlanListParams(String wipEntityId,String operationSeqNum,String organizationId){
		setParam("uri", "get_mtl_plan_list_4f");
		setParam("wipEntityId", wipEntityId);
		setParam("operationSeqNum", operationSeqNum);
		setParam("organizationId", organizationId);
	}
}
