package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.DbManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.book.BookDialog;
import me.stupidme.cooker.view.book.BookView;

import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_BOOK_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_BOOK_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_BOOK_SUCCESS_BUT_EMPTY;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_BOOK_DB_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_BOOK_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_BOOK_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_BOOK_HISTORY_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_BOOK_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_BOOK_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_BOOK_DB_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_LOCAL_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_SERVER_COOKER_FAILED;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookPresenterImpl implements BookPresenter {

    private static final String TAG = "BookPresenter";

    private BookView mView;

    private DbManager mDbManager;

    private CookerService mService;

    public BookPresenterImpl(BookView view) {
        mView = view;
        mDbManager = DbManagerImpl.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void insertBook(BookBean book) {
        mView.showDialog(true);
        mService.insertBook(SharedPreferenceUtil.getAccountUserId(0L), book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY, null);
                            return;
                        }
                        BookBean bookBean = value.getData().get(0);
                        bookBean.setCookerStatus("Booking");
                        boolean success = mDbManager.insertBook(bookBean);
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_DB_FAILED, null);
                            return;
                        }
                        boolean success2 = mDbManager.insertBookHistory(bookBean);
                        if (!success2) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_HISTORY_FAILED, null);
                            return;
                        }
                        boolean success3 = setCookerStatusBooking(bookBean);
                        if (!success3) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_UPDATE_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        mView.insertBook(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_INSERT_BOOK_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    private boolean setCookerStatusBooking(BookBean bookBean) {
        CookerBean cookerBean = new CookerBean();
        cookerBean.setUserId(bookBean.getUserId());
        cookerBean.setCookerId(bookBean.getCookerId());
        cookerBean.setCookerName(bookBean.getCookerName());
        cookerBean.setCookerLocation(bookBean.getCookerLocation());
        cookerBean.setCookerStatus("Booking");
        return mDbManager.updateCooker(cookerBean);
    }

    @Override
    public void insertBook(Map<String, String> map) {
        mView.showDialog(true);

        String cookerName = map.get(BookDialog.KEY_COOKER_NAME);
        int peopleCount = Integer.parseInt(map.get(BookDialog.KEY_PEOPLE_COUNT));
        int riceWeight = Integer.parseInt(map.get(BookDialog.KEY_RICE_WEIGHT));
        String taste = map.get(BookDialog.KEY_TASTE);
        String time = map.get(BookDialog.KEY_BOOK_TIME);
        String[] times = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        long t = calendar.getTimeInMillis();
        CookerBean cookerBean = mDbManager.queryCooker(DbManager.KEY_COOKER_NAME, cookerName);
        if (cookerBean == null) {
            mView.showDialog(false);
            mView.showMessage(MESSAGE_INSERT_BOOK_FAILED, null);
            return;
        }
        BookBean bookBean = new BookBean();
        bookBean.setUserId(SharedPreferenceUtil.getAccountUserId(0L));
        bookBean.setBookId(new Random().nextLong());
        bookBean.setCookerId(cookerBean.getCookerId());
        bookBean.setCookerName(cookerBean.getCookerName());
        bookBean.setCookerLocation(cookerBean.getCookerLocation());
        bookBean.setCookerStatus(cookerBean.getCookerStatus());
        bookBean.setPeopleCount(peopleCount);
        bookBean.setRiceWeight(riceWeight);
        bookBean.setTaste(taste);
        bookBean.setTime(t);

        mService.insertBook(SharedPreferenceUtil.getAccountUserId(0L), bookBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY, null);
                            return;
                        }
                        BookBean bookBean1 = value.getData().get(0);
                        bookBean1.setCookerStatus("Booking");
                        boolean success = mDbManager.insertBook(bookBean1);
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_DB_FAILED, null);
                            return;
                        }

                        boolean success2 = mDbManager.insertBookHistory(bookBean1);
                        if (!success2) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_BOOK_HISTORY_FAILED, null);
                            return;
                        }
                        boolean success3 = setCookerStatusBooking(bookBean1);
                        if (!success3) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_UPDATE_SERVER_COOKER_FAILED, null);
                            return;
                        }

                        mView.insertBook(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_INSERT_BOOK_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void deleteBook(BookBean book) {
        mView.showDialog(true);
        mService.deleteBook(SharedPreferenceUtil.getAccountUserId(0L), book.getBookId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_BOOK_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_BOOK_SUCCESS_BUT_EMPTY, null);
                            return;
                        }
                        BookBean bookBean = value.getData().get(0);
                        boolean success = mDbManager.deleteBook(DbManager.KEY_BOOK_ID, bookBean.getBookId());
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_BOOK_FAILED, null);
                            return;
                        }
