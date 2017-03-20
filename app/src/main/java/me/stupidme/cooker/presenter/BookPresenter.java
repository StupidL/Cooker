package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.db.DBManager;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.BookModel;
import me.stupidme.cooker.model.IBookModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.retrofit.HttpResult;
import me.stupidme.cooker.view.book.IBookView;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookPresenter implements IBookPresenter {

    private static final String TAG = "BookPresenter";

    private IBookView mView;

    private IBookModel mModel;

    private CookerService mService;

    public BookPresenter(IBookView view) {
        mView = view;
        mModel = BookModel.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void insertBook(BookBean book) {
        mService.insertBook(mView.getUserId(), book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        mModel.insertBook(value.getData().get(0));
                        mView.insertBook(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
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
    public void deleteBook(BookBean book) {
        mService.deleteBook(mView.getUserId(), book.getBookId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(BookBean value) {
                        mView.showMessage(value.toString());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.removeBook(book);
                        mModel.deleteBook(book);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryBooksFromDB() {

        DBManager.getInstance()
                .queryBooks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BookBean> value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //just a test
                        List<BookBean> list = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            BookBean bookBean = new BookBean();
                            bookBean.setCookerId(i);
                            bookBean.setCookerName(i + "");
                            bookBean.setCookerLocation(i + "");
                            bookBean.setPeopleCount(i);
                            bookBean.setRiceWeight(500 + i);
                            bookBean.setTaste(i % 2 == 0 ? "soft" : "hard");
                            bookBean.setCookerStatus("free");
                            bookBean.setTime("18:00");
                            list.add(bookBean);
                        }
                        Log.v(getClass().getCanonicalName(), "List Size: " + list.size());
                        mView.insertBooks(list);
                    }
                });

    }

    @Override
    public void queryBooksFromServer(long userId) {
        mView.setRefreshing(true);
        mService.queryBooks(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        mModel.updateBooks(value.getData());
                        mView.insertBooks(value.getData());
                        mView.showMessage(value.toString());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }
}
