package me.stupidme.cooker.presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.DbManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.status.StatusView;

/**
 * Created by StupidL on 2017/4/5.
 */

public class StatusPresenterImpl implements StatusPresenter {

    private StatusView mView;

    private DbManager mDbManager;

    private CookerService mService;

    public StatusPresenterImpl(StatusView view) {
        mView = view;
        mDbManager = DbManagerImpl.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void cancelBook(long bookId) {
        mView.showDialog(true);
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        mService.deleteBook(userId, bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value == null
                                || value.getData() == null
                                || value.getResultCode() != 200
                                || value.getData().get(0) == null) {
                            mView.onCancelFailed();
                            return;
                        }
                        boolean success = mDbManager.deleteBook(DbManager.KEY_BOOK_ID, bookId);
                        if (!success) {
                            mView.onCancelFailed();
                            return;
                        }
                        BookBean bookBean = value.getData().get(0);
                        CookerBean cookerBean = new CookerBean();
                        cookerBean.setUserId(bookBean.getUserId());
                        cookerBean.setCookerId(bookBean.getCookerId());
                        cookerBean.setCookerName(bookBean.getCookerName());
                        cookerBean.setCookerLocation(bookBean.getCookerLocation());
                        cookerBean.setCookerStatus(bookBean.getCookerStatus());
                        boolean success2 = mDbManager.updateCooker(cookerBean);
                        if (!success2) {
                            mView.onCancelFailed();
                            return;
                        }
                        mView.onCancelSuccess(bookBean);
                        mView.removeItem(bookBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.onCancelFailed();
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                    }
                });
    }

    @Override
    public void loadBooks() {
        List<BookBean> list = mDbManager.queryBooks(DbManager.KEY_USER_ID,
                SharedPreferenceUtil.getAccountUserId(0L));
        List<BookBean> data = new ArrayList<>();
        list.parallelStream()
                .filter(b -> "booking".toUpperCase().equals(b.getCookerStatus().toUpperCase()))
                .forEach(data::add);
        mView.acceptData(data);
    }
}
