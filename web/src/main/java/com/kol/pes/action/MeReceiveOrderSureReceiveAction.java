package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeReceiveOrderSureReceiveAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String trxId = null;
	private String staffNo = null;
	private String qty;

	@Override
	@Action(value="/receive_order_receive", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		boolean res = mesService.receiveOrderSureReceive(trxId, qty, staffNo);
		
		LogUtil.log("trxId="+this.trxId);
		
		if(res) {
			setMessage("接收或退数成功");
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("接收或退数失败");
			return ERROR;
		}
	}
	
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	
	public void setQty(String qty) {
		this.qty = qty;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
}
