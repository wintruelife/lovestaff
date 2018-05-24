package love.wintrue.com.lovestaff.config;

public class AppConfig {
    /**
     * 应用调试模式
     */
    public static final boolean DEBUG_MODE = true;
    /**
     * clientID
     */
    public static final String CLIENT_ID = "10004";

    /**
     * SD卡文件存储根目录
     */
    public static final String FILE_ROOT_URL = "lovestaffDownlod/";

    /**
     * 基础网络请求url
     */
//    public static final String HTTP_BASE_URL = "http://10.0.2.72:8188/hollycrm-yuntu/";//测试环境 218.106.118.147:8888   http://10.0.2.72:8188
    public static final String HTTP_BASE_URL = "http://182.148.121.104:8188/hollycrm-yuntu/";//公网测试环境
//    public static final String HTTP_BASE_URL = "http://192.168.2.33:8080/hollycrm-yuntu/";//本地测试环境
    /**
     * 应用名称
     */
    public static final String APP_NAME = "lovestaff";

    /**
     * 发送carsh报告
     */
    public static final boolean SEND_CARSH = false;
    /**
     * SD卡文件存储根目录
     */
    public static final String FILE_ROOT_PATH = "lovestaffDownlod/";
    /**
     * 日志保存路径
     */
    public static final String LOG_PATH = FILE_ROOT_PATH + "log/";
    /**
     * 图片保存路径
     */
    public static final String PICTURE_PATH = FILE_ROOT_PATH + "image/";

}
