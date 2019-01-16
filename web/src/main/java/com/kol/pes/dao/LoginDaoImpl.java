/*-----------------------------------------------------------

-- PURPOSE

--    处理登录的数据库操作类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package com.kol.pes.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.kol.pes.item.DataLoginItem;
import com.kol.pes.utils.CommonUtil;
import com.kol.pes.utils.LogUtil;


@Repository("loginDao")
public class LoginDaoImpl implements LoginDao {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	//登录，返回登录人的信息
	public DataLoginItem login(String userId, String password) throws SQLException {
		if(CommonUtil.isValidNumber(userId)) {
			QueryRunner runner = new QueryRunner(this.dataSource);
			String selectUserSql = "select staff_no, staff_name, pwd,attribute12 from kol_pes_staffs where staff_no = '"+userId+"'";
			LogUtil.log("LoginDaoImpl:selectUserSql="+selectUserSql);
			return runner.query(selectUserSql, new ResultSetHandler<DataLoginItem>() {
	
				public DataLoginItem handle(ResultSet rs) throws SQLException {
					if (rs == null) {
			            return null;
			        }
					while(rs.next()) {
						DataLoginItem user = new DataLoginItem();
						user.staffNo = rs.getString("STAFF_NO");
						user.staffName = CommonUtil.noNullString(rs.getString("STAFF_NAME"));
						user.pwd = rs.getString("PWD");
						user.type = rs.getString("attribute12");
						LogUtil.log("user.staffName="+user.staffName+", user.pwd="+user.pwd);
						return user;
					}
					return null;
				}
			});
		}
		return null;
	}
	
	public boolean setPassword(String userId, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(this.dataSource);
		String sql = "update apps.per_all_people_f set attribute2 = '"+password+"' where employee_number = '"+userId+"' and nvl(effective_end_date,sysdate) > sysdate";
		int res = runner.update(sql);
		return res > 0;
	}

}
