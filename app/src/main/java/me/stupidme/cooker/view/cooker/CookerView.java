package me.stupidme.cooker.view.cooker;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/9.
 */

public interface CookerView {

    /**
     * Control refresh view show or dismiss
     *
     * @param show show if true.
     */
    void showRefreshing(boolean show);

    /**
     * Remove a specified cooker shown in activity or fragment.
     *
     * @param cooker cooker
     */
    void removeCooker(CookerBean cooker);

    /**
     * Remove a cooker shown in activity or fragment by cookerId.
     *
     * @param cookerId id of cooker
     */
    void removeCooker(Long cookerId);

    /**
     * Remove cookers listed in activity or fragment.
     *
     * @param cookers cookers
     */
    void removeCookers(List<CookerBean> cookers);

    /**
     * Insert a single cooker to a activity or fragment.
     *
     * @param cooker cooker
     */
    void insertCooker(CookerBean cooker);

    /**
     * Insert cookers to a activity or fragment.
     *
     * @param cookers cookers
     */
    void insertCookers(List<CookerBean> cookers);

    /**
     * Update a specified cooker.
     *
     * @param position position in adapter
     * @param cooker   updated cooker
     */
    void updateCooker(int position, CookerBean cooker);

    /**
     * Toast a message in activity or fragment.
     *
     * @param what    message type
     * @param message message content
     */
    void showMessage(int what, CharSequence message);

    /**
     * Show a tips dialog
     *
     * @param show show dialog if true
     */
    void showDialog(boolean show);

}
