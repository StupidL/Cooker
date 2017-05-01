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

    private IServerDbManager mServerDbManager = RealmServerManager.getInstance();

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
        UserBean userBean = mServerDbManager.queryUser(userId);
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
        UserBean bean = mServerDbManager.insertUser(user);
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
        UserBean bean = mServerDbManager.insertUser(user);
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
        list.addAll(mServerDbManager.queryCookers(userId));
        result.setResultCode(200);
        result.setResultMessage("Query Cookers success.");
        result.setData(list);
        return mDelegate.returningResponse(result).queryCookers(userId);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> queryCooker(@Path("userId") long userId,
                                                                @Path("cookerId") long cookerId) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        list.add(mServerDbManager.queryCooker(userId, cookerId));
        result.setResultCode(200);
        result.setResultMessage("Query Cookers success.");
        result.setData(list);
        return mDelegate.returningResponse(result).queryCooker(userId, cookerId);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> updateCooker(@Path("userId") long userId,
                                                                 @Path("cookerId") long cookerId,
                                                                 @Body CookerBean device) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        CookerBean cookerBean = mServerDbManager.updateCooker(device);
        list.add(cookerBean);
        result.setResultCode(200);
        result.setResultMessage("Update Cookers success.");
        result.setData(list);
        return mDelegate.returningResponse(result).updateCooker(userId, cookerId, device);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> insertCooker(@Path("userId") long userId,
                                                                 @Body CookerBean device) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        CookerBean cookerBean = mServerDbManager.insertCooker(device);
        list.add(cookerBean);
        result.setResultCode(200);
        result.setResultMessage("Insert Cookers success.");
        result.setData(list);
        return mDelegate.returningResponse(result).insertCooker(userId, device);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> deleteCooker(@Path("userId") long userId,
                                                                 @Path("cookerId") long cookerId) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        CookerBean cookerBean = mServerDbManager.deleteCooker(cookerId);
        list.add(cookerBean);
        result.setResultCode(200);
        result.setResultMessage("Delete Cooker success.");
        result.setData(list);
        return mDelegate.returningResponse(result).deleteCooker(userId, cookerId);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> deleteCookers(@Path("userId") long userId) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        list.addAll(mServerDbManager.deleteCookers(userId));
        result.setResultCode(200);
        result.setResultMessage("Delete Cookers success.");
        result.setData(list);
        return mDelegate.returningResponse(result).deleteCookers(userId);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> queryBook(@Path("userId") long userId,
                                                            @Path("bookId") long bookId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        list.add(mServerDbManager.queryBook(bookId));
        result.setResultCode(200);
        result.setResultMessage("Query Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).queryBook(userId, bookId);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> queryBooks(@Path("userId") long userId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        list.addAll(mServerDbManager.queryBooks(userId));
        result.setResultCode(200);
        result.setResultMessage("Query Books success.");
        result.setData(list);
        return mDelegate.returningResponse(result).queryBooks(userId);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> updateBook(@Path("userId") long userId,
                                                             @Body BookBean bean) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        list.add(mServerDbManager.updateBook(bean));
        result.setResultCode(200);
        result.setResultMessage("Update Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).updateBook(userId, bean);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> insertBook(@Path("userId") long userId,
                                                             @Body BookBean book) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        list.add(mServerDbManager.insertBook(book));
        result.setResultCode(200);
        result.setResultMessage("Insert Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).insertBook(userId, book);
    }

    @Override
    public Observable<BookBean> deleteBook(@Path("userId") long userId, @Path("bookId") long bookId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        list.add(mServerDbManager.deleteBook(bookId));
        result.setResultCode(200);
        result.setResultMessage("Delete Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).deleteBook(userId, bookId);
    }

    @Override
    public Observable<BookBean> deleteBooks(@Path("userId") long userId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        list.addAll(mServerDbManager.deleteBooks(userId));
        result.setResultCode(200);
        result.setResultMessage("Insert Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).deleteBooks(userId);
    }

    @Override
    public Observable<HttpResult<VersionBean>> checkUpdate() {
        HttpResult<VersionBean> result = new HttpResult<>();
        VersionBean versionBean = new VersionBean();
        result.setResultCode(200);
        result.setResultMessage("Check update success.");
        result.setData(versionBean);
        return mDelegate.returningResponse(result).checkUpdate();
    }

    @Override
    public Observable<ResponseBody> downloadFile(@Url String url) {
        return null;
    }
}
