package me.stupidme.cooker.model.db;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public interface RealmBookManager {

    String KEY_USER_ID = "userId";
    String KEY_BOOK_ID = "bookId";
    String KEY_COOKER_ID = "cookerId";
    String KEY_COOKER_NAME = "cookerName";
    String KEY_COOKER_LOCATION = "cookerLocation";
    String KEY_COOKER_STATUS = "cookerStatus";
    String KEY_RICE_WEIGHT = "riceWeight";
    String KEY_PEOPLE_COUNT = "peopleCount";
    String KEY_BOOK_TASTE = "taste";
    String KEY_BOOK_TIME = "time";

    BookBean insertBook(BookBean bookBean);

    List<BookBean> insertBooks(List<BookBean> bookBeanList);

    List<BookBean> deleteBooks(String where, String equalTo);

    List<BookBean> deleteBooks(String where, Long equalTo);

    BookBean updateBook(BookBean bookBean);

    List<BookBean> updateBooks(List<BookBean> bookBeanList);

    List<BookBean> queryBooks(String where, String equalTo);

    List<BookBean> queryBooks(String where, Long equalTo);

}
