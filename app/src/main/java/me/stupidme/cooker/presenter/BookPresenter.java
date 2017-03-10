package me.stupidme.cooker.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static BookPresenter sInstance;

    private IBookView mView;

    private IBookModel mModel;

    private CookerService mService;

    private BookPresenter(IBookView view) {
        mView = view;
        mModel = BookModel.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    public static BookPresenter getInstance(IBookView view) {
        if (sInstance == null)
            sInstance = new BookPresenter(view);
        return sInstance;
    }

    @Override
    public void insertBook(BookBean book) {

    }

    @Override
    public void deleteBook(BookBean book) {
        mView.removeBook(book);
    }

    @Override
    public void queryBooksFromDB() {
        List<BookBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BookBean bookBean = new BookBean();
            bookBean.setDeviceId(i);
            bookBean.setDeviceName(i + "");
            bookBean.setDevicePlace(i + "");
            bookBean.setPeopleCount(i);
            bookBean.setRiceWeight(500 + i);
            bookBean.setTaste(i % 2 == 0 ? "soft" : "hard");
            bookBean.setDeviceStatus("free");
            bookBean.setTime("18:00");
            list.add(bookBean);
        }

        mView.insertBooks(list);
    }

    @Override
    public void queryBooksFromServer(Map<String, String> map) {

    }
}
