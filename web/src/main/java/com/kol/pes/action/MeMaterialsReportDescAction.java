package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.service.MeMaterialsReportService;
import com.kol.pes.utils.LogUtil;


public class MeMaterialsReportDescAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("materialsReportService")
	private MeMaterialsReportService materialsReportService;
	
	private String wipId = null;
	private String type = "1";
	private String seqNum;
	
	private DataMeTimeReportDescInfoAndActiveListInfo descInfo;

	@Override
	@Action(value="/materials_report_get_desc", results={
			@Result(name="success", location="materials_report_get_desc.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		descInfo = materialsReportService.getDescInfo(wipId, Integer.valueOf(type), seqNum);
		
		LogUtil.log("MeMaterialsReportSeqListAction:wipId="+this.wipId+", type="+type);
		
		if(descInfo!=null && descInfo.display!=null) {
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
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo() {
		return this.descInfo;
	}
}
