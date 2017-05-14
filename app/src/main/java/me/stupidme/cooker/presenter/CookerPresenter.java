package me.stupidme.cooker.presenter;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/7.
 * <p>
 * Presenter of CookerFragment. Manage all cooker related operations.
 */

public interface CookerPresenter {

    /**
     * error occurs when request sever to delete a cooker.
     */
    int MESSAGE_DELETE_COOKER_ERROR = 0x10;

    /**
     * failed to delete a cooker from activity or fragment, but network has response.
     */
    int MESSAGE_DELETE_COOKER_FAILED = 0x11;

    /**
     * failed to delete a cooker from db, but network has response.
     */
    int MESSAGE_DELETE_DB_COOKER_FAILED = 0x12;


    /**
     * error occurs when request sever to insert a cooker.
     */
    int MESSAGE_INSERT_COOKER_ERROR = 0x20;

    /**
     * failed to insert a cooker from activity or fragment, but network has response.
     */
    int MESSAGE_INSERT_COOKER_FAILED = 0x21;

    /**
     * failed to insert a cooker from db, but network has response.
     */
    int MESSAGE_INSERT_DB_COOKER_FAILED = 0x22;

    /**
     * error occurs when request sever to update a cooker.
     */
    int MESSAGE_UPDATE_COOKER_ERROR = 0x30;

    /**
     * failed to update a cooker for activity or fragment, but network has response.
     */
    int MESSAGE_UPDATE_COOKER_FAILED = 0x31;

    /**
     * failed to update a cooker for db, but network has response.
     */
    int MESSAGE_UPDATE_DB_COOKER_FAILED = 0x32;

    /**
     * failed to query cooker(s) from server.
     */
    int MESSAGE_QUERY_SERVER_COOKER_FAILED = 0x40;

    /**
     * error occurs when request server to query cooker(s).
     */
    int MESSAGE_QUERY_SERVER_COOKER_ERROR = 0x41;

    /**
     * success to query a cooker from server, but the result may be empty.
     */
    int MESSAGE_QUERY_SERVER_COOKER_SUCCESS = 0x42;

    /**
     * Delete a cooker for user both in db and server.
     *
     * @param cookerId id of cooker
     */
    void deleteCooker(long cookerId);

    /**
     * Delete all cookers of user both in db and server.
     */
    void deleteCookers();

    /**
     * Insert a cooker for user both in db and server.
     *
     * @param bean cooker
     */
    void insertCooker(CookerBean bean);

    /**
     * Query a specified cooker from server for user, and update db at the same time.
     *
     * @param cookerId id of cooker
     */
    void queryCookerFromDB(long cookerId);

    /**
     * Fetch all cookers for user in db.
     */
    void queryCookersFromDB();

    /**
     * Update a specified cooker's info for user both in server and db.
     *
     * @param position adapter position
     * @param bean     cooker
     */
    void updateCooker(int position, CookerBean bean);

    /**
     * Query a specified cooker from server, and update cooker in db at the same time.
     *
     * @param position adapter position
     * @param cookerId id of cooker
     */
    void queryCookerFromServer(int position, long cookerId);

    /**
     * Query all cookers from server for user, and update cookers in db at the same time.
     */
    void queryCookersFromServer();
}
