package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataQaNeedFillItem;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetQaListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String opCode = null;
	private String orgId = null;
	
	private List<DataQaNeedFillItem> qaList;

	@Override
	@Action(value="/get_qa_list", results={
			@Result(name="success", location="get_qa_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		qaList = mesService.getQaList(opCode, orgId);
		
		LogUtil.log("opCode="+opCode+", orgId="+orgId);
		
		if(qaList==null || qaList.size()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取质量管理计划列表失败");
			return ERROR;
		}
	}
	
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public List<DataQaNeedFillItem> getQaList() {
		return this.qaList;
	}
	
}
