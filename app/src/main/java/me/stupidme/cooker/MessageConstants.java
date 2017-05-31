package me.stupidme.cooker;

/**
 * Created by stupidl on 17-5-31.
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

    public static final int MESSAGE_QUERY_SERVER_COOKER_SUCCESS_RETURN_EMPTY = 0x43;

    public static final int MESSAGE_INSERT_BOOK_ERROR = 0x10;
    public static final int MESSAGE_INSERT_BOOK_FAILED = 0x11;
    public static final int MESSAGE_INSERT_BOOK_SUCCESS = 0x12;
    public static final int MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY = 0x13;
    public static final int MESSAGE_INSERT_BOOK_DB_FAILED = 0x14;
    public static final int MESSAGE_INSERT_BOOK_HISTORY_FAILED = 0x15;

    public static final int MESSAGE_DELETE_BOOK_ERROR = 0x20;
    public static final int MESSAGE_DELETE_BOOK_FAILED = 0x21;
    public static final int MESSAGE_DELETE_BOOK_SUCCESS = 0x22;
    public static final int MESSAGE_DELETE_BOOK_SUCCESS_BUT_EMPTY = 0x23;
    public static final int MESSAGE_DELETE_BOOK_DB_FAILED = 0x24;
    public static final int MESSAGE_DELETE_BOOK_HISTORY_FAILED = 0x25;

    public static final int MESSAGE_QUERY_BOOK_ERROR = 0x30;
    public static final int MESSAGE_QUERY_BOOK_FAILED = 0x33;
    public static final int MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY = 0x32;

    public static final int MESSAGE_UPDATE_BOOK_DB_FAILED = 0x41;
}
