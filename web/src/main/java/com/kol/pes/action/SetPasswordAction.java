/*-----------------------------------------------------------

-- PURPOSE

--    登录的Action

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kol.pes.service.LoginService;
import com.kol.pes.utils.LogUtil;


public class SetPasswordAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	private String userId = "";//用户staffNo
	private String password;
	

	@Override
	@Action(value="/set_password", results={
			@Result(name="success", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		boolean res = loginService.setPassword(this.userId, this.password);
		
		LogUtil.log("userId="+this.userId);
		
		if(res) {
			return SUCCESS;
		}
		else {
			setCode(CODE_FAIL);
			setMessage("设置密码失败");
			return ERROR;
		}
	}
	
	//接收客户端传来的staffNo
	public void setUserId(String id) {
		this.userId = id;
	}
	
	public void setPassword(String pwd) {
		this.password = pwd;
	}
	
}
