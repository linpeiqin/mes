/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class MeGetAssetInfoParams extends KoHttpParams {
	public MeGetAssetInfoParams(String staffNo, String resCode){
		
		setParam("uri", "get_asset_info");
		
		setParam("staffNo", staffNo);
		setParam("resCode", resCode);
	}
}
