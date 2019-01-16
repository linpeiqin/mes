package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeSeqInfoData;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetSeqInfoBySeqIdAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String assetCode = null;
	private String wipId = null;
	
	private List<DataMeSeqInfoData> seqList;

	@Override
	@Action(value="/get_seq_info_by_asset_code", results={
			@Result(name="success", location="get_seq_info_by_asset_code.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		seqList = mesService.getSeqInfoBySeqId(assetCode, wipId);
		
		LogUtil.log("assetCode="+this.assetCode);
		
		if(seqList!=null && seqList.size()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取工序信息失败");
			return ERROR;
		}
	}
	
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	
	public void setWipId(String wipId) {
		this.wipId = wipId;
	}
	
	public List<DataMeSeqInfoData> getSeqList() {
		return this.seqList;
	}
	
}
