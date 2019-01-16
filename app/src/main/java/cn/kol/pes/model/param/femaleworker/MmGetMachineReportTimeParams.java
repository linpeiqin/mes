/*-----------------------------------------------------------

-- PURPOSE

--    获取工序列表的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmGetMachineReportTimeParams extends KoHttpParams {

	public MmGetMachineReportTimeParams(String resCode, String scheduleDate, String shift){
		
		setParam("uri", "main_machine_report_time_4f");
		setParam("resCode", resCode);
		setParam("scheduleDate", scheduleDate);
		setParam("shift", shift);
	}
}
