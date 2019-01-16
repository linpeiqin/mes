package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.item.DataDateShift;
import com.kol.pes.service.MesService;
import com.kol.pes.utils.LogUtil;


public class MeGetDateShiftAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("mesService")
	private MesService mesService;
	
	private DataDateShift resData;

	@Override
	@Action(value="/date_shift", results={
			@Result(name="success", location="date_shift.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		resData = mesService.getDateShift();

		LogUtil.log("MeGetDateShiftAction");
		
		if(resData!=null) {
			
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("获取date shfit失败");
			return ERROR;
		}
	}

	public DataDateShift getResData() {
		return this.resData;
	}
	
}
