package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MePafteropListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String wipId = null;
	
	private List<DataMeSeqInfoData> pafteropList;

	@Override
	@Action(value="/get_pafterop_when_seq_selected", results={
			@Result(name="success", location="get_pafterop_when_seq_selected.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		pafteropList = mesService.getPafteropList(wipId);
		
		LogUtil.log("MePafteropListAction:wipId="+this.wipId);
		
		if(pafteropList!=null&& pafteropList.size()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("没有查p after op");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public List<DataMeSeqInfoData> getPafteropList() {
		return this.pafteropList;
	}
	
}
