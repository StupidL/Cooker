package me.stupidme.cooker.view.book;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.BookPresenter;
import me.stupidme.cooker.widget.BookRecyclerAdapter;
import me.stupidme.cooker.widget.SpaceItemDecoration;

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
     * 进度条。进行网络请求的时候显示
     */
    protected ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSet = new ArrayList<>();
        mAdapter = new BookRecyclerAdapter(mDataSet);
        mPresenter = BookPresenter.getInstance(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getResources().getString(R.string.title_book_fragment_progress));
        mProgressDialog.setMessage(getResources().getString(R.string.message_book_fragment_dialog));
        mProgressDialog.setCancelable(false);
        Log.v(getClass().getCanonicalName(), "onCreate()");

        mPresenter.queryBooksFromDB();
        Log.v(getClass().getCanonicalName(), "DataSet Size: " + mDataSet.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_recycler_view);
        initRecyclerView();

        Log.v(getClass().getCanonicalName(), "onCreateView()");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
//        mPresenter.queryBooksFromDB();
        Log.v(getClass().getCanonicalName(), "onViewCreated()");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.v(getClass().getCanonicalName(), "onCreateOptionsMenu()");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v(getClass().getCanonicalName(), "onOptionsItemSelected()");

        if (item.getItemId() == R.id.book_fragment_refresh) {

            Map<String, String> map = new ArrayMap<>();

            //TODO inflate map


            mPresenter.queryBooksFromServer(map);

            return true;
        }
        return false;
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

        Log.v(getClass().getCanonicalName(), "initRecyclerView()");
    }

    @Override
    public void showProgressDialog(boolean show) {
        if (show) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    public void removeBook(BookBean book) {
        int position = mDataSet.indexOf(book);
        mDataSet.remove(book);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void insertBook(BookBean book) {
        mDataSet.add(0, book);
        mAdapter.notifyItemInserted(0);
    }

    @Override
    public void insertBooks(List<BookBean> list) {
//        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateBook(int position, BookBean book) {
        mDataSet.remove(position);
        mDataSet.add(position, book);
        mAdapter.notifyItemInserted(position);
    }

    @Override
    public void updateBooks(List<BookBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
