/*-----------------------------------------------------------

-- PURPOSE

--    登录请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoLoginParams extends KoHttpParams {
	
	public KoLoginParams(String userId, String password){
		
		setParam("uri", "login");
		setParam("userId", userId);
		if(password!=null && password.trim().length()>0) {
			setParam("password", password);
		}
	}
}
