package me.stupidme.cooker.presenter;

import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.CookerModel;
import me.stupidme.cooker.model.ICookerModel;
import me.stupidme.cooker.view.ICookerFragmentView;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerPresenter implements ICookerPresenter {

    private static CookerPresenter sInstance;

    private ICookerFragmentView mView;

    private ICookerModel mModel;

    private CookerPresenter(ICookerFragmentView view) {
        mView = view;
        mModel = CookerModel.getInstance();
    }

    public static CookerPresenter getInstance(ICookerFragmentView view) {
        if (sInstance == null)
            sInstance = new CookerPresenter(view);
        return sInstance;
    }

    /**
     * 删除一个电饭锅信息，同时更新界面和数据库
     *
     * @param bean 电饭锅
     */
    @Override
    public void onDeleteCooker(CookerBean bean) {
        mModel.deleteFromDataBase(bean);
        mView.removeItem(bean);
    }

    /**
     * 插入一条电饭锅信息，同时更新界面和数据库
     *
     * @param bean 电饭锅
     */
    @Override
    public void onInsertCooker(CookerBean bean) {
        mModel.insertToDataBase(bean);
        mView.insertItem(bean);
    }

    /**
     * 将数据库加载到的电饭锅信息更新到界面
     */
    @Override
    public void onLoadFromDataBase() {
        mView.loadCookersFromDataBase(mModel.loadCookersFromDataBase());
    }

    /**
     * 在界面收到更新消息之后，负责通知界面更新和数据库更新
     *
     * @param position 原来电饭锅在列表的位置
     * @param bean     电饭锅
     */
    @Override
    public void onUpdateStatus(int position, CookerBean bean) {
        mView.updateStatusToView(position, bean);
        mModel.updateStatusToDataBase(bean);
    }
}
