/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障类型的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action.femaleworker;

import com.kol.pes.action.ParentAction;
import com.kol.pes.item.femaleworker.RoutingInfoItem;
import com.kol.pes.service.SeqService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;


public class MmGetRoutingListAction extends ParentAction {

	
	private static final long serialVersionUID = 4426087712869585099L;

	@Autowired
	@Qualifier("seqService")
	private SeqService seqService;
	private List<RoutingInfoItem> routingList;

	@Override
	@Action(value="/get_routing_list_4f", results={
			@Result(name="success", location="get_routing_list_4f.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
	//	this.routingList = seqService.getFemaleRouting();
		//女工工序不再从数据库中获取，而是直接按照用户要求只有G3，G4，G3代表半成品，G4代表成品
		routingList = new ArrayList<RoutingInfoItem>();
		RoutingInfoItem routingInfoItem = new RoutingInfoItem();
		routingInfoItem.routingCode = "G3";
		routingInfoItem.routingName = "G3";
		routingInfoItem.routingType = "G3";

		RoutingInfoItem routingInfoItem1 = new RoutingInfoItem();
		routingInfoItem1.routingCode = "G4";
		routingInfoItem1.routingName = "G4";
		routingInfoItem1.routingType = "G4";
		routingList.add(routingInfoItem);
		routingList.add(routingInfoItem1);
		if(routingList == null) {
			setCode(CODE_FAIL);
			setMessage(getText("获取女工细工序失败"));//获取女工细工序失败
			return ERROR;
		}
		return SUCCESS;
	}

	public List<RoutingInfoItem> getRoutingList() {
		return routingList;
	}
}
