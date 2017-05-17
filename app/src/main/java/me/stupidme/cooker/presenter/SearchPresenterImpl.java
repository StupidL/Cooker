package me.stupidme.cooker.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.DbManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.search.SearchView;

/**
 * Created by StupidL on 2017/5/15.
 */

public class SearchPresenterImpl implements SearchPresenter {

    private ExecutorService mExecutorService;

    private DbManager mDbManager;

    private SearchView mSearchView;

    private List<String> mDataSet;

    private BookSearchCallable mBookCallable;

    private CookerSearchCallable mCookerCallable;

    public SearchPresenterImpl(SearchView searchView) {
        mSearchView = searchView;
        mDbManager = DbManagerImpl.getInstance();
        mDataSet = new ArrayList<>();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void search(String input) {
        if (mBookCallable == null)
            mBookCallable = new BookSearchCallable();
        if (mCookerCallable == null)
            mCookerCallable = new CookerSearchCallable();

        mBookCallable.setKeyWords(input);
        Future<List<BookBean>> future = mExecutorService.submit(mBookCallable);
        try {
            List<BookBean> list = future.get(3, TimeUnit.SECONDS);
            if (list == null || list.size() <= 0) {
                return;
            }


        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        mCookerCallable.setKeyWords(input);
        Future<List<CookerBean>> cookerFuture = mExecutorService.submit(mCookerCallable);

        try {
            List<CookerBean> cookers = cookerFuture.get(3, TimeUnit.SECONDS);
            if (cookers == null || cookers.size() <= 0) {
                return;
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    private class BookSearchCallable implements Callable<List<BookBean>> {

        private String mKeyWords;

        BookSearchCallable() {

        }

        public void setKeyWords(String input) {
            mKeyWords = input;
        }

        @Override
        public List<BookBean> call() throws Exception {
            if (mKeyWords == null || mKeyWords.isEmpty()) {
                return null;
            }
            List<BookBean> result;
            result = mDbManager.queryBooks(DbManager.KEY_USER_ID, SharedPreferenceUtil.getAccountUserId(0L));
            result.addAll(mDbManager.queryBooksHistory(DbManager.KEY_USER_ID,
                    SharedPreferenceUtil.getAccountUserId(0L)));

            return null;
        }
    }

    private class CookerSearchCallable implements Callable<List<CookerBean>> {

        private String mKeyWords;

        public CookerSearchCallable() {

        }

        public void setKeyWords(String input) {
            mKeyWords = input;
        }

        @Override
        public List<CookerBean> call() throws Exception {
            return null;
        }
    }
}
