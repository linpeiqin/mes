/*-----------------------------------------------------------

-- PURPOSE

--    登录请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoSetPasswordParams extends KoHttpParams {
	
	public KoSetPasswordParams(String userId, String password){
		
		setParam("uri", "set_password");
		setParam("userId", userId);
		setParam("password", password);
	}
}
