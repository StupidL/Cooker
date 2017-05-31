package me.stupidme.cooker.view.book;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.BookMockPresenterImpl;
import me.stupidme.cooker.presenter.BookPresenter;
import me.stupidme.cooker.presenter.BookPresenterImpl;
import me.stupidme.cooker.util.ToastUtil;
import me.stupidme.cooker.view.custom.SpaceItemDecoration;
import me.stupidme.cooker.view.detail.BookDetailActivity;

/**
 * A base fragment to show books.
 * This class is <code>abstract</code> and sub class must override {@link #setItemTouchHelperCallback}
 * to custom item behavior on recycler view.
 */

public abstract class BookBaseFragment extends Fragment implements BookView {

    protected RecyclerView mRecyclerView;

    protected List<BookBean> mDataSet;

    protected BookRecyclerAdapter mAdapter;

    protected BookPresenter mPresenter;

    protected SwipeRefreshLayout mSwipeLayout;

    protected FloatingActionButton mFab;

    protected TextView mEmptyView;

    protected ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSet = new ArrayList<>();
        mAdapter = new BookRecyclerAdapter(mDataSet);
//        mPresenter = new BookPresenterImpl(this);
        mPresenter = new BookMockPresenterImpl(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getActivity().getString(R.string.fragment_book_dialog_title));
        mProgressDialog.setMessage(getString(R.string.fragment_cooker_dialog_tips));

        mAdapter.setOnItemClickListener((position, bookBean) -> {
            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
            intent.putExtra("userId", bookBean.getUserId());
            intent.putExtra("bookId", bookBean.getBookId());
            intent.putExtra("cookerId", bookBean.getCookerId());
            intent.putExtra("cookerName", bookBean.getCookerName());
            intent.putExtra("cookerLocation", bookBean.getCookerLocation());
            intent.putExtra("cookerStatus", bookBean.getCookerStatus());
            intent.putExtra("peopleCount", bookBean.getPeopleCount());
            intent.putExtra("riceWeight", bookBean.getRiceWeight());
            intent.putExtra("taste", bookBean.getTaste());
            intent.putExtra("time", bookBean.getTime());
            startActivity(intent);
        });
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

//    @Override
//    public void onViewCreated(View view, Bundle bundle) {
//        mPresenter.queryBooksFromDB();
//        Log.v(getClass().getCanonicalName(), "onViewCreated()");
//    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.queryBooksFromDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(getClass().getCanonicalName(), "mPresenter dispose...");
        Log.v(getClass().getCanonicalName(), "onDestroy()");
    }

    /**
     * abstract method that sub class must override to determine item's behavior on recycler view.
     */
    protected abstract void setItemTouchHelperCallback();

    protected void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setItemTouchHelperCallback();
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
    public void showDialog(boolean show) {
        if (show) {
            mProgressDialog.setTitle(getString(R.string.fragment_book_dialog_title));
            mProgressDialog.setMessage(getString(R.string.fragment_cooker_dialog_tips));
            mProgressDialog.show();
            return;
        }
        mProgressDialog.dismiss();
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
    public void removeBook(Long bookId) {
        Iterator<BookBean> iterator = mDataSet.iterator();
        while (iterator.hasNext()) {
            BookBean bookBean = iterator.next();
            if (bookBean.getBookId() == (long) bookId)
                iterator.remove();
        }
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
    public void showMessage(int what, CharSequence message) {
        switch (what) {
            case BookPresenter.MESSAGE_DELETE_BOOK_ERROR:
            case BookPresenter.MESSAGE_INSERT_BOOK_ERROR:
            case BookPresenter.MESSAGE_QUERY_BOOK_ERROR:
                showToastShort(message);
                break;

            case BookPresenter.MESSAGE_DELETE_BOOK_SUCCESS:
            case BookPresenter.MESSAGE_DELETE_BOOK_SUCCESS_BUT_EMPTY:
            case BookPresenter.MESSAGE_INSERT_BOOK_SUCCESS:
            case BookPresenter.MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY:
            case BookPresenter.MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY:
                break;

            case BookPresenter.MESSAGE_INSERT_BOOK_DB_FAILED:
                showToastShort("Insert Book DB failed.");
                break;
            case BookPresenter.MESSAGE_INSERT_BOOK_FAILED:
                showToastShort("Insert Book failed.");
                break;
            case BookPresenter.MESSAGE_QUERY_BOOK_FAILED:
                showToastShort("Query Book failed.");
                break;
            case BookPresenter.MESSAGE_UPDATE_BOOK_DB_FAILED:
                showToastShort("Update Book DB failed.");
                break;
            case BookPresenter.MESSAGE_UPDATE_COOKER_FAILED:
                showToastShort("Update Book failed.");
                break;
            case BookPresenter.MESSAGE_DELETE_BOOK_DB_FAILED:
                showToastShort("Delete Book DB failed.");
                break;
            case BookPresenter.MESSAGE_DELETE_BOOK_FAILED:
                showToastShort("Delete Book failed.");
                break;
        }
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

    private void showToastShort(CharSequence message) {
        ToastUtil.showToastShort(getActivity(), message);
    }

}
