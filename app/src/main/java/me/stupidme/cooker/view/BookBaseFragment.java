package me.stupidme.cooker.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.widget.BookRecyclerAdapter;
import me.stupidme.cooker.widget.SpaceItemDecoration;

/**
 * Created by StupidL on 2017/3/8.
 */

public abstract class BookBaseFragment extends Fragment implements BookActivity.OnRefreshBookInfoListener {

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
     * 网络服务
     */
    protected CookerService mService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDataSet == null)
            mDataSet = new ArrayList<>();
        if (mAdapter == null)
            mAdapter = new BookRecyclerAdapter(mDataSet);
        mService = CookerRetrofit.getInstance().getCookerService();

        Log.v(getClass().getCanonicalName(), "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_recycler_view);
        initRecyclerView();
        return view;
    }

    /**
     * 为RecyclerView设置Item回调，子类必须实现该抽象方法，以达到各自想要的效果
     */
    protected abstract void setItemTouchHelperCallback();

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(100));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setItemTouchHelperCallback();

    }

    /**
     * 回调监听。当有新数据来到时，刷新界面
     *
     * @param list 数据集合
     */
    @Override
    public void onRefresh(List<BookBean> list) {
        if (mDataSet == null)
            mDataSet = new ArrayList<>();
        if (mAdapter == null)
            mAdapter = new BookRecyclerAdapter(mDataSet);
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
        Log.v(getClass().getCanonicalName(), "onRefresh()");
    }
}
