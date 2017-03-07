package me.stupidme.cooker.view;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/7.
 */

public interface CookerObserver {

    /**
     * 通知CookerFragment，该电饭锅的状态已经更新
     *
     * @param position 电饭锅位置
     * @param bean     电饭锅
     */
    void notifyObserver(int position, CookerBean bean);
}
