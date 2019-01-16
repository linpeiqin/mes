/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataAssetInfoItem;
import com.kol.pes.item.DataAvailQuanErrorCodeMsgItem;
import com.kol.pes.service.SeqService;
import com.kol.pes.utils.LogUtil;


public class SeqGetMaxStartQuanAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	
	private String wipEntityName;//工单名称
	private int wipEntityId;//工单id
	private int seqId;//工序id
	private String fmOperationCode;//工序code
	private String curOperationId;//当前正在加工的工序id
	private String canJump;//是否允许回跳工序
	
	private String maxQuan;//最大可投入数
	private List<DataAssetInfoItem> opAssetList;//加工设备列表
	
	@Override
	@Action(value="/seqMaxQuan", results={
			@Result(name="success", location="seq_max_quan.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		if(seqService.dataStatus()!=STATUS_OK) {
			setCode(CODE_FAIL);
			setMessage(getText("common.dataRefreshing"));//服务器正在更新，请稍后操作
			return ERROR;
		}
		
		//int quan = seqService.getLeftCountForStartOp(wipEntityName, wipEntityId, seqId, fmOperationCode, curOperationId, "Y".equals(canJump));
		DataAvailQuanErrorCodeMsgItem errCodeRes = seqService.runProcedureBeforeStartOp(wipEntityId, fmOperationCode);
		LogUtil.log("runProcedureBeforeStartOp:errCodeRes="+errCodeRes.errorCode);

		maxQuan = String.valueOf(errCodeRes.availQty);
		
		LogUtil.log("maxQuan="+maxQuan);
		
		opAssetList = seqService.getOpAssetList(fmOperationCode);
		
		if(opAssetList == null) {
			opAssetList = new ArrayList<DataAssetInfoItem>();
		}
		
		
		if(0 == errCodeRes.errorCode) {
			return SUCCESS;
		}
		else if(2 == errCodeRes.errorCode) {
			setCode(CODE_FAIL);
			setMessage(getText("op.endedOpCanNotBeRestarted"));//此工序已完成不能马上重开
			return ERROR;
		}
		else if(3 == errCodeRes.errorCode) {
			setCode(CODE_FAIL);
			setMessage(getText("op.opCanNotStartWhenUploading"));//上一道工序正在上传，请稍候再试，如超过5分钟仍未成功，请通知IT部门处理
			return ERROR;
		}
		else {
			setCode(CODE_FAIL);
			setMessage(getText("op.opCanNotStartJustNow"));//暂时不能开启工序,请通知IT部门处理
			return ERROR;
		}
	}
	
	public String getMaxQuan() {
		return maxQuan;
	}
	
	public void setWipEntityName(String wipEntityName) {
		this.wipEntityName = wipEntityName;
	}
	
	public void setWipEntityId(String wipEntityId) {
		this.wipEntityId = Integer.valueOf(wipEntityId);
	}
	
	public void setSeqId(String seqId) {
		this.seqId = Integer.valueOf(seqId);
	}
	
	public void setFmOperationCode(String fmOperationCode) {
		this.fmOperationCode = fmOperationCode;
	}
	
	public void setCurOperationId(String curOperationId) {
		this.curOperationId = curOperationId;
	}

	public void setCanJump(String canJump) {
		this.canJump = canJump;
	}
	
	public List<DataAssetInfoItem> getOpAssetList() {
		return opAssetList;
	}

}
