package me.stupidme.cooker.db;

import java.util.List;

import io.reactivex.Observable;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/20.
 */

public interface IDBManager {

    Observable<Boolean> insertCooker(CookerBean cooker);

    Observable<Boolean> insertCookers(List<CookerBean> cookers);

    Observable<Boolean> deleteCooker(long cookerId);

    Observable<Boolean> deleteCookers();

    Observable<CookerBean> queryCooker(long cookerId);

    Observable<List<CookerBean>> queryCookers();

    Observable<Boolean> updateCooker(CookerBean cooker);

    Observable<Boolean> updateCookers(List<CookerBean> cookers);

    Observable<Boolean> insertBook(BookBean book);

    Observable<Boolean> insertBooks(List<BookBean> books);

    Observable<Boolean> deleteBook(long bookId);

    Observable<Boolean> deleteBooks();

    Observable<BookBean> queryBook(long bookId);

    Observable<List<BookBean>> queryBooks();

    Observable<Boolean> updateBook(BookBean book);

    Observable<Boolean> updateBooks(List<BookBean> books);
}
