package love.wintrue.com.lovestaff.http;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author th
 * @desc 公共接口类
 * @time 2017/3/16 15:55
 */
public interface ApiService {

    @GET
    Observable<ResponseBody> getResponseBody(@HeaderMap Map<String, Object> headers, @Url String url, @QueryMap Map<String, Object> map);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postResponseBody(@HeaderMap Map<String, Object> headers, @Url String url, @FieldMap Map<String, Object> map);

    @Multipart
    @POST("file/pic/upload")
    Call<ResponseBody> uploadFile(@HeaderMap Map<String, Object> headers, @Part("fileName") String description, @Part MultipartBody.Part file);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
