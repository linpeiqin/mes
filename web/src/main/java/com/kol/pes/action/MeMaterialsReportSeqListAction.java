package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;
import com.kol.pes.service.MeMaterialsReportService;
import com.kol.pes.utils.LogUtil;


public class MeMaterialsReportSeqListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("materialsReportService")
	private MeMaterialsReportService materialsReportService;
	
	private String wipId = null;
	
	private DataMeTimeReportProduceInfoAndSeqList descInfo;

	@Override
	@Action(value="/materials_report_seq_list", results={
			@Result(name="success", location="materials_report_seq_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		descInfo = materialsReportService.seqListByProduceId(wipId);
		
		LogUtil.log("MeMaterialsReportSeqListAction:wipId="+this.wipId);
		
		if(descInfo!=null && descInfo.getSeqList()!=null) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取工序信息失败");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public DataMeTimeReportProduceInfoAndSeqList getDescInfo() {
		return this.descInfo;
	}
}
