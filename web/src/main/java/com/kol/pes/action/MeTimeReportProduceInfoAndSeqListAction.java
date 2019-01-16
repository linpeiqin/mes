package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeTimeReportProduceInfoAndSeqList;
import com.kol.pes.service.MeTimeReportService;
import com.kol.pes.utils.LogUtil;


public class MeTimeReportProduceInfoAndSeqListAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("timeReportService")
	private MeTimeReportService timeReportService;
	
	private String wipId = null;
	private String assetId = null;
	
	private DataMeTimeReportProduceInfoAndSeqList descInfo;

	@Override
	@Action(value="/time_report_get_produce_info_and_seq_list", results={
			@Result(name="success", location="time_report_produce_info_and_seq_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		descInfo = timeReportService.timeReportGetProduceInfoAndSeqListByProduceId(wipId, assetId);
		
		LogUtil.log("MeTimeReportProduceInfoAndSeqListAction:wipId="+this.wipId);
		
		if(descInfo!=null && descInfo.getSeqList()!=null) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取信息失败");
			return ERROR;
		}
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public DataMeTimeReportProduceInfoAndSeqList getDescInfo() {
		return this.descInfo;
	}
}
