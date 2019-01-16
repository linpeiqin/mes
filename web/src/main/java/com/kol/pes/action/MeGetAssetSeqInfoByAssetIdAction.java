package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeAssetSeqInfo;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetAssetSeqInfoByAssetIdAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String assetId = null;
	
	private DataMeAssetSeqInfo assetInfo;

	@Override
	@Action(value="/get_asset_seq_info_by_asset_id", results={
			@Result(name="success", location="get_asset_seq_info_by_asset_id.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		assetInfo = mesService.getAssetSeqInfoByAssetId(assetId);
		
		LogUtil.log("assetId="+this.assetId);
		
		if(assetInfo!=null && assetInfo.resourceCode!=null) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取设备信息失败");
			return ERROR;
		}
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public DataMeAssetSeqInfo getAssetInfo() {
		return this.assetInfo;
	}
	
}
