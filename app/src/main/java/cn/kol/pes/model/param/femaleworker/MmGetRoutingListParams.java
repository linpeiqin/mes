/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetRoutingListParams extends KoHttpParams {

	public MmGetRoutingListParams(){
		setParam("uri", "get_routing_list_4f");
	}
}
