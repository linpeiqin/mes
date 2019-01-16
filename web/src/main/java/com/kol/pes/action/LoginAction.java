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

import com.kol.pes.item.DataLoginItem;
import com.kol.pes.service.LoginService;
import com.kol.pes.utils.LogUtil;


public class LoginAction extends ParentAction {

	private static final long serialVersionUID = 4424087712369555098L;

	@Autowired
	@Qualifier("loginService")
	private LoginService loginService;
	
	private String userId = "";//用户staffNo
	private String password = null;
	private DataLoginItem loginData;//登陆后的数据
	
	private final String CODE_NEED_SET_PWD = "2";
	
	@Override
	@Action(value="/login", results={
			@Result(name="success", location="login.ftl", type="freemarker", params={"contentType", "text/xml"}),
			@Result(name="error", location="code_message.ftl", type="freemarker", params={"contentType", "text/xml"})
	})
	
	public String execute() throws Exception {
		
		loginData = loginService.login(this.userId, this.password);
		
		LogUtil.log("userId="+this.userId);
		
		if(loginData==null || loginData.staffName==null) {
			setCode(CODE_FAIL);
			setMessage(getText("common.loginNoThisStaff")+this.userId);//没有此员工的记录:
			return ERROR;
		}
		else if(loginData.pwd==null || loginData.pwd.trim().length()==0) {
			setCode(CODE_NEED_SET_PWD);
			setMessage("需要设置密码");
			return ERROR;
	}
		else if(password==null || !loginData.pwd.equals(password)) {
			setCode(CODE_FAIL);
			setMessage("密码错误");
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	//接收客户端传来的staffNo
	public void setUserId(String id) {
		this.userId = id;
	}
	
	public void setPassword(String pwd) {
		this.password = pwd;
	}
	
	public DataLoginItem getLoginData() {
		return this.loginData;
	}
	
}
