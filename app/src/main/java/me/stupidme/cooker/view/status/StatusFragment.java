package me.stupidme.cooker.view.status;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.IStatusPresenter;
import me.stupidme.cooker.presenter.StatusPresenter;
import me.stupidme.cooker.view.custom.SpaceItemDecoration;
import me.stupidme.cooker.view.base.BaseFragment;
import me.stupidme.cooker.view.cooker.CookerActivity;

public class StatusFragment extends BaseFragment implements IStatusView {

    private RecyclerView mRecyclerView;

    private StatusRecyclerAdapter mAdapter;

    private List<BookBean> mDataSet;

    private IStatusPresenter mPresenter;

    public StatusFragment() {
        // Required empty public constructor
    }

    public static StatusFragment newInstance() {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataSet = new ArrayList<>();
        mAdapter = new StatusRecyclerAdapter(mDataSet);
        mPresenter = new StatusPresenter(this);
        mAdapter.setPresenter(mPresenter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_status;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Button home = (Button) view.findViewById(R.id.status_home);
        Button exit = (Button) view.findViewById(R.id.status_exit);

        home.setOnClickListener(v -> startActivity(new Intent(getActivity(), CookerActivity.class)));
        exit.setOnClickListener(v -> getActivity().finish());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.status_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);

    }

//
//    @Override
//    public void updateBook(BookBean book) {
//        int position = -1;
//        if (mDataSet.size() > 0) {
//            for (BookBean b : mDataSet) {
//                if (b.getBookId() == book.getBookId()) {
//                    position = mDataSet.indexOf(b);
//                }
//            }
//        }
//
//        if (position != -1) {
//            mDataSet.remove(position);
//            mDataSet.add(position, book);
//            mAdapter.notifyItemChanged(position);
//        }
//    }

    @Override
    public void removeBook(long bookId) {
        int position = -1;
        if (mDataSet.size() > 0) {
            for (BookBean b : mDataSet) {
                if (b.getBookId() == bookId)
                    position = mDataSet.indexOf(b);
            }
        }

        if (position != -1) {
            mDataSet.remove(position);
            mAdapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void showMessage(CharSequence msg) {
        showToast(msg);
    }
}
