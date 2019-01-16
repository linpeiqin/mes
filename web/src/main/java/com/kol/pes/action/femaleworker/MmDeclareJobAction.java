/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.DataAssetInfoItem;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmDeclareJobAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String declareJobId;


	private String organizationId;
	private String jobId;
	private String wipEntityId;
	private String inventoryItemId;
	private String scheduleDate;
	private String workGroup;
	private String workMonitor;
	private String workClassCode;

	
	@Override
	@Action(value="/declare_job_4f", results={
			@Result(name="success", location="declare_job_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		String codes[] = seqService.handworkDeclareJob(organizationId,jobId,wipEntityId,inventoryItemId,scheduleDate,workGroup,workMonitor,workClassCode);
		Integer codeI = Integer.valueOf(codes[0]);
		String  codeMsg = codes[1];
		if(codeI < 0) {
			setCode(CODE_FAIL);
			setMessage(getText(codeMsg));
			return ERROR;
		}
		this.declareJobId = String.valueOf(codeI);
		return SUCCESS;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setWipEntityId(String wipEntityId) {
		this.wipEntityId = wipEntityId;
	}

	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public void setWorkGroup(String workGroup) {
		this.workGroup = workGroup;
	}

	public void setWorkMonitor(String workMonitor) {
		this.workMonitor = workMonitor;
	}

	public void setWorkClassCode(String workClassCode) {
		this.workClassCode = workClassCode;
	}

	public String getDeclareJobId() {
		return declareJobId;
	}
}
