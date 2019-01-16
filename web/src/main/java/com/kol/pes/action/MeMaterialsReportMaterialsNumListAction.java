package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeMaterialsNumItem;
import com.kol.pes.service.MeMaterialsReportService;
import com.kol.pes.utils.LogUtil;


public class MeMaterialsReportMaterialsNumListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("materialsReportService")
	private MeMaterialsReportService materialsReportService;
	
	private String type = null;
	private String wipId = null;
	private String seqNum = null;
	private String keyWords = null;
	
	private List<DataMeMaterialsNumItem> materialsNumList;

	@Override
	@Action(value="/materials_report_get_materials_num", results={
			@Result(name="success", location="materials_report_get_materials_num.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		materialsNumList = materialsReportService.getMaterialsNum(type, wipId, seqNum, keyWords);
		
		LogUtil.log("MeMaterialsReportSeqListAction:wipId="+this.wipId);
		
		if(materialsNumList!=null && materialsNumList.size()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取物料信息失败");
			return ERROR;
		}
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	public List<DataMeMaterialsNumItem> getMaterialsNumList() {
		return this.materialsNumList;
	}
}
