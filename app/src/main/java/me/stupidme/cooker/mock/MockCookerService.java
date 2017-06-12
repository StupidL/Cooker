package me.stupidme.cooker.mock;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * Mock server for {@link CookerService}.
 * This mock server use SQLite database to manage data.
 */

public class MockCookerService implements CookerService {

    /**
     * server SQLite manager
     */
    private ServerDbManager mServerDbManager = ServerDbManagerImpl.getInstance();

    /**
     * delegate of CookerService
     */
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
        UserBean userBean = mServerDbManager.queryUser(ServerDbManager.KEY_TABLE_USER_ID, userId);
        if (userBean == null) {
            result.setResultCode(404);
            result.setResultMessage("Not a valid account.");
            result.setData(list);
            return mDelegate.returningResponse(result).login(userId, name, password);
        }
        if (!userBean.getUserName().equals(name)) {
            result.setResultCode(403);
            result.setResultMessage("Please check your name or password.");
            result.setData(list);
            return mDelegate.returningResponse(result).login(userId, name, password);
        }
        if (!userBean.getPassword().equals(password)) {
            result.setResultCode(403);
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
        user.setUserId(new Random().nextLong());
        UserBean bean = mServerDbManager.insertUser(user);
        if (bean == null) {
            result.setResultCode(400);
            result.setResultMessage("Register failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).register(user);
        }
        if (!bean.getUserName().equals(user.getUserName())
                || !bean.getPassword().equals(user.getPassword())
                || TextUtils.isEmpty(bean.getUserId() + "")) {
            result.setResultCode(400);
            result.setResultMessage("Register failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).register(user);
        }

        list.add(bean);
        result.setResultCode(200);
        result.setResultMessage("Register success.");
        result.setData(list);
        return mDelegate.returningResponse(result).register(user);
    }

    @Override
    public Observable<HttpResult<List<UserBean>>> register(@Field("userId") Long userId,
                                                           @Field("username") String name,
                                                           @Field("password") String password) {
        HttpResult<List<UserBean>> result = new HttpResult<>();
        List<UserBean> list = new ArrayList<>();
        UserBean user = new UserBean(name, password);
        user.setUserId(new Random().nextLong());
        UserBean bean = mServerDbManager.insertUser(user);
        if (bean == null) {
            result.setResultCode(400);
            result.setResultMessage("Register failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).register(new UserBean());
        }
        if (!bean.getUserName().equals(name)
                || !bean.getPassword().equals(password)
                || TextUtils.isEmpty(bean.getUserId() + "")) {
            result.setResultCode(400);
            result.setResultMessage("Register failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).register(bean);
        }

        list.add(bean);
        result.setResultCode(200);
        result.setResultMessage("Register success.");
        result.setData(list);
        return mDelegate.returningResponse(result).register(bean);
    }

    @Override
    public Observable<HttpResult<List<CookerBean>>> queryCookers(@Path("userId") long userId) {
        HttpResult<List<CookerBean>> result = new HttpResult<>();
        List<CookerBean> list = new ArrayList<>();
        List<CookerBean> cookerBeanList = mServerDbManager.queryCookers(userId);
        if (cookerBeanList == null || cookerBeanList.size() == 0) {
            result.setResultCode(200);
            result.setResultMessage("Query Cookers success but no data.");
            result.setData(list);
            return mDelegate.returningResponse(result).queryCookers(userId);
        }
        list.addAll(cookerBeanList);
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
        CookerBean cookerBean = mServerDbManager.queryCooker(userId, cookerId);
        if (cookerBean == null
                || cookerBean.getCookerId() != cookerId
                || cookerBean.getUserId() != userId) {
            result.setResultCode(400);
            result.setResultMessage("Query Cooker failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).queryCooker(userId, cookerId);
        }
        list.add(cookerBean);
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
        if (cookerBean == null
                || cookerBean.getCookerId() != cookerId
                || cookerBean.getUserId() != userId) {
            result.setResultCode(400);
            result.setResultMessage("Update Cooker failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).updateCooker(userId, cookerId, device);
        }
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
        if (cookerBean == null || cookerBean.getUserId() != userId) {
            result.setResultCode(400);
            result.setResultMessage("Insert Cooker failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).insertCooker(userId, device);
        }
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
        CookerBean cookerBean = mServerDbManager.deleteCooker(userId, cookerId);
        if (cookerBean == null) {
            result.setResultCode(400);
            result.setResultMessage("Delete Cooker failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).deleteCooker(userId, cookerId);
        }
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
        List<CookerBean> cookerBeanList = mServerDbManager.deleteCookers(userId);
        if (cookerBeanList == null || cookerBeanList.size() <= 0) {
            result.setResultCode(400);
            result.setResultMessage("Delete Cooker failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).deleteCookers(userId);
        }
        list.addAll(cookerBeanList);
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
        BookBean bookBean = mServerDbManager.queryBook(userId, bookId);
        if (bookBean == null) {
            result.setResultCode(200);
            result.setResultMessage("Query Book success but no data.");
            result.setData(list);
            return mDelegate.returningResponse(result).queryBook(userId, bookId);
        }
        list.add(bookBean);
        result.setResultCode(200);
        result.setResultMessage("Query Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).queryBook(userId, bookId);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> queryBooks(@Path("userId") long userId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        List<BookBean> bookBeanList = mServerDbManager.queryBooks(userId);
        if (bookBeanList == null || bookBeanList.size() <= 0) {
            result.setResultCode(200);
            result.setResultMessage("Query Book success but no data.");
            result.setData(list);
            return mDelegate.returningResponse(result).queryBooks(userId);
        }
        list.addAll(bookBeanList);
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
        BookBean bookBean = mServerDbManager.updateBook(bean);
        if (bookBean == null) {
            result.setResultCode(400);
            result.setResultMessage("Update Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).updateBook(userId, bean);
        }
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
        BookBean bookBean = mServerDbManager.insertBook(book);
        if (bookBean == null) {
            result.setResultCode(400);
            result.setResultMessage("Insert Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).insertBook(userId, book);
        }
        CookerBean cookerBean = new CookerBean();
        cookerBean.setUserId(book.getUserId());
        cookerBean.setCookerId(book.getCookerId());
        cookerBean.setCookerName(book.getCookerName());
        cookerBean.setCookerLocation(book.getCookerLocation());
        cookerBean.setCookerStatus("Booking");
        CookerBean cookerBean1 = mServerDbManager.updateCooker(cookerBean);
        if (cookerBean1 == null) {
            result.setResultCode(400);
            result.setResultMessage("Update Cooker after insert Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).insertBook(userId, book);
        }
        list.add(bookBean);
        result.setResultCode(200);
        result.setResultMessage("Insert Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).insertBook(userId, book);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> deleteBook(@Path("userId") long userId, @Path("bookId") long bookId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        BookBean bookBean = mServerDbManager.deleteBook(userId, bookId);
        if (bookBean == null) {
            result.setResultCode(400);
            result.setResultMessage("Delete Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).deleteBook(userId, bookId);
        }
        CookerBean cookerBean = new CookerBean();
        cookerBean.setUserId(bookBean.getUserId());
        cookerBean.setCookerId(bookBean.getCookerId());
        cookerBean.setCookerName(bookBean.getCookerName());
        cookerBean.setCookerLocation(bookBean.getCookerLocation());
        //This may cause an error, because a cooker may be used by many books.
        //So set status directly is not a right solution.
        cookerBean.setCookerStatus("Free");
        if (mServerDbManager.updateCooker(cookerBean) == null) {
            result.setResultCode(200);
            result.setResultMessage("Delete Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).deleteBook(userId, bookId);
        }
        bookBean.setCookerStatus("Free");
        if (mServerDbManager.updateBook(bookBean) == null) {
            result.setResultCode(200);
            result.setResultMessage("Delete Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).deleteBook(userId, bookId);
        }
        list.add(bookBean);
        result.setResultCode(200);
        result.setResultMessage("Delete Book success.");
        result.setData(list);
        return mDelegate.returningResponse(result).deleteBook(userId, bookId);
    }

    @Override
    public Observable<HttpResult<List<BookBean>>> deleteBooks(@Path("userId") long userId) {
        HttpResult<List<BookBean>> result = new HttpResult<>();
        List<BookBean> list = new ArrayList<>();
        List<BookBean> bookBeanList = mServerDbManager.deleteBooks(userId);
        if (bookBeanList == null || bookBeanList.size() <= 0) {
            result.setResultCode(400);
            result.setResultMessage("Delete Book failed.");
            result.setData(list);
            return mDelegate.returningResponse(result).deleteBooks(userId);
        }
        list.addAll(bookBeanList);
        result.setResultCode(200);
        result.setResultMessage("Delete Book success.");
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
