/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataAssetInfoItem;
import com.kol.pes.service.AssetCheckService;


public class MeGetAssetListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;
	
	@Autowired
	@Qualifier("assetCheckService")
	private AssetCheckService assetCheckService;
	
	private List<DataAssetInfoItem> assetList;
	private String staffNo;
	
	@Override
	@Action(value="/get_asset_list", results={
			@Result(name="success", location="get_asset_list.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		this.assetList = assetCheckService.meGetAssetList(staffNo);
		
		if(assetList == null) {
			
			setCode(CODE_FAIL);
			setMessage(getText("asset.getAssetInfoFail"));//获取设备信息失败
			
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public List<DataAssetInfoItem> getAssetList() {
		return this.assetList;
	}
	
}
