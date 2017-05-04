package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.db.RealmBookManager;
import me.stupidme.cooker.model.db.RealmDbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
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
        BookBean bookBean = mRealmManager.insertBook(book);
        mView.insertBook(bookBean);
        Log.v(TAG, "BookBean: " + bookBean.toString());
    }

    @Override
    public void insertBooks(List<BookBean> books) {
        mRealmManager.insertBooks(books);
        mView.insertBooks(books);
    }

    @Override
    public void deleteBook(BookBean book) {
        mRealmManager.deleteBooks(RealmBookManager.KEY_BOOK_ID, book.getBookId() + "");
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
}
