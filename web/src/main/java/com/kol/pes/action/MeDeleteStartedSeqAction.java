package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeDeleteStartedSeqAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String trxId = null;

	@Override
	@Action(value="/delete_started_seq", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		boolean res = mesService.deleteStartedSeq(trxId);
		
		LogUtil.log("MeDeleteStartedSeqAction:res="+res);
		
		if(res) {
			setMessage("删除工序成功");
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("删除工序失败");
			return ERROR;
		}
	}
	
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	
}
