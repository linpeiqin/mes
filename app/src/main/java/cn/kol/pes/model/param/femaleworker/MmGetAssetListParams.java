/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetAssetListParams extends KoHttpParams {

	public MmGetAssetListParams(String staffNo){
		
		setParam("uri", "get_asset_list_4f");
		
		setParam("staffNo", staffNo);
	}
}
