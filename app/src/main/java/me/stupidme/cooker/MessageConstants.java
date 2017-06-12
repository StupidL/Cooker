package me.stupidme.cooker;

/**
 * This class defines all the messages type using a int number.
 */

public class MessageConstants {

    /**
     * error occurs when request sever to delete a cooker.
     */
    public static final int MESSAGE_DELETE_SERVER_COOKER_ERROR = 0x10;

    /**
     * failed to delete a cooker from activity or fragment, but network has response.
     */
    public static final int MESSAGE_DELETE_SERVER_COOKER_FAILED = 0x11;

    /**
     * failed to delete a cooker from db, but network has response.
     */
    public static final int MESSAGE_DELETE_LOCAL_COOKER_FAILED = 0x12;

    /**
     * success to delete a cooker from server, but response is empty.
     */
    public static final int MESSAGE_DELETE_SERVER_COOKER_SUCCESS_RETURN_EMPTY = 0x13;

    /**
     * error occurs when request sever to insert a cooker.
     */
    public static final int MESSAGE_INSERT_SERVER_COOKER_ERROR = 0x20;

    /**
     * failed to insert a cooker from activity or fragment, but network has response.
     */
    public static final int MESSAGE_INSERT_SERVER_COOKER_FAILED = 0x21;

    /**
     * failed to insert a cooker from db, but network has response.
     */
    public static final int MESSAGE_INSERT_LOCAL_COOKER_FAILED = 0x22;

    /**
     * success to insert a cooker to server, but response is empty.
     */
    public static final int MESSAGE_INSERT_SERVER_COOKER_SUCCESS_RETURN_EMPTY = 0x23;

    /**
     * error occurs when request sever to update a cooker.
     */
    public static final int MESSAGE_UPDATE_SERVER_COOKER_ERROR = 0x30;

    /**
     * failed to update a cooker for activity or fragment, but network has response.
     */
    public static final int MESSAGE_UPDATE_SERVER_COOKER_FAILED = 0x31;

    /**
     * failed to update a cooker for db, but network has response.
     */
    public static final int MESSAGE_UPDATE_LOCAL_COOKER_FAILED = 0x32;

    /**
     * success to update a cooker to server, but response is empty.
     */
    public static final int MESSAGE_UPDATE_SERVER_COOKER_SUCCESS_RETURN_EMPTY = 0x33;

    /**
     * failed to query cooker(s) from server.
     */
    public static final int MESSAGE_QUERY_SERVER_COOKER_FAILED = 0x40;

    /**
     * error occurs when request server to query cooker(s).
     */
    public static final int MESSAGE_QUERY_SERVER_COOKER_ERROR = 0x41;

    /**
     * success to query a cooker from server, but the result may be empty.
     */
    public static final int MESSAGE_QUERY_SERVER_COOKER_SUCCESS = 0x42;

    /**
     * success to query a cooker from server, but response is empty.
     */
    public static final int MESSAGE_QUERY_SERVER_COOKER_SUCCESS_RETURN_EMPTY = 0x43;

    /**
     * error when insert book to server caused by network.
     */
    public static final int MESSAGE_INSERT_BOOK_ERROR = 0x50;

    /**
     * failed to insert book to server.
     */
    public static final int MESSAGE_INSERT_BOOK_FAILED = 0x51;

    /**
     * success to insert book to server.
     */
    public static final int MESSAGE_INSERT_BOOK_SUCCESS = 0x52;

    /**
     * success to insert book to server, but response is empty.
     */
    public static final int MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY = 0x53;

    /**
     * failed to insert book to table 'book' of local database.
     */
    public static final int MESSAGE_INSERT_BOOK_DB_FAILED = 0x54;

    /**
     * failed to insert book to table 'book_history' of local database.
     */
    public static final int MESSAGE_INSERT_BOOK_HISTORY_FAILED = 0x55;

    /**
     * error occurs when delete book from server caused by network errors.
     */
    public static final int MESSAGE_DELETE_BOOK_ERROR = 0x60;

    /**
     * failed to delete book from server.
     */
    public static final int MESSAGE_DELETE_BOOK_FAILED = 0x61;

    /**
     * success to delete book from server.
     */
    public static final int MESSAGE_DELETE_BOOK_SUCCESS = 0x62;

    /**
     * success to delete book from server, but response is empty.
     */
    public static final int MESSAGE_DELETE_BOOK_SUCCESS_BUT_EMPTY = 0x63;

    /**
     * failed to delete book from table 'book' of local database.
     */
    public static final int MESSAGE_DELETE_BOOK_DB_FAILED = 0x64;

    /**
     * failed to delete book from table 'book_history' of local database.
     */
    public static final int MESSAGE_DELETE_BOOK_HISTORY_FAILED = 0x65;

    /**
     * error occurs when query book from server caused by network errors.
     */
    public static final int MESSAGE_QUERY_BOOK_ERROR = 0x70;

    /**
     * failed to query book from server.
     */
    public static final int MESSAGE_QUERY_BOOK_FAILED = 0x73;

    /**
     * success to query book from server, but response is empty.
     */
    public static final int MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY = 0x72;

    /**
     * failed to update book to table 'book' of local database.
     */
    public static final int MESSAGE_UPDATE_BOOK_DB_FAILED = 0x81;
}
