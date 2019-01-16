/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetGroupListParams extends KoHttpParams {

	public MmGetGroupListParams(String assetNumber){
		
		setParam("uri", "get_group_list_4f");
		
		setParam("assetNumber", assetNumber);
	}
}
