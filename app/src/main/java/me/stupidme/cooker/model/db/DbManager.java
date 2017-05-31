package me.stupidme.cooker.model.db;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/29.
 * <p>
 * This interface defines all the operation about local database.
 */

public interface DbManager {

    /**
     * Name of table "cooker".
     */
    String TABLE_NAME_COOKER = "cooker";

    /**
     * Name of table "book".
     */
    String TABLE_NAME_BOOK = "book";

    /**
     * Name of table "book_history".
     */
    String TABLE_NAME_HISTORY = "book_history";

    /**
     * Field name "userId".
     */
    String KEY_USER_ID = "userId";

    /**
     * Filed name "bookId".
     */
    String KEY_BOOK_ID = "bookId";

    /**
     * Field name "cookerId".
     */
    String KEY_COOKER_ID = "cookerId";

    /**
     * Field name "cookerName".
     */
    String KEY_COOKER_NAME = "cookerName";

    /**
     * Field name "cookerLocation".
     */
    String KEY_COOKER_LOCATION = "cookerLocation";

    /**
     * Field name "cookerStatus".
     */
    String KEY_COOKER_STATUS = "cookerStatus";

    /**
     * Field name "peopleCount".
     */
    String KEY_BOOK_PEOPLE_COUNT = "peopleCount";

    /**
     * Field name "riceWeight".
     */
    String KEY_BOOK_RICE_WEIGHT = "riceWeight";

    /**
     * Filed name "taste".
     */
    String KEY_BOOK_TASTE = "taste";

    /**
     * Field name "time".
     */
    String KEY_BOOK_TIME = "time";

    /**
     * Insert a cooker record.
     *
     * @param cooker cooker
     * @return true if insertion is success
     */
    boolean insertCooker(CookerBean cooker);

    /**
     * Insert cooker records.
     *
     * @param cookers cookers
     * @return true if insertion is success
     */
    boolean insertCookers(List<CookerBean> cookers);

    /**
     * Delete a cooker record if condition matches.
     *
     * @param where   field in local db
     * @param equalTo value of field
     * @return true if deletion is success
     */
    boolean deleteCooker(String where, Long equalTo);

    /**
     * Delete cooker records if condition matches.
     *
     * @param where   field of local db
     * @param equalTo value of the field
     * @return true if deletion is success
     */
    boolean deleteCookers(String where, Long equalTo);

    /**
     * Query a cooker record according condition.
     *
     * @param where   field in local db
     * @param equalTo value of the field
     * @return an instance of {@link CookerBean} if query success, or null if failed.
     */
    CookerBean queryCooker(String where, Long equalTo);

    /**
     * Query a cooker record according condition.
     *
     * @param where   field in local db
     * @param equalTo value of the field
     * @return an instance of {@link CookerBean} if query success, or null if failed.
     */
    CookerBean queryCooker(String where, String equalTo);

    /**
     * Query cooker records according condition.
     *
     * @param where   field in local db
     * @param equalTo value of the field
     * @return A collection of instances of {@link CookerBean} if query success, or null or empty collection if failed.
     */
    List<CookerBean> queryCookers(String where, Long equalTo);

    /**
     * Update a cooker record.
     *
     * @param cooker cooker
     * @return true if update success
     */
    boolean updateCooker(CookerBean cooker);

    /**
     * Update cooker records.
     *
     * @param cookers cookers
     * @return true if update success, false if failed. And transaction rolls back if failed.
     */
    boolean updateCookers(List<CookerBean> cookers);

    /**
     * Insert a book.
     *
     * @param book book
     * @return true if insertion success
     */
    boolean insertBook(BookBean book);

    /**
     * Insert book records
     *
     * @param books collection of book
     * @return true if insertion is success and transaction rolls back if failed.
     */
    boolean insertBooks(List<BookBean> books);

    /**
     * Delete a book record.
     *
     * @param where  field in local db table
     * @param bookId value of the field
     * @return true if deletion is success
     */
    boolean deleteBook(String where, Long bookId);

    /**
     * Delete all record of the table.
     *
     * @param where   field in local db table
     * @param equalTo value of the field
     * @return true if success
     */
    boolean deleteBooks(String where, Long equalTo);

    /**
     * Query a book record.
     *
     * @param where   field in local db
     * @param equalTo value of field
     * @return instance of {@link BookBean} if query success, or null if failed
     */
    BookBean queryBook(String where, Long equalTo);

    /**
     * Query book records
     *
     * @param where   field in local db
     * @param equalTo value of field
     * @return collection of book if query success, or null or empty collection if failed
     */
    List<BookBean> queryBooks(String where, Long equalTo);

    /**
     * Query book records
     *
     * @param where   field in local db
     * @param equalTo value of field
     * @return collection of book if query success, or null or empty collection if failed
     */
    List<BookBean> queryBooks(String where, String equalTo);

    /**
     * Update a book record.
     *
     * @param book book
     * @return true if success
     */
    boolean updateBook(BookBean book);

    /**
     * Update book records.
     *
     * @param books books
     * @return true if success
     */
    boolean updateBooks(List<BookBean> books);

    /**
     * Query all of user's cooker.
     *
     * @param where   field in local db
     * @param equalTo value of the field
     * @return collection of cooker names
     */
    List<String> queryCookerNamesAll(String where, Long equalTo);

    /**
     * Insert a book record to history table.
     *
     * @param book book
     * @return true if success
     */
    boolean insertBookHistory(BookBean book);

    /**
     * Insert book records to history table.
     *
     * @param books books
     * @return true if success, and transaction rolls back if failed
     */
    boolean insertBooksHistory(List<BookBean> books);

    /**
     * Delete a book record to history table.
     *
     * @param where   field in local db
     * @param equalTo value of field
     * @return true if success
     */
    boolean deleteBookHistory(String where, Long equalTo);

    /**
     * Delete book records to history table.
     *
     * @param where   field in local db
     * @param equalTo value of field
     * @return true if success
     */
    boolean deleteBooksHistory(String where, Long equalTo);

    /**
     * Query a book record in history table.
     *
     * @param where   field in local db
     * @param equalTo value of the field
     * @return an instance of {@link BookBean} if success, or null if failed
     */
    BookBean queryBookHistory(String where, Long equalTo);

    /**
     * Query book records in history table.
     *
     * @param where   field in local db
     * @param equalTo value of the field
     * @return collection of instance of {@link BookBean} if success, or null or empty collection if failed
     */
    List<BookBean> queryBooksHistory(String where, Long equalTo);

    /**
     * Update a book record to history table.
     *
     * @param book book
     * @return true if success
     */
    boolean updateBookHistory(BookBean book);

    /**
     * Update book records to history table
     *
     * @param books books
     * @return true if success, and transaction rolls back if failed
     */
    boolean updateBooksHistory(List<BookBean> books);
}


