package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.BookModel;
import me.stupidme.cooker.model.IBookModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.retrofit.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.book.IBookView;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookPresenter implements IBookPresenter {

    private static final String TAG = "BookPresenter";

    private IBookView mView;

    private IBookModel mModel;

    private CookerService mService;

    private CompositeDisposable mCompositeDisposable;

    public BookPresenter(IBookView view) {
        mView = view;
        mModel = BookModel.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void insertBook(BookBean book) {

        mService.insertBook(SharedPreferenceUtil.getAccountUserId(0L), book)
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.insertBook(listHttpResult.getData().get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);

                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
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
    public void insertBooks(List<BookBean> books) {

    }

    @Override
    public void deleteBook(BookBean book) {

        mService.deleteBook(SharedPreferenceUtil.getAccountUserId(0L), book.getBookId())
                .subscribeOn(Schedulers.io())
                .doOnNext(bean -> mModel.deleteBook(bean))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);

                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(BookBean value) {
                        mView.removeBook(book);
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
    public void deleteBooks(List<BookBean> books) {

    }

    @Override
    public void queryBookFromDB(long bookId) {
        mView.insertBook(mModel.queryBook(bookId));
    }

    @Override
    public void queryBooksFromDB() {

//        mView.insertBooks(mModel.queryBooks());

        List<BookBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BookBean bookBean = new BookBean();
            bookBean.setCookerId(i);
            bookBean.setCookerName(i + "");
            bookBean.setCookerLocation(i + "");
            bookBean.setPeopleCount(i);
            bookBean.setRiceWeight(500 + i);
            bookBean.setTaste(i % 2 == 0 ? "soft" : "hard");
            bookBean.setCookerStatus(i % 2 == 0 ? "free" : "booking");
            bookBean.setTime(1433387772);
            list.add(bookBean);
        }
        Log.v(getClass().getCanonicalName(), "List Size: " + list.size());
        mView.insertBooks(list);

    }

    @Override
    public void queryBookFromServer(long bookId) {

        mService.queryBook(SharedPreferenceUtil.getAccountUserId(0L), bookId)
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.updateBook(listHttpResult.getData().get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);

                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        mView.insertBooks(value.getData());
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
    public void queryBooksFromServer() {
        mView.setRefreshing(true);

        mService.queryBooks(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.updateBooks(listHttpResult.getData()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);

                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        mView.insertBooks(value.getData());
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
    public void dispose() {
        mCompositeDisposable.clear();
    }
}
