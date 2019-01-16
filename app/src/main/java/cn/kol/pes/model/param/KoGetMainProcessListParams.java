/*-----------------------------------------------------------

-- PURPOSE

--    获取工序列表的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoGetMainProcessListParams extends KoHttpParams {
	
	public KoGetMainProcessListParams(String resCode, String scheduleDate, String shift){
		
		setParam("uri", "main_process_list");
		setParam("resCode", resCode);
		setParam("scheduleDate", scheduleDate);
		setParam("shift", shift);
	}
}
