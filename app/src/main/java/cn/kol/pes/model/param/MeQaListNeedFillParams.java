/*-----------------------------------------------------------

-- PURPOSE

--    获取质量管理计划数据项列表的参数类.

-- History

--	  09-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;

public class MeQaListNeedFillParams extends KoHttpParams {
	
	public MeQaListNeedFillParams(String opCode, String orgId){
		
		setParam("uri", "get_qa_list");

		setParam("opCode", opCode);
		setParam("orgId", orgId);
	}
}
