package me.stupidme.cooker.mock;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public interface ServerDbManager {

    String TABLE_NAME_USER_SERVER = "user_server";
    String KEY_TABLE_USER_ID = "userId";
    String KEY_TABLE_USER_NAME = "userName";
    String KEY_TABLE_USER_PASSWORD = "userPassword";

    String TABLE_NAME_COOKER_SERVER = "cooker_server";
    String TABLE_NAME_BOOK_SERVER = "book_server";
    String KEY_USER_ID_SERVER = "userId";
    String KEY_BOOK_ID_SERVER = "bookId";
    String KEY_COOKER_ID_SERVER = "cookerId";
    String KEY_COOKER_NAME_SERVER = "cookerName";
    String KEY_COOKER_LOCATION_SERVER = "cookerLocation";
    String KEY_COOKER_STATUS_SERVER = "cookerStatus";
    String KEY_BOOK_PEOPLE_COUNT_SERVER = "peopleCount";
    String KEY_BOOK_RICE_WEIGHT_SERVER = "riceWeight";
    String KEY_BOOK_TASTE_SERVER = "taste";
    String KEY_BOOK_TIME_SERVER = "time";

    UserBean insertUser(UserBean userBean);

    UserBean queryUser(String where, Long equalTo);

    UserBean updateUser(UserBean userBean);

    CookerBean insertCooker(CookerBean cooker);

    List<CookerBean> insertCookers(List<CookerBean> cookers);

    CookerBean deleteCooker(Long userId, Long cookerId);

    List<CookerBean> deleteCookers(Long userId);

    CookerBean queryCooker(Long userId, Long cookerId);

    List<CookerBean> queryCookers(Long userId);

    CookerBean updateCooker(CookerBean cooker);

    List<CookerBean> updateCookers(List<CookerBean> cookers);

    BookBean insertBook(BookBean book);

    List<BookBean> insertBooks(List<BookBean> books);

    BookBean deleteBook(Long userId, Long bookId);

    List<BookBean> deleteBooks(Long userId);

    BookBean queryBook(Long userId, Long bookId);

    List<BookBean> queryBooks(Long userId);

    BookBean updateBook(BookBean book);

    List<BookBean> updateBooks(List<BookBean> books);
}
