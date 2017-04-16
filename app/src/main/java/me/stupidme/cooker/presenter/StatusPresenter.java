package me.stupidme.cooker.presenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.db.DBManager;
import me.stupidme.cooker.model.db.IDBManager;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.status.IStatusView;

/**
 * Created by StupidL on 2017/4/5.
 */

public class StatusPresenter implements IStatusPresenter {

    private IStatusView mView;

    private IDBManager mManager;

    private CookerService mService;

    public StatusPresenter(IStatusView view) {
        mView = view;
        mManager = DBManager.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void cancelBook(long bookId) {
        mService.deleteBook(SharedPreferenceUtil.getAccountUserId(0L), bookId)
                .subscribeOn(Schedulers.io())
                .doOnNext(bean -> {
                    bean.setCookerStatus("finished");
                    mManager.updateBook(bean);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookBean value) {
                        mView.removeBook(value.getBookId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showMessage("Cancel Success!");
                    }
                });
    }

//    @Override
//    public void updateBook(long bookId) {
//
//    }

}
