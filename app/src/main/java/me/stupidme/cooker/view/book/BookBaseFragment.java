package me.stupidme.cooker.view.book;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.BookMockPresenterImpl;
import me.stupidme.cooker.presenter.BookPresenter;
import me.stupidme.cooker.view.custom.SpaceItemDecoration;

/**
 * Created by StupidL on 2017/3/8.
 */

public abstract class BookBaseFragment extends Fragment implements BookView {

    /**
     * RecyclerView控件，用来展示各个预约信息
     */
    protected RecyclerView mRecyclerView;

    /**
     * 预约信息集合
     */
    protected List<BookBean> mDataSet;

    /**
     * RecyclerView适配器
     */
    protected BookRecyclerAdapter mAdapter;

    /**
     * Presenter，负责网络数据请求和数据库操作
     */
    protected BookPresenter mPresenter;

    /**
     * 下拉刷新控件
     */
    protected SwipeRefreshLayout mSwipeLayout;

    /**
     * 添加预约悬浮按钮
     */
    protected FloatingActionButton mFab;

    protected TextView mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDataSet == null)
            mDataSet = new ArrayList<>();
        if (mAdapter == null)
            mAdapter = new BookRecyclerAdapter(mDataSet);
//        mPresenter = new BookPresenter(this);
        mPresenter = new BookMockPresenterImpl(this);
        Log.v(getClass().getCanonicalName(), "onCreate()");

        Log.v(getClass().getCanonicalName(), "DataSet Size: " + mDataSet.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.book_fragment_swipe_layout);

        mSwipeLayout.setOnRefreshListener(() -> mPresenter.queryBooksFromServer());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_recycler_view);
        initRecyclerView();

        Log.v(getClass().getCanonicalName(), "onCreateView()");

        mEmptyView = (TextView) view.findViewById(R.id.empty_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mPresenter.queryBooksFromDB();
        Log.v(getClass().getCanonicalName(), "onViewCreated()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
        Log.v(getClass().getCanonicalName(), "mPresenter dispose...");
        Log.v(getClass().getCanonicalName(), "onDestroy()");
    }

    /**
     * 为RecyclerView设置Item回调，子类必须实现该抽象方法，以达到各自想要的效果
     */
    protected abstract void setItemTouchHelperCallback();

    /**
     * 初始化RecyclerView
     */
    protected void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setItemTouchHelperCallback();

        Log.v(getClass().getCanonicalName(), "initRecyclerView()");
    }

    @Override
    public void setRefreshing(boolean show) {
        if (show) {
            if (!mSwipeLayout.isRefreshing())
                mSwipeLayout.setRefreshing(true);
        } else {
            if (mSwipeLayout.isRefreshing())
                mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void removeBook(BookBean book) {
        int position = mDataSet.indexOf(book);
        mDataSet.remove(book);
        mAdapter.notifyItemRemoved(position);
        showEmptyView(mDataSet.size() <= 0);
    }

    @Override
    public void insertBook(BookBean book) {
        mDataSet.add(0, book);
        mAdapter.notifyItemInserted(0);
        showEmptyView(mDataSet.size() <= 0);
    }

    @Override
    public void insertBooks(List<BookBean> list) {
        updateDataSet(list);
        Log.v(getClass().getCanonicalName(), "insert List Size: " + list.size());
    }

    @Override
    public void updateBook(int position, BookBean book) {
        mDataSet.remove(position);
        mDataSet.add(position, book);
        mAdapter.notifyItemInserted(position);
        showEmptyView(mDataSet.size() <= 0);
    }

    @Override
    public void updateBooks(List<BookBean> list) {
        updateDataSet(list);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void updateDataSet(List<BookBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
        showEmptyView(mDataSet.size() <= 0);
    }

    protected void showEmptyView(boolean show) {
        if (show) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

}
