package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeStartedSeqItem;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetStartedSeqListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String assetId = null;
	
	private List<DataMeStartedSeqItem> seqList;

	@Override
	@Action(value="/get_started_seq_list", results={
			@Result(name="success", location="get_started_seq_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		seqList = mesService.getStartedSeqList(assetId);
		
		LogUtil.log("assetId="+this.assetId);
		
		if(seqList!=null) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("没有查到已开启工序");
			return ERROR;
		}
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public List<DataMeStartedSeqItem> getSeqList() {
		return this.seqList;
	}
	
}
