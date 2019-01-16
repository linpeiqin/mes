/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetWipListParams extends KoHttpParams {

	public MmGetWipListParams(String jobNo,String organizationId){
		
		setParam("uri", "get_wip_list_4f");

		setParam("jobNo", jobNo);
		setParam("organizationId", organizationId);

	}
}
