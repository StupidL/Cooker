package me.stupidme.cooker.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.User;
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
    Observable<User> login(@Query("username") String name, @Query("password") String password);

    /**
     * 注册。使用POST请求，<code>User</code>会被Gson转化为JSON，作为POST请求的BODY
     *
     * @param user 用户信息
     * @return 用户信息
     */
    @POST("/user/register")
    Observable<User> register(@Body User user);

    /**
     * 获取用户所有的设备信息。
     *
     * @param options POST请求体参数
     * @return 设备信息列表
     */
    @GET("/user/devices")
    Observable<List<CookerBean>> getAllDevices(@QueryMap Map options);

    /**
     * 获取具体设备的信息
     *
     * @param deviceId 设备id
     * @return 设备信息
     */
    @GET("/user/devices/{deviceId}")
    Observable<CookerBean> getDevice(@Path("deviceId") int deviceId);

    /**
     * 更新设备信息
     *
     * @param device 设备信息
     * @return 设备信息
     */
    @POST("/user/devices/")
    Observable<CookerBean> updateDevice(@Body CookerBean device);

    /**
     * 增加设备一个信息
     *
     * @param device 设备信息
     * @return 设备
     */
    @POST("/user/devices/")
    Observable<CookerBean> postNewDevice(@Body CookerBean device);

    /**
     * 删除一个设备信息
     *
     * @param deviceId 设备id
     * @return 设备信息
     */
    @DELETE("/user/devices/{id}")
    Observable<CookerBean> deleteDevice(@Path("id") int deviceId);

    /**
     * 获取一个预约的信息
     *
     * @param bookId 预约的id
     * @return 预约信息
     */
    @GET("/user/books/{id}")
    Observable<BookBean> getBookInfo(@Path("id") int bookId);

    /**
     * 获取用户所有的预约信息
     *
     * @param options 请求参数
     * @return 预约列表
     */
    @GET("/user/books")
    Observable<List<BookBean>> getAllBooksInfo(@QueryMap Map options);

    /**
     * 更新预约信息
     *
     * @param bean 预约信息
     * @return 预约信息
     */
    @POST("/user/books")
    Observable<BookBean> updateBook(@Body BookBean bean);

    /**
     * 新增一个预约
     *
     * @param book 请求体参数
     * @return 预定信息
     */
    @POST("/user/books")
    Observable<BookBean> postNewBook(@Body BookBean book);

    /**
     * 删除一个预约信息
     *
     * @param bookId 预约信息
     * @return 预约信息
     */
    @DELETE("/user/books/{id}")
    Observable<BookBean> deleteBook(@Path("id") int bookId);
}
