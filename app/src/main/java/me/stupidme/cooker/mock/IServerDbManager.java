package me.stupidme.cooker.model.db;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public interface IServerDbManager{

    UserBean insertUser(UserBean userBean);

    UserBean queryUser(Long userId);

    CookerBean insertCooker(CookerBean cooker);

    List<CookerBean> insertCookers(List<CookerBean> cookers);

    CookerBean deleteCooker(Long cookerId);

    boolean deleteCookers();

    CookerBean queryCooker(Long cookerId);

    List<CookerBean> queryCookers();

    CookerBean updateCooker(CookerBean cooker);

    List<CookerBean> updateCookers(List<CookerBean> cookers);

    BookBean insertBook(BookBean book);

    List<BookBean> insertBooks(List<BookBean> books);

    BookBean deleteBook(Long bookId);

    boolean deleteBooks();

    BookBean queryBook(Long bookId);

    List<BookBean> queryBooks();

    BookBean updateBook(BookBean book);

    List<BookBean> updateBooks(List<BookBean> books);
}