//                        boolean success2 = updateCookerStatus(bookBean);
//                        if (!success2) {
//                            mView.showDialog(false);
//                            mView.showMessage(MESSAGE_UPDATE_LOCAL_COOKER_FAILED, null);
//                            return;
//                        }
                        mView.removeBook(book);
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_DELETE_BOOK_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    private boolean updateCookerStatus(BookBean bookBean) {
        List<BookBean> list = mDbManager.queryBooks(DbManager.KEY_COOKER_NAME, bookBean.getCookerName());
        if (list == null || list.size() <= 0) {
            return setCookerStatusFree(bookBean);
        }
        int count = 0;
        for (BookBean b : list) {
            if ("Booking".toUpperCase().equals(b.getCookerStatus().toUpperCase()))
                count++;
        }
        return count != 0 || setCookerStatusFree(bookBean);
    }

    private boolean setCookerStatusFree(BookBean bookBean) {
        CookerBean cookerBean = new CookerBean();
        cookerBean.setUserId(bookBean.getUserId());
        cookerBean.setCookerId(bookBean.getCookerId());
        cookerBean.setCookerName(bookBean.getCookerName());
        cookerBean.setCookerLocation(bookBean.getCookerLocation());
        cookerBean.setCookerStatus("Free");
        return mDbManager.updateCooker(cookerBean);
    }

    @Override
    public void deleteBookHistory(BookBean book) {
        boolean success = mDbManager.deleteBookHistory(DbManager.KEY_BOOK_ID, book.getBookId());
        if (!success) {
            mView.showMessage(0, "Db delete history failed!");
            return;
        }
        mView.removeBook(book);
    }

    @Override
    public void queryBookFromDB(long bookId) {
        mView.insertBook(mDbManager.queryBook(DbManager.KEY_BOOK_ID, bookId));
    }

    @Override
    public void queryBooksFromDB() {
        mView.insertBooks(mDbManager.queryBooks(DbManager.KEY_USER_ID,
                SharedPreferenceUtil.getAccountUserId(0L)));
    }

    @Override
    public void queryBookFromServer(long bookId) {
        mView.setRefreshing(true);
        mService.queryBook(SharedPreferenceUtil.getAccountUserId(0L), bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.setRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_BOOK_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.setRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.updateBook(value.getData().get(0));
                        if (!success) {
                            mView.setRefreshing(false);
                            mView.showMessage(MESSAGE_UPDATE_BOOK_DB_FAILED, null);
                            return;
                        }
                        mView.insertBooks(value.getData());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setRefreshing(false);
                        mView.showMessage(MESSAGE_QUERY_BOOK_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryBooksFromServer() {
        mView.setRefreshing(true);
        mService.queryBooks(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.setRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_BOOK_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.setRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.updateBooks(value.getData());
                        if (!success) {
                            mView.setRefreshing(false);
                            mView.showMessage(MESSAGE_UPDATE_BOOK_DB_FAILED, null);
                            return;
                        }
                        mView.insertBooks(value.getData());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setRefreshing(false);
                        mView.showMessage(MESSAGE_QUERY_BOOK_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public List<String> queryCookerNamesFromDB() {
        return mDbManager.queryCookerNamesAll(DbManager.KEY_USER_ID, SharedPreferenceUtil.getAccountUserId(0L));
    }

}
