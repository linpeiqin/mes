package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeSearchSeqItem;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeSearchSeqListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String wipId = null;
	
	private List<DataMeSearchSeqItem> seqList;

	@Override
	@Action(value="/search_seq_list", results={
			@Result(name="success", location="search_seq_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		seqList = mesService.searchSeqList(wipId);
		
		LogUtil.log("MeSearchSeqListAction:wipId="+this.wipId);
		
		if(seqList==null || seqList.size()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("未查询到工序数据");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public List<DataMeSearchSeqItem> getSeqList() {
		return this.seqList;
	}
	
}
