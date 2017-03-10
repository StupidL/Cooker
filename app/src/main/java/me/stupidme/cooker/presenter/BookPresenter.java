package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.BookModel;
import me.stupidme.cooker.model.IBookModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
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
        mService.rxPostNewBook(book)
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
                        mModel.insertBook(book);
                        mView.insertBook(book);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void deleteBook(BookBean book) {
        mService.rxDeleteBook(book)
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
        //just a test
//        List<BookBean> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            BookBean bookBean = new BookBean();
//            bookBean.setDeviceId(i);
//            bookBean.setDeviceName(i + "");
//            bookBean.setDevicePlace(i + "");
//            bookBean.setPeopleCount(i);
//            bookBean.setRiceWeight(500 + i);
//            bookBean.setTaste(i % 2 == 0 ? "soft" : "hard");
//            bookBean.setDeviceStatus("free");
//            bookBean.setTime("18:00");
//            list.add(bookBean);
//        }
//        Log.v(getClass().getCanonicalName(), "List Size: " + list.size());
//        mView.insertBooks(list);

        mView.insertBooks(mModel.queryBooks());

    }

    @Override
    public void queryBooksFromServer(Map<String, String> map) {
        mView.setRefreshing(true);
        mService.rxGetAllBooksInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookBean>>() {

                    List<BookBean> list = new ArrayList<>();

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(List<BookBean> value) {
                        list.addAll(value);
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
                        mModel.updateBooks(list);
                        mView.insertBooks(list);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }
}
