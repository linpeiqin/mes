package cn.kol.common.util;

public class Constants {
	
	public static final boolean IS_USE_TEST = true;//android
    
    public static final boolean IS_OPEN_LOG = true;//日志开android
    
    private static final String KO_SERVER_URL_RELEASE = "http://10.0.2.128:8080/meserp/"; //NI PROD Web Server

  //  private static final String KO_SERVER_URL_TEST = "http://192.168.1.213:8080/meserp/";	//NI Test Web Server
    private static final String KO_SERVER_URL_TEST = "http://192.168.33.242:8080/meserp/";	//Local Test Web Server

    //private static final String KO_SERVER_URL_TEST = "http://192.168.1.3:8080/mes_erp/";	// LI 服务器地址
    //private static final String KO_SERVER_URL_TEST = "http://192.168.1.104:8080/mes_erp/";	//Felix's home server
    //private static final String KO_SERVER_URL_TEST = "http://192.168.57.181:8080/mes_erp/";	//Felix's HZ server
    //private static final String KO_SERVER_URL_TEST = "http://192.168.1.87:8080/mes_erp/";	//Felix's HK office server
     
    
    public static String getServerUrl() {
    	return IS_USE_TEST ? KO_SERVER_URL_TEST : KO_SERVER_URL_RELEASE;
    }
}
