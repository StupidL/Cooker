package me.stupidme.cooker.mock;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;

/**
 * This interface defines all methods to manage server database.
 */

public interface ServerDbManager {

    /**
     * table name 'user_server'
     */
    String TABLE_NAME_USER_SERVER = "user_server";

    /**
     * table column string 'usrId'
     */
    String KEY_TABLE_USER_ID = "userId";

    /**
     * table column string 'userName'
     */
    String KEY_TABLE_USER_NAME = "userName";

    /**
     * table column string 'userPassword'
     */
    String KEY_TABLE_USER_PASSWORD = "userPassword";

    /**
     * table name 'cooker_server'
     */
    String TABLE_NAME_COOKER_SERVER = "cooker_server";

    /**
     * table name 'book_server'
     */
    String TABLE_NAME_BOOK_SERVER = "book_server";

    /**
     * table column string 'userId'
     */
    String KEY_USER_ID_SERVER = "userId";

    /**
     * table column string 'bookId'
     */
    String KEY_BOOK_ID_SERVER = "bookId";

    /**
     * table column string 'cookerId'
     */
    String KEY_COOKER_ID_SERVER = "cookerId";

    /**
     * table column string 'cookerName'
     */
    String KEY_COOKER_NAME_SERVER = "cookerName";

    /**
     * table column string 'cookerLocation'
     */
    String KEY_COOKER_LOCATION_SERVER = "cookerLocation";

    /**
     * table column string 'cookerStatus'
     */
    String KEY_COOKER_STATUS_SERVER = "cookerStatus";

    /**
     * table column string 'peopleCount'
     */
    String KEY_BOOK_PEOPLE_COUNT_SERVER = "peopleCount";

    /**
     * table column string 'riceWeight'
     */
    String KEY_BOOK_RICE_WEIGHT_SERVER = "riceWeight";

    /**
     * table column string 'taste'
     */
    String KEY_BOOK_TASTE_SERVER = "taste";

    /**
     * table column string 'time'
     */
    String KEY_BOOK_TIME_SERVER = "time";

    /**
     * insert a user into server db.
     *
     * @param userBean user
     * @return user
     */
    UserBean insertUser(UserBean userBean);

    /**
     * query a user by condition.
     *
     * @param where   table column string
     * @param equalTo value of column
     * @return user
     */
    UserBean queryUser(String where, Long equalTo);

    /**
     * update a user.
     *
     * @param userBean user
     * @return user
     */
    UserBean updateUser(UserBean userBean);

    /**
     * insert a cooker.
     *
     * @param cooker cooker
     * @return cooker
     */
    CookerBean insertCooker(CookerBean cooker);

    /**
     * insert cookers.
     *
     * @param cookers cookers
     * @return cookers
     */
    List<CookerBean> insertCookers(List<CookerBean> cookers);

    /**
     * delete a cooker.
     *
     * @param userId   user's id
     * @param cookerId cooker's id
     * @return cooker
     */
    CookerBean deleteCooker(Long userId, Long cookerId);

    /**
     * delete cookers.
     *
     * @param userId user's id
     * @return cookers
     */
    List<CookerBean> deleteCookers(Long userId);

    /**
     * query cooker.
     *
     * @param userId   user's id
     * @param cookerId cooker's id
     * @return cooker
     */
    CookerBean queryCooker(Long userId, Long cookerId);

    /**
     * query cookers.
     *
     * @param userId user's id
     * @return cookers
     */
    List<CookerBean> queryCookers(Long userId);

    /**
     * update a cooker.
     *
     * @param cooker cooker
     * @return cooker
     */
    CookerBean updateCooker(CookerBean cooker);

    /**
     * update cookers.
     *
     * @param cookers cookers
     * @return cookers
     */
    List<CookerBean> updateCookers(List<CookerBean> cookers);

    /**
     * insert book.
     *
     * @param book book
     * @return book
     */
    BookBean insertBook(BookBean book);

    /**
     * insert books.
     *
     * @param books books
     * @return books
     */
    List<BookBean> insertBooks(List<BookBean> books);

    /**
     * delete a book.
     *
     * @param userId user's id
     * @param bookId book's id
     * @return book
     */
    BookBean deleteBook(Long userId, Long bookId);

    /**
     * delete books.
     *
     * @param userId user's id
     * @return book
     */
    List<BookBean> deleteBooks(Long userId);

    /**
     * query a book.
     *
     * @param userId user's id
     * @param bookId book's id
     * @return book
     */
    BookBean queryBook(Long userId, Long bookId);

    /**
     * query books.
     *
     * @param userId user'd id
     * @return books
     */
    List<BookBean> queryBooks(Long userId);

    /**
     * update book.
     *
     * @param book book
     * @return book
     */
    BookBean updateBook(BookBean book);

    /**
     * update books.
     *
     * @param books books.
     * @return books
     */
    List<BookBean> updateBooks(List<BookBean> books);
}
