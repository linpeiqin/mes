package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeTimeReportDescInfoAndActiveListInfo;
import com.kol.pes.service.MeMaterialsReportService;
import com.kol.pes.utils.LogUtil;


public class MeMaterialsReportMaterialsDescAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("materialsReportService")
	private MeMaterialsReportService materialsReportService;
	
	private String wipId = null;
	private String itemId = null;
	
	private DataMeTimeReportDescInfoAndActiveListInfo descInfo;

	@Override
	@Action(value="/materials_report_get_new_material_desc", results={
			@Result(name="success", location="materials_report_get_desc.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		descInfo = materialsReportService.getMaterialsDescInfo(wipId, itemId);
		
		LogUtil.log("MeMaterialsReportMaterialsDescAction:wipId="+this.wipId);
		
		if(descInfo!=null && descInfo.display!=null) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取新增物料 "+itemId+" 信息失败");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public DataMeTimeReportDescInfoAndActiveListInfo getDescInfo() {
		return this.descInfo;
	}
}
