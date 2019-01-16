/*-----------------------------------------------------------

-- PURPOSE

--    获取工序列表的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param.femaleworker;


import cn.kol.pes.model.param.KoHttpParams;

public class MmDeclareJobParams extends KoHttpParams {

	public MmDeclareJobParams(String organizationId,String jobId,String wipEntityId,String inventoryItemId,String scheduleDate,String workGroup,String workMonitor,String workClassCode){
		
		setParam("uri", "declare_job_4f");
		setParam("organizationId", organizationId);
		setParam("jobId", jobId);
		setParam("wipEntityId", wipEntityId);
		setParam("inventoryItemId", inventoryItemId);
		setParam("scheduleDate", scheduleDate);
		setParam("workGroup", workGroup);
		setParam("workMonitor", workMonitor);
		setParam("workClassCode", workClassCode);
	}
}
