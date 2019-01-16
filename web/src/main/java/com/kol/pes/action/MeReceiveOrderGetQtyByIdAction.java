package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeReceiveOrderGetQtyByIdAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String trxId = null;
	private String staffNum = null;
	
	private String qty;

	@Override
	@Action(value="/receive_order_get_qty_by_id", results={
			@Result(name="success", location="receive_order_get_qty_by_id.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		qty = mesService.receiveOrderGetQtyById(trxId, staffNum);
		
		LogUtil.log("trxId="+this.trxId);
		
		if(qty!=null && qty.length()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取接收数量失败");
			return ERROR;
		}
	}
	
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	
	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}
	
	public String getQty() {
		return this.qty;
	}
	
}
