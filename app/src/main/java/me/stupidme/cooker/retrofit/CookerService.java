package me.stupidme.cooker.retrofit;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface CookerService {

    String BASE_URL = "http://121.42.47.110:8080/";

    @POST("/user/login")
    Observable<UserBean> rxLogin(@Field("username") String name, @Field("password") String password);

    @POST("/user/register")
    Observable<UserBean> rxRegister(@Body UserBean user);

    @GET("/user/devices")
    Observable<List<CookerBean>> rxGetAllDevices(@QueryMap Map<String, String> options);

    @GET("/user/devices/{deviceId}")
    Observable<CookerBean> rxGetDevice(@Path("deviceId") int deviceId);

    @POST("/user/devices/")
    Observable<CookerBean> rxUpdateDevice(@Body CookerBean device);

    @POST("/user/devices/")
    Observable<CookerBean> rxPostNewDevice(@Body CookerBean device);

    @DELETE("/user/devices/{id}")
    Observable<CookerBean> rxDeleteDevice(@Path("id") int deviceId);

    @DELETE("/user/devices/{id}")
    Observable<CookerBean> rxDeleteDevice(@Body CookerBean device);

    @GET("/user/books/{id}")
    Observable<BookBean> rxGetBookInfo(@Path("id") int bookId);

    @GET("/user/books")
    Observable<List<BookBean>> rxGetAllBooksInfo(@QueryMap Map<String, String> options);

    @POST("/user/books")
    Observable<BookBean> rxGpdateBook(@Body BookBean bean);

    @POST("/user/books")
    Observable<BookBean> rxPostNewBook(@Body BookBean book);

    @DELETE("/user/books/{id}")
    Observable<BookBean> rxDeleteBook(@Path("id") int bookId);

    @DELETE("/user/books/")
    Observable<BookBean> rxDeleteBook(@Body BookBean book);

    @DELETE("/user/books/{id}")
    Call<BookBean> retrofitDeleteBook(@Path("id") int bookId);

}
