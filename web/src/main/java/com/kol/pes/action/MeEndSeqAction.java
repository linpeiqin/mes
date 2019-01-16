package com.kol.pes.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeProcedure;
import com.kol.pes.item.DataQaReqItem;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeEndSeqAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String trxId = null;
	private String wipId = null;
	private String opCode = null;
	
	private String planId;
	private String staffNo;
	private String inputQty;
	private String scrapQty;
	private String endTime;
	private List<DataQaReqItem> qaDataList;
	private String workClassCode;
	
	private String seqNum = null;
	private String schedDate;
	
	private DataMeProcedure resData;

	@Override
	@Action(value="/end_seq", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		LogUtil.log("MeEndSeqAction:inputQty="+inputQty);
		resData = mesService.endOp(trxId, wipId, opCode, planId, staffNo, inputQty, scrapQty, endTime, qaDataList, workClassCode, seqNum, schedDate);

		LogUtil.log("trxId="+this.trxId);
		
		if(resData!=null && resData.errorCode>=0) {
			if(resData.errorCode>=2) {
				setCode(CODE_FAIL);
				setMessage("完成工序失败:"+resData.errorMsg);
				return ERROR;
			}
			else if(resData.errorCode==1) {
				setMessage("完成工序成功:"+resData.errorMsg);
				return SUCCESS;
			}
			else {
				setMessage("完成工序成功");
				return SUCCESS;
			}
		}
		else {
			setCode(CODE_FAIL);
			setMessage("完成工序失败");
			return ERROR;
		}
	}
	
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public void setInputQty(String inputQty) {
		this.inputQty = inputQty;
	}
	
	public void setScrapQty(String scrapQty) {
		this.scrapQty = scrapQty;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setQaListString(String qaString) {
		this.qaDataList = getNvListByString(qaString);
	}
	
	public void setWorkClassCode(String workClassCode) {
		this.workClassCode = workClassCode;
	}
	
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	
	public void setSchedDate(String schedDate) {
		this.schedDate = schedDate;
	}
	
	public DataMeProcedure getResData() {
		return this.resData;
	}
	
	private List<DataQaReqItem> getNvListByString(String qaNvListString) {
		
		LogUtil.log("qaNvListString="+qaNvListString);
		
		List<DataQaReqItem> qaReqList = new ArrayList<DataQaReqItem>();
		
		if(qaNvListString!=null && qaNvListString.contains(",")) {
			String[] nvListArr = qaNvListString.split("@");
			for(String nv : nvListArr) {
				String[] nvArr = nv.split(",");
				DataQaReqItem qaItem = new DataQaReqItem();
				qaItem.charId = nvArr[0];
				qaItem.resultColumnName = nvArr[1];
				if(nvArr.length >=3) {
					qaItem.value = nvArr[2];
				}
				else {
					qaItem.value = "";
				}
				qaReqList.add(qaItem);
			}
		}
		
		return qaReqList;
	}
	
}
