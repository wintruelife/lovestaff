package love.wintrue.com.lovestaff.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3DES加密
 * @author rsh 2016-04-20
 *
 */
public class Des3 {

	//默认编码
	private final static String DEFAULT_CHARSET = "UTF-8";
	//加密算法
	private final static String ALGORITHM = "DESede";
	//填充方式
	private final static String PADDING = "PKCS5Padding";
    
    /**
     * ECB加密,不要IV
     * @param key 密钥
     * @param data 明文
     * @return 密文
     * @throws Exception
     */
    public static String des3EncodeECB(String key, String data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes(DEFAULT_CHARSET));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(ALGORITHM +"/ECB/"+ PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        return Base64.encode(cipher.doFinal(data.getBytes(DEFAULT_CHARSET)));
    }
    
    /**
     * ECB解密,不要IV
     * @param keyStr 密钥
     * @param data 密文
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeECB(String keyStr, String data)
            throws Exception {
        return des3DecodeECB(keyStr, Base64.decode(data));
    }
    
    /**
     * ECB解密,不要IV
     * @param key 密钥
     * @param data 密文
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeECB(String key, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes(DEFAULT_CHARSET));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(ALGORITHM + "/ECB/"+ PADDING);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return new String(bOut, DEFAULT_CHARSET);
    }
    
    
    /**
     * CBC加密
     * @param keyStr 密钥
     * @param keyivStr IV
     * @param data 明文
     * @return 密文
     * @throws Exception
     */
    public static String des3EncodeCBC(String keyStr, String keyivStr, String data)
            throws Exception {
        return des3EncodeCBC(keyStr.getBytes(DEFAULT_CHARSET),
        				keyivStr.getBytes(DEFAULT_CHARSET), 
        				data.getBytes(DEFAULT_CHARSET));
    }
    
    /**
     * CBC加密
     * @param key 密钥
     * @param keyivStr IV
     * @param data 明文
     * @return 密文
     * @throws Exception
     */
    public static String des3EncodeCBC(byte[] key, String keyivStr, String data)
            throws Exception {
        return des3EncodeCBC(key,
        				keyivStr.getBytes(DEFAULT_CHARSET), 
        				data.getBytes(DEFAULT_CHARSET));
    }
    
    /**
     * CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return 密文
     * @throws Exception
     */
    public static String des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(ALGORITHM+ "/CBC/"+ PADDING);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return Base64.encode(bOut);
    }
    
    /**
     * CBC解密
     * @param keyStr 密钥
     * @param keyivStr IV
     * @param data 密文
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeCBC(String keyStr, String keyivStr, String data)
            throws Exception {
        return des3DecodeCBC(keyStr, keyivStr, Base64.decode(data));
    }
    
    
    /**
     * CBC解密
     * @param keyStr 密钥
     * @param keyivStr IV
     * @param data 密文BASE64解码字节数组
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeCBC(String keyStr, String keyivStr, byte[] data)
            throws Exception {
        return des3DecodeCBC(keyStr.getBytes(DEFAULT_CHARSET), 
        				keyivStr.getBytes(DEFAULT_CHARSET),
        				data);
    }
    
    /**
     * CBC解密
     * @param key 密钥
     * @param keyiv IV
     * @param data 密文BASE64解码字节数组
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
        deskey = keyfactory.generateSecret(spec);
        
        Cipher cipher = Cipher.getInstance(ALGORITHM+ "/CBC/"+ PADDING);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        return new String(cipher.doFinal(data), DEFAULT_CHARSET);
    }
    
    /**
    * 去掉加密字符串换行符
    * @param str
    * @return
    */
    public static String filter(String str) {
    	if(str==null) return null;
    	String output = "";
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < str.length(); i++) {
    		int asc = str.charAt(i);
			if (asc != 10 && asc != 13) {
				sb.append(str.subSequence(i, i+1));
			}
    	}
    	output = new String(sb);
    	return output;
    }
}
