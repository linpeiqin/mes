/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetMtlNewListParams extends KoHttpParams {

	public MmGetMtlNewListParams(String wipEntityId, String mtlNo, String organizationId){
		setParam("uri", "get_mtl_new_list_4f");
		setParam("wipEntityId", wipEntityId);
		setParam("mtlNo", mtlNo);
		setParam("organizationId", organizationId);
	}
}
