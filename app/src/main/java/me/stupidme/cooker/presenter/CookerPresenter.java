package me.stupidme.cooker.presenter;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/7.
 * <p>
 * Presenter of CookerFragment. Manage all cooker related operations.
 */

public interface CookerPresenter {

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
