package love.wintrue.com.lovestaff.http;

import android.util.Log;


import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import love.wintrue.com.lovestaff.utils.StringUtils;


/**
 * 接口
 * @author 
 *
 */
public class BaseInterface3 {
	
	/**手机APP发来接口时需要填写在mobileAppId的标识*/
	protected static String Come_App_Id = "10001";
	
	/**手机APP发来接口时，CRM验签的密钥*/
	protected static String Come_App_Secret = "ba9e69e9ffd2417eb46b369f323f002c";

	/**CRM发往手机APP发请求时需要传的系统标识*/
	protected static String Go_App_Id = "10014";  
		
	/**CRM发往手机APP发请求时加签所用的密钥*/
	protected static String Go_App_Secret = "6396c6d430dd48809fea88fb01d94cce";   
	
	/**接口请求域*/
	//protected static String Urm_App_Domain = "http://10.110.2.171:48081/dee/httpService?action=";
	protected static String Go_App_Domain = "http://127.0.0.1:8080/hollycrm-yuntu/";
	
	/**用户列表*/
	protected static final String App_Test = "fundflow/list";
	
	public static void main(String[] args) {
		System.out.println(md5("loginName=admin&password=1234"+Come_App_Secret));
		System.out.println(md5("loginName=admin1&password=1234"+Come_App_Secret));
		System.out.println(md5("loginName=admin&password=hollycrm"+Come_App_Secret));
		System.out.println(md5("custNo=CUST000BBF&factory=1000&materialId=000000000020100017&qty=3"+Come_App_Secret));
		System.out.println(md5("custNo=CUST000BBF&factory=1000&materialId=000000000020100020&qty=2"+Come_App_Secret));
		System.out.println(md5("custNo=CUST000BBF&page=1&rows=2"+Come_App_Secret));
		System.out.println(md5(null+Come_App_Secret));
		System.out.println(md5(""+Come_App_Secret));
	}
	
	/**
     * 计算签名并组合请求参数
     *	      用于请求Urm接口
     * @param requestParams
     * @param appId
     * @param appSecret
	 * 		post/delete方式不需要带参数：false；
	 * 		get方式需要带参数：true
     * @return
     */
    public static String groupParams(Map<String, Object> requestParams, String appSecret) {

    	StringBuilder paramsBuffer = new StringBuilder("");
        Object[] keys = requestParams.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            String value = (String) requestParams.get(key);
            if (StringUtils.isNotEmpty(value)) {
            	paramsBuffer.append(key + "=" + value + "&");
            }
        }
        String params = "";
        if (paramsBuffer.length() > 0) {
        	params += paramsBuffer.substring(0, paramsBuffer.length() - 1);
        }

        Log.d("hzm","params = "+params);

        String sig = md5(params + appSecret);

        return sig;
    }
    
    /**
     * 签名验证
     * @param requestParams	请求的参数（不包含签名sig、应用编号app_id）
     * @param reqSig	请求的签名
     * @return
     */
    public static boolean validSig(Map<String, String> requestParams, String reqSig){
    	if(StringUtils.isNotEmpty(reqSig))
    		return false;
    	if(requestParams == null){
    		requestParams = new HashMap<String, String>();
    	}
    	//requestParams.put("app_id", CC_App_Id);
    	StringBuilder paramsBuffer = new StringBuilder("");
        Object[] keys = requestParams.keySet().toArray();
        Arrays.sort(keys);
        for (Object key : keys) {
            String value = requestParams.get(key);
            if (!StringUtils.isNotEmpty(value)) {
            	paramsBuffer.append(key + "=" + value + "&");
            }
        }
        String params = "";
        if (paramsBuffer.length() > 0) {
        	params += paramsBuffer.substring(0, paramsBuffer.length() - 1);
        }
        //System.out.println(params + CC_App_Secret);
        String sig = md5(params + Come_App_Secret);
    	return StringUtils.equals(sig, reqSig);
    }
    
    /**
     * md5 加密
     * @param input
     * @return
     */
    public static String md5(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }

        md.update(input.getBytes());
        byte byteData[] = md.digest();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            buffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return buffer.toString();
    }
	
	/**
	 * 获取签名
	 * @param requestParams
	 * @param action
	 * 		post/delete方式不需要带参数：false；
	 * 		get方式需要带参数：true
	 * @return
	 */
    protected String getRequestUrl(Map<String, Object> requestParams, String action){
		if(requestParams == null){
			requestParams = new HashMap<String, Object>();
		}
		
		//请求所需参数
		//requestParams.put("req_time", SystemTimeUtils.getDate(data));
		return Go_App_Domain + action + groupParams(requestParams, Go_App_Secret);
	}


    /*
   * 得到request所有的请求参数，并连接起来
   */
    public static Map getQueryMap(HttpServletRequest request)
            throws Exception {
        HashMap map = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String)paramNames.nextElement();
            String value = request.getParameter(name);
            value = URLDecoder.decode(value, "utf-8");
            map.put(name, value);
        }
        return map;
    }
}
