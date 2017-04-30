package me.stupidme.cooker.mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.model.update.VersionBean;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Path;
import retrofit2.http.Url;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by StupidL on 2017/4/29.
 */

public class MockCookerService implements CookerService {

    private RealmServerManager mRealmManager = RealmServerManager.getInstance();

    private BehaviorDelegate<CookerService> mDelegate;

    public MockCookerService(BehaviorDelegate<CookerService> delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public Observable<HttpResult<List<UserBean>>> login(@Field("userId") Long userId,
                                                        @Field("username") String name,
                                                        @Field("password") String password) {
        HttpResult<List<UserBean>> result = new HttpResult<>();
        List<UserBean> list = new ArrayList<>();
        UserBean userBean = mRealmManager.queryUser(userId);
        if (userBean == null) {
            result.setResultCode(400);
            result.setResultMessage("Not a valid account.");
            result.setData(list);
            return mDelegate.returningResponse(result).login(userId, name, password);
        }
        if (!userBean.getUserName().equals(name)) {
            result.setResultCode(400);
            result.setResultMessage("Please check your name or password.");
            result.setData(list);
            return mDelegate.returningResponse(result).login(userId, name, password);
        }
        if (!userBean.getPassword().equals(password)) {
            result.setResultCode(400);
            result.setResultMessage("Please check your name or password.");
            result.setData(list);
            return mDelegate.returningResponse(result).login(userId, name, password);
        }
        result.setResultCode(200);
        result.setResultMessage("Login success.");
        list.add(userBean);
        result.setData(list);
        return mDelegate.returningResponse(result).login(userId, name, password);
    }

    @Override
    public Observable<HttpResult<List<UserBean>>> register(@Body UserBean user) {
        HttpResult<List<UserBean>> result = new HttpResult<>();
        List<UserBean> list = new ArrayList<>();
        UserBean bean = mRealmManager.insertUser(user);
        list.add(bean);
        result.setResultCode(200);
        result.setResultMessage("Register success.");
        result.setData(list);
        return mDelegate.returningResponse(result).register(user);
    }

    @Override
    public Observable<HttpResult<List<UserBean>>> register(@Field("username") String name,
                                                           @Field("password") String password) {
        HttpResult<List<UserBean>> result = new HttpResult<>();
        List<UserBean> list = new ArrayList<>();
        UserBean user = new UserBean(name, password);
        UserBean bean = mRealmManager.insertUser(user);
        list.add(bean);
        result.setResultCode(200);
        result.setResultMessage("Register success.");
        result.setData(list);
        return mDelegate.returningResponse(result).register(user);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> queryCookers(@Path("userId") long userId) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        list.addAll(mRealmManager.queryCookers(userId));
        result.setResultCode(200);
        result.setResultMessage("Query Cookers success.");
        result.setData(list);
        return mDelegate.returningResponse(list).queryCookers(userId);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> queryCooker(@Path("userId") long userId,
                                                                @Path("cookerId") long cookerId) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> updateCooker(@Path("userId") long userId, @Path("cookerId") long cookerId, @Body CookerBean device) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> insertCooker(@Path("userId") long userId, @Body CookerBean device) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> deleteCooker(@Path("userId") long userId, @Path("cookerId") long cookerId) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> deleteCookers(@Path("userId") long userId) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> queryBook(@Path("userId") long userId, @Path("bookId") long bookId) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> queryBooks(@Path("userId") long userId) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> updateBook(@Path("userId") long userId, @Body BookBean bean) {
        return null;
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> insertBook(@Path("userId") long userId, @Body BookBean book) {
        return null;
    }

    @Override
    public Observable<BookBean> deleteBook(@Path("userId") long userId, @Path("bookId") long bookId) {
        return null;
    }

    @Override
    public Observable<BookBean> deleteBooks(@Path("userId") long userId) {
        return null;
    }

    @Override
    public Observable<HttpResult<VersionBean>> checkUpdate() {
        return null;
    }

    @Override
    public Observable<ResponseBody> downloadFile(@Url String url) {
        return null;
    }
}
