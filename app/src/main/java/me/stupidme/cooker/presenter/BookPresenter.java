package me.stupidme.cooker.presenter;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.BookModel;
import me.stupidme.cooker.model.IBookModel;
import me.stupidme.cooker.view.IBookAddView;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookPresenter implements IBookPresenter {

    private IBookAddView mView;

    private IBookModel mModel;

    public BookPresenter(IBookAddView view) {
        mView = view;
        mModel = BookModel.getInstance();
    }

    @Override
    public void addBook(BookBean bookBean) {

    }

    @Override
    public List<String> getCookersName() {
        return null;
    }
}
