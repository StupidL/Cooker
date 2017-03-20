package me.stupidme.cooker.retrofit;

import java.util.List;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 该类定义了所有网络请求所用的接口。
 * Created by StupidL on 2017/3/8.
 */

public interface CookerService {

    String BASE_URL = "http://121.42.47.110:8080/";

    /**
     * 用户登录接口，发送POST请求，带有<code>username</code>和<code>password</code>两个域。
     * 因为返回结果的<code>data</code>是一个JSON数组，所以该方法的返回值是一个List。
     *
     * @param name     用户名
     * @param password 密码
     * @return HttpResult对象，包含了一个UserBean的列表
     * @see HttpResult
     * @see UserBean
     */
    @POST("/user/login")
    Observable<HttpResult<List<UserBean>>> login(@Field("username") String name,
                                                 @Field("password") String password);

    /**
     * 用户注册接口，发送POST请求，将UserBean作为参数，实际上会把该参数的域逐个解析成POST请求的field。
     * 因为返回结果的<code>data</code>是一个JSON数组，所以该方法的返回值是一个List。
     *
     * @param user 用户
     * @return HttpResult对象，包含了一个UserBean的列表，实际上只有一i个UserBean对象，该对象的<code>userId</code>是唯一确定的。
     * @see HttpResult
     * @see UserBean
     */
    @POST("/user/register")
    Observable<HttpResult<List<UserBean>>> register(@Body UserBean user);

    /**
     * 用户注册接口，发送POST请求，带有<code>username</code>和<code>password</code>两个域。
     * 因为返回结果的<code>data</code>是一个JSON数组，所以该方法的返回值是一个List。
     *
     * @param name 用户名
     * @return HttpResult对象，包含了一个UserBean的列表，实际上只有一i个UserBean对象，该对象的<code>userId</code>是唯一确定的。
     * @see HttpResult
     * @see UserBean
     */
    @POST("/user/register")
    Observable<HttpResult<List<UserBean>>> register(@Field("username") String name,
                                                    @Field("password") String password);

    /**
     * 用户获取所有设备信息的接口，发送GET请求，只需要<code>userId</code>一个参数。
     * 考虑到安全因素，后期可能会加上一定的安全手段。
     *
     * @param userId 用户ID
     * @return HttpResult对象，包含一个该用户所有的设备信息列表
     * @see HttpResult
     * @see CookerBean
     */
    @GET("/user/{userId}/cookers")
    Observable<HttpResult<List<CookerBean>>> queryCookers(@Path("userId") long userId);

    /**
     * 用户获取某个电饭锅设备的接口，发送GET请求，需要<code>userId</code>和<code>cookerId</code>两个参数
     *
     * @param userId   用户ID
     * @param cookerId 设备ID
     * @return HttpResult对象，包含一个设备信息的列表，该列表实际上最多只有一项
     * @see HttpResult
     * @see CookerBean
     */
    @GET("/user/{userId}/cookers/{cookerId}")
    Observable<HttpResult<List<CookerBean>>> queryCooker(@Path("userId") long userId,
                                                         @Path("cookerId") long cookerId);

    /**
     * 更新某个电饭锅设备的接口，发送PUT请求。
     *
     * @param device 设备，携带的数据是要更新的数据，但是ID不变
     * @return HttpResult对象, 包含一个设备信息列表，实际上该列表最多只有一项
     * @see HttpResult
     * @see CookerBean
     */
    @PUT("/user/{userId}/cookers/{cookerId}")
    Observable<HttpResult<List<CookerBean>>> updateCooker(@Path("userId") long userId,
                                                          @Path("cookerId") long cookerId,
                                                          @Body CookerBean device);

    /**
     * 增加一个电饭锅设备接口，发送POST请求
     *
     * @param userId 用户ID
     * @param device 设备信息
     * @return HttpResult对象，包含一个设备信息列表，实际上最多只有一项
     * @see HttpResult
     * @see CookerBean
     */
    @POST("/user/{userId}/cookers")
    Observable<HttpResult<List<CookerBean>>> insertCooker(@Path("userId") long userId,
                                                          @Body CookerBean device);

    /**
     * 删除一个电饭锅设备接口，发送DELETE请求
     *
     * @param userId   用户ID
     * @param cookerId 设备ID
     * @return HttpResult对象，包含一个设备信息列表，实际上最多只有一项。（要不要返回被删除的设备信息有待商榷）
     * @see HttpResult
     * @see CookerBean
     */
    @DELETE("/user/{userId}/cookers/{cookerId}")
    Observable<HttpResult<List<CookerBean>>> deleteCooker(@Path("userId") long userId,
                                                          @Path("cookerId") long cookerId);

    /**
     * 删除所有电饭锅设备接口，发送DELETE请求
     *
     * @param userId 用户ID
     * @return HttpResult对象，包含一个设备信息列表。（要不要返回被删除的设备信息有待商榷）
     * @see HttpResult
     * @see CookerBean
     */
    @DELETE("/user/{userId}/cookers")
    Observable<HttpResult<List<CookerBean>>> deleteCookers(@Path("userId") long userId);

    /**
     * 查询某个预约信息接口
     *
     * @param userId 用户ID
     * @param bookId 预约ID
     * @return HttpResult对象，包含一个预约信息列表
     * @see HttpResult
     * @see BookBean
     */
    @GET("/user/{userId}/books/{bookId}")
    Observable<HttpResult<List<BookBean>>> queryBook(@Path("userId") long userId,
                                                     @Path("bookId") long bookId);

    /**
     * 查询所有预约信息接口
     *
     * @param userId 用户ID
     * @return HttpResult对象，包含一个预约信息列表
     * @see HttpResult
     * @see BookBean
     */
    @GET("/user/{userId}/books")
    Observable<HttpResult<List<BookBean>>> queryBooks(@Path("userId") long userId);

    /**
     * 更新某个预约信息接口
     *
     * @param userId 用户ID
     * @param bean   预约信息
     * @return HttpResult对象，包含一个预约信息列表，列表最多包含一项预约信息
     * @see HttpResult
     * @see BookBean
     */
    @PUT("/user/{userId}/books/{bookId}")
    Observable<HttpResult<List<BookBean>>> updateBook(@Path("userId") long userId,
                                                      @Body BookBean bean);

    /**
     * 增加一个预约信息接口
     *
     * @param userId 用户ID
     * @param book   预约信息
     * @return HttpResult对象，包含一个要预约信息列表，列表最多包含一项预约信息
     * @see HttpResult
     * @see BookBean
     */
    @POST("/user/{userId}/books")
    Observable<HttpResult<List<BookBean>>> insertBook(@Path("userId") long userId,
                                                      @Body BookBean book);

    /**
     * 删除某个预约接口
     *
     * @param userId 用户ID
     * @param bookId 预约信息ID
     * @return HttpResult对象，包含一个要预约信息列表，列表最多包含一项预约信息
     */
    @DELETE("/user/{userId}/books/{bookId}")
    Observable<BookBean> deleteBook(@Path("userId") long userId,
                                    @Path("bookId") long bookId);

    /**
     * 删除所有预约信息接口
     *
     * @param userId 用户ID
     * @return HttpResult对象，包含一个要预约信息列表，列表最多包含一项预约信息
     */
    @DELETE("/user/{userId}/books")
    Observable<BookBean> deleteBooks(@Path("userId") long userId);

}
