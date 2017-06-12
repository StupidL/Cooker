package me.stupidme.cooker.model.http;

import java.util.List;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.update.VersionBean;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * This class defines all the methods to network communication.
 */

public interface CookerService {

    /**
     * server address.
     */
    String BASE_URL = "http://121.42.47.110:8080/Cooker/";

    /**
     * User login interface. Use POST request method.
     *
     * @param userId   user's id
     * @param name     user's name
     * @param password user's password
     * @return observable http result containing a list of {@link UserBean},
     * and the list at most contains one user bean.
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<HttpResult<List<UserBean>>> login(@Field("userId") Long userId,
                                                 @Field("username") String name,
                                                 @Field("password") String password);

    /**
     * User register interface. Use POST request method.
     *
     * @param user a user bean as HTTP request body.
     * @return observable http result, the list of result contains at most one user bean.
     * @see HttpResult
     * @see UserBean
     */
    @POST("/user/register")
    Observable<HttpResult<List<UserBean>>> register(@Body UserBean user);

    /**
     * user register interface.
     *
     * @param userId   user's id
     * @param name     user's name
     * @param password user's password
     * @return observable http result, the list of result contains at most one user bean.
     * @see HttpResult
     */
    @POST("/user/register")
    Observable<HttpResult<List<UserBean>>> register(@Field("userId") Long userId,
                                                    @Field("username") String name,
                                                    @Field("password") String password);

    /**
     * query cookers from server.
     *
     * @param userId user's id
     * @return observable http result, the list may empty or contains several cooker bean.
     */
    @GET("/user/{userId}/cookers")
    Observable<HttpResult<List<CookerBean>>> queryCookers(@Path("userId") long userId);

    /**
     * query a cooker from server.
     *
     * @param userId   user's id
     * @param cookerId cooker's id
     * @return observable http result, the list contains at most one cooker bean.
     */
    @GET("/user/{userId}/cookers/{cookerId}")
    Observable<HttpResult<List<CookerBean>>> queryCooker(@Path("userId") long userId,
                                                         @Path("cookerId") long cookerId);

    /**
     * update a cooker.
     *
     * @param userId   user's id
     * @param cookerId cooker's id
     * @param device   cooker
     * @return observable http result, the list contains at most one cooker bean.
     */
    @PUT("/user/{userId}/cookers/{cookerId}")
    Observable<HttpResult<List<CookerBean>>> updateCooker(@Path("userId") long userId,
                                                          @Path("cookerId") long cookerId,
                                                          @Body CookerBean device);

    @POST("/user/{userId}/cookers")
    Observable<HttpResult<List<CookerBean>>> insertCooker(@Path("userId") long userId,
                                                          @Body CookerBean device);

    @DELETE("/user/{userId}/cookers/{cookerId}")
    Observable<HttpResult<List<CookerBean>>> deleteCooker(@Path("userId") long userId,
                                                          @Path("cookerId") long cookerId);

    @DELETE("/user/{userId}/cookers")
    Observable<HttpResult<List<CookerBean>>> deleteCookers(@Path("userId") long userId);

    @GET("/user/{userId}/books/{bookId}")
    Observable<HttpResult<List<BookBean>>> queryBook(@Path("userId") long userId,
                                                     @Path("bookId") long bookId);

    @GET("/user/{userId}/books")
    Observable<HttpResult<List<BookBean>>> queryBooks(@Path("userId") long userId);

    @PUT("/user/{userId}/books")
    Observable<HttpResult<List<BookBean>>> updateBook(@Path("userId") long userId,
                                                      @Body BookBean bean);

    @POST("/user/{userId}/books")
    Observable<HttpResult<List<BookBean>>> insertBook(@Path("userId") long userId,
                                                      @Body BookBean book);

    @DELETE("/user/{userId}/books/{bookId}")
    Observable<HttpResult<List<BookBean>>> deleteBook(@Path("userId") long userId,
                                                      @Path("bookId") long bookId);

    @DELETE("/user/{userId}/books")
    Observable<HttpResult<List<BookBean>>> deleteBooks(@Path("userId") long userId);

    @GET("/update/onResult")
    Observable<HttpResult<VersionBean>> checkUpdate();

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

}
