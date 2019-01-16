/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.ReasonCodeInfoItem;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;


public class MmNonGetReasonCodeListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	private List<ReasonCodeInfoItem> reasonCodeList;

	@Override
	@Action(value="/get_non_reason_code_list_4f", results={
			@Result(name="success", location="get_reason_code_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		this.reasonCodeList = seqService.getNonReasonCodeList();

		if(reasonCodeList == null) {
			setCode(CODE_FAIL);
			setMessage(getText("获取坏货原因失败"));//获取坏货原因失败

			return ERROR;
		}
		return SUCCESS;
	}

	public List<ReasonCodeInfoItem> getReasonCodeList() {
		return reasonCodeList;
	}
}
