package me.stupidme.cooker.db;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/29.
 */

public interface IDBManager {

    boolean insertCooker(CookerBean cooker);

    boolean insertCookers(List<CookerBean> cookers);

    boolean deleteCooker(long cookerId);

    boolean deleteCookers();

    CookerBean queryCooker(long cookerId);

    List<CookerBean> queryCookers();

    boolean updateCooker(CookerBean cooker);

    boolean updateCookers(List<CookerBean> cookers);

    boolean insertBook(BookBean book);

    boolean insertBooks(List<BookBean> books);

    boolean deleteBook(long bookId);

    boolean deleteBooks();

    BookBean queryBook(long bookId);

    List<BookBean> queryBooks();

    boolean updateBook(BookBean book);

    boolean updateBooks(List<BookBean> books);
}
