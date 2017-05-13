package me.stupidme.cooker.presenter;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.RealmBookManager;
import me.stupidme.cooker.model.db.RealmDbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.book.BookDialog;
import me.stupidme.cooker.view.book.BookView;

/**
 * Created by StupidL on 2017/4/30.
 */

public class BookMockPresenterImpl implements BookPresenter {

    private static final String TAG = "BookMockPresenter";

    private BookView mView;

    private RealmBookManager mRealmManager;

    private MockCookerService mMockService;

    public BookMockPresenterImpl(BookView view) {
        mView = view;
        mRealmManager = RealmDbManagerImpl.getInstance();
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void insertBook(BookBean book) {
//        BookBean bookBean = mRealmManager.insertBook(book);
//        mView.insertBook(bookBean);
//        Log.v(TAG, "BookBean: " + bookBean.toString());

        mMockService.insertBook(SharedPreferenceUtil.getAccountUserId(0L), book)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value.getResultCode() == 200) {
                            mRealmManager.insertBook(value.getData().get(0));
                            mView.insertBook(value.getData().get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void insertBooks(List<BookBean> books) {
        mRealmManager.insertBooks(books);
        mView.insertBooks(books);
    }

    @Override
    public void insertBook(Map<String, String> map) {
        String cookerName = map.get(BookDialog.KEY_COOKER_NAME);
        String taste = map.get(BookDialog.KEY_TASTE);
        int peopleCount = Integer.parseInt(map.get(BookDialog.KEY_PEOPLE_COUNT));
        int riceWeight = Integer.parseInt(map.get(BookDialog.KEY_RICE_WEIGHT));
        String time = map.get(BookDialog.KEY_BOOK_TIME);
        BookBean bookBean = new BookBean();
        CookerBean cookerBean = mRealmManager.queryCooker(RealmBookManager.KEY_COOKER_NAME, cookerName);
        bookBean.setBookId(new Random().nextLong());
        bookBean.setUserId(SharedPreferenceUtil.getAccountUserId(0L));
        bookBean.setCookerId(cookerBean.getCookerId());
        bookBean.setCookerName(cookerName);
        bookBean.setCookerLocation(cookerBean.getCookerLocation());
        bookBean.setCookerStatus(cookerBean.getCookerStatus());
        bookBean.setPeopleCount(peopleCount);
        bookBean.setTaste(taste);
        bookBean.setRiceWeight(riceWeight);

        String[] t = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        Long timeLong = calendar.getTimeInMillis();
        bookBean.setTime(timeLong);

        mMockService.insertBook(SharedPreferenceUtil.getAccountUserId(0L), bookBean)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value.getResultCode() == 200) {
                            BookBean bookBean1 = value.getData().get(0);
                            CookerBean cookerBean = new CookerBean();
                            cookerBean.setUserId(bookBean1.getUserId());
                            cookerBean.setCookerId(bookBean1.getCookerId());
                            cookerBean.setCookerName(bookBean1.getCookerName());
                            cookerBean.setCookerLocation(bookBean1.getCookerLocation());
                            cookerBean.setCookerStatus("Booking");
                            mRealmManager.updateCookerStatus(cookerBean);
                            mView.insertBook(bookBean1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //update local cooker status
//        mRealmManager.updateCookerStatus(cookerBean);

        //update server cooker status

//        mView.insertBook(bookBean);
    }

    @Override
    public void deleteBook(BookBean book) {
        mRealmManager.deleteBooks(RealmBookManager.KEY_BOOK_ID, book.getBookId());
        mView.removeBook(book);
    }

    @Override
    public void deleteBooks(List<BookBean> books) {
        books.forEach(this::deleteBook);
    }

    @Override
    public void queryBookFromDB(long bookId) {
        mView.insertBooks(mRealmManager.queryBooks(RealmBookManager.KEY_BOOK_ID, bookId + ""));
    }

    @Override
    public void queryBooksFromDB() {
        mView.insertBooks(mRealmManager.queryBooks(RealmBookManager.KEY_USER_ID, SharedPreferenceUtil.getAccountUserId(0L)));
    }

    @Override
    public void queryBookFromServer(long bookId) {
        mMockService.queryBook(SharedPreferenceUtil.getAccountUserId(0L), bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        BookBean bookBean = value.getData().get(0);
                        mView.insertBook(bookBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void queryBooksFromServer() {
        mMockService.queryBooks(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        mView.insertBooks(value.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<String> queryCookerNamesFromDB() {
        return mRealmManager.queryCookerNamesAll(RealmBookManager.KEY_USER_ID,
                SharedPreferenceUtil.getAccountUserId(0L));
    }
}
