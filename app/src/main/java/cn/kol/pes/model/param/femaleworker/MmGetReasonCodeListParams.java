/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetReasonCodeListParams extends KoHttpParams {

	public MmGetReasonCodeListParams(){
		setParam("uri", "get_reason_code_list_4f");
	}
}
