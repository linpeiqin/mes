/*-----------------------------------------------------------

-- PURPOSE

--    获取工序列表的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.item.femaleworker.MmMainListItem;
import cn.kol.pes.model.param.KoHttpParams;

public class MmGetMainProcessListParams extends KoHttpParams {

	public MmGetMainProcessListParams(MmMainListItem mmMainProcessItem){
		
		setParam("uri", "main_process_list_4f");
		setParam("resCode", mmMainProcessItem.resCode);
		setParam("scheduleDate", mmMainProcessItem.scheduleDate);
		setParam("organizationId", mmMainProcessItem.organizationId);
		setParam("workClassCode", mmMainProcessItem.workClassCode);
		setParam("workGroup", mmMainProcessItem.workGroup);
		setParam("workMonitor", mmMainProcessItem.workMonitor);
	}
}
