/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class MeGetAssetListParams extends KoHttpParams {
	
	public MeGetAssetListParams(String staffNo){
		
		setParam("uri", "get_asset_list");
		
		setParam("staffNo", staffNo);
	}
}
