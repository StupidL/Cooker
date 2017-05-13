package me.stupidme.cooker.model.db;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/29.
 */

public interface DbManager {

    String TABLE_NAME_COOKER = "cooker";
    String TABLE_NAME_BOOK = "book";
    String KEY_USER_ID = "userId";
    String KEY_BOOK_ID = "bookId";
    String KEY_COOKER_ID = "cookerId";
    String KEY_COOKER_NAME = "cookerName";
    String KEY_COOKER_LOCATION = "cookerLocation";
    String KEY_COOKER_STATUS = "cookerStatus";
    String KEY_BOOK_PEOPLE_COUNT = "peopleCount";
    String KEY_BOOK_RICE_WEIGHT = "riceWeight";
    String KEY_BOOK_TASTE = "taste";
    String KEY_BOOK_TIME = "time";

    boolean insertCooker(CookerBean cooker);

    boolean insertCookers(List<CookerBean> cookers);

    boolean deleteCooker(String where, Long cookerId);

    boolean deleteCookers(String where, Long equalTo);

    CookerBean queryCooker(String where, Long equalTo);

    CookerBean queryCooker(String where, String equalTo);

    List<CookerBean> queryCookers(String where, Long equalTo);

    boolean updateCooker(CookerBean cooker);

    boolean updateCookers(List<CookerBean> cookers);

    boolean insertBook(BookBean book);

    boolean insertBooks(List<BookBean> books);

    boolean deleteBook(String where, Long bookId);

    boolean deleteBooks(String where, Long equalTo);

    BookBean queryBook(String where, Long bookId);

    List<BookBean> queryBooks(String where, Long equalTo);

    boolean updateBook(BookBean book);

    boolean updateBooks(List<BookBean> books);

    List<String> queryCookerNamesAll(String where, Long equalTo);
}
