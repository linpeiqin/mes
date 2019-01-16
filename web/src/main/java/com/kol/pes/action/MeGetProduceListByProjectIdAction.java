package com.kol.pes.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataMeProduceItem;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetProduceListByProjectIdAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private String projectNum = null;
	
	private List<DataMeProduceItem> produceList;

	@Override
	@Action(value="/get_produce_list_by_project_num", results={
			@Result(name="success", location="get_produce_list_by_project_id.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		produceList = mesService.getProduceListByProjectNum(projectNum);
		
		LogUtil.log("projectNum="+this.projectNum);
		
		if(produceList==null || produceList.size()>0) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取工单列表失败");
			return ERROR;
		}
	}
	
	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}
	
	public List<DataMeProduceItem> getProduceList() {
		return this.produceList;
	}
	
}
