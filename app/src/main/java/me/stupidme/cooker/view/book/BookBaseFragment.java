package me.stupidme.cooker.view.book;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.BookPresenter;
import me.stupidme.cooker.view.SpaceItemDecoration;
import me.stupidme.cooker.view.login.Constants;

/**
 * Created by StupidL on 2017/3/8.
 */

public abstract class BookBaseFragment extends Fragment implements IBookView {

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
    private SwipeRefreshLayout mSwipeLayout;

    /**
     * 添加预约悬浮按钮
     */
    protected FloatingActionButton mFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDataSet == null)
            mDataSet = new ArrayList<>();
        if (mAdapter == null)
            mAdapter = new BookRecyclerAdapter(mDataSet);
        mPresenter = new BookPresenter(this);

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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mPresenter.queryBooksFromDB();
        Log.v(getClass().getCanonicalName(), "onViewCreated()");
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

    /**
     * 刷新控件显示与否
     *
     * @param show true则显示，false则不显示
     */
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

    /**
     * 界面上移除一个预定
     *
     * @param book 要移除的预约项目
     */
    @Override
    public void removeBook(BookBean book) {
        int position = mDataSet.indexOf(book);
        mDataSet.remove(book);
        mAdapter.notifyItemRemoved(position);
    }

    /**
     * 界面上要插入一个预约信息
     *
     * @param book 要插入的项目
     */
    @Override
    public void insertBook(BookBean book) {
        mDataSet.add(0, book);
        mAdapter.notifyItemInserted(0);
    }

    /**
     * 批量插入预约信息，在从数据库获取数据的时候调用，
     * 要先清空已有的信息，再全部加入，避免数据重复
     *
     * @param list 项目列表
     */
    @Override
    public void insertBooks(List<BookBean> list) {
        if (mDataSet == null)
            mDataSet = new ArrayList<>();
        mDataSet.clear();
        mDataSet.addAll(list);
        if (mAdapter == null)
            mAdapter = new BookRecyclerAdapter(mDataSet);
        mAdapter.notifyDataSetChanged();
        Log.v(getClass().getCanonicalName(), "insert List Size: " + list.size());
    }

    /**
     * 更新某个具体的预约信息
     *
     * @param position 预约项在适配器的位置
     * @param book     更新后的预约项
     */
    @Override
    public void updateBook(int position, BookBean book) {
        mDataSet.remove(position);
        mDataSet.add(position, book);
        mAdapter.notifyItemInserted(position);
    }

    /**
     * 批量更新预约信息，再从服务器获取数据的时候调用，
     * 要先清空列表，再加入，避免数据重复
     *
     * @param list 项目列表
     */
    @Override
    public void updateBooks(List<BookBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 弹出Toast信息
     *
     * @param message 信息内容
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public long getUserId() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.COOKER_USER_LOGIN, Context.MODE_PRIVATE);
        return preferences.getLong(Constants.USER_ID, 0L);
    }
}
