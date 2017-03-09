package me.stupidme.cooker.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface CookerService {

    String BASE_URL = "http://121.42.47.110:8080/";

    /**
     * 登陆。使用GET请求，会把<code>name</code>和<code>password</code>拼接在URL后面
     *
     * @param name     用户名
     * @param password 密码
     * @return 用户信息
     */
    @GET("/user/login")
    Observable<User> rxLogin(@Query("username") String name, @Query("password") String password);

    @GET("/user/login")
    Call<User> retrofitLogin(@Query("username") String name, @Query("password") String password);

    /**
     * 注册。使用POST请求，<code>User</code>会被Gson转化为JSON，作为POST请求的BODY
     *
     * @param user 用户信息
     * @return 用户信息
     */
    @POST("/user/register")
    Observable<User> rxRegister(@Body User user);

    @POST("/user/register")
    Call<User> retrofitRegister(@Body User user);

    /**
     * 获取用户所有的设备信息。
     *
     * @param options POST请求体参数
     * @return 设备信息列表
     */
    @GET("/user/devices")
    Observable<List<CookerBean>> rxGetAllDevices(@QueryMap Map<String, String> options);

    @GET("/user/devices")
    Call<List<CookerBean>> retrofitGetAllDevices(@QueryMap Map<String, String> options);

    /**
     * 获取具体设备的信息
     *
     * @param deviceId 设备id
     * @return 设备信息
     */
    @GET("/user/devices/{deviceId}")
    Observable<CookerBean> rxGetDevice(@Path("deviceId") int deviceId);

    @GET("/user/devices/{deviceId}")
    Call<CookerBean> retrofitGetDevice(@Path("deviceId") int deviceId);

    /**
     * 更新设备信息
     *
     * @param device 设备信息
     * @return 设备信息
     */
    @POST("/user/devices/")
    Observable<CookerBean> rxUpdateDevice(@Body CookerBean device);

    @POST("/user/devices/")
    Call<CookerBean> retrofitUpdateDevice(@Body CookerBean device);

    /**
     * 增加设备一个信息
     *
     * @param device 设备信息
     * @return 设备
     */
    @POST("/user/devices/")
    Observable<CookerBean> rxPostNewDevice(@Body CookerBean device);

    @POST("/user/devices/")
    Call<CookerBean> retrofitPostNewDevice(@Body CookerBean device);

    /**
     * 删除一个设备信息
     *
     * @param deviceId 设备id
     * @return 设备信息
     */
    @DELETE("/user/devices/{id}")
    Observable<CookerBean> rxDeleteDevice(@Path("id") int deviceId);

    @DELETE("/user/devices/{id}")
    Observable<CookerBean> rxDeleteDevice(@Body CookerBean device);

    @DELETE("/user/devices/{id}")
    Call<CookerBean> retrofitDeleteDevice(@Path("id") int deviceId);

    /**
     * 获取一个预约的信息
     *
     * @param bookId 预约的id
     * @return 预约信息
     */
    @GET("/user/books/{id}")
    Observable<BookBean> rxGetBookInfo(@Path("id") int bookId);

    @GET("/user/books/{id}")
    Call<BookBean> retrofitGetBookInfo(@Path("id") int bookId);

    /**
     * 获取用户所有的预约信息
     *
     * @param options 请求参数
     * @return 预约列表
     */
    @GET("/user/books")
    Observable<List<BookBean>> rxGetAllBooksInfo(@QueryMap Map<String, String> options);

    @GET("/user/books")
    Call<List<BookBean>> retrofitGetAllBooksInfo(@QueryMap Map<String, String> options);

    /**
     * 更新预约信息
     *
     * @param bean 预约信息
     * @return 预约信息
     */
    @POST("/user/books")
    Observable<BookBean> rxGpdateBook(@Body BookBean bean);

    @POST("/user/books")
    Call<BookBean> retrofitGpdateBook(@Body BookBean bean);

    /**
     * 新增一个预约
     *
     * @param book 请求体参数
     * @return 预定信息
     */
    @POST("/user/books")
    Observable<BookBean> rxPostNewBook(@Body BookBean book);

    @POST("/user/books")
    Call<BookBean> retrofitPostNewBook(@Body BookBean book);

    /**
     * 删除一个预约信息
     *
     * @param bookId 预约信息
     * @return 预约信息
     */
    @DELETE("/user/books/{id}")
    Observable<BookBean> rxDeleteBook(@Path("id") int bookId);

    @DELETE("/user/books/{id}")
    Call<BookBean> retrofitDeleteBook(@Path("id") int bookId);

}
