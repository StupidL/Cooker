package me.stupidme.cooker.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    protected RecyclerView mRecyclerView;

    protected List<BookBean> mDataSet;

    protected BookRecyclerAdapter mAdapter;

    protected CookerService mService;

//    public BookBaseFragment() {
//        // Required empty public constructor
//    }
//
//    public static BookBaseFragment newInstance() {
//        BookNowFragment fragment = new BookNowFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

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
//
//    protected abstract boolean onRecyclerItemMove(RecyclerView recyclerView,
//                                                  RecyclerView.ViewHolder viewHolder,
//                                                  RecyclerView.ViewHolder target);
//
//    protected abstract void onRecyclerItemSwiped(RecyclerView.ViewHolder viewHolder, int direction);
//
    protected abstract void setItemTouchHelperCallback();

    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setItemTouchHelperCallback();

//        ItemTouchHelper.Callback callback =
//                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
//                        ItemTouchHelper.LEFT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
//                                          RecyclerView.ViewHolder target) {
//
//                        int fromPos = viewHolder.getAdapterPosition();
//                        int toPos = target.getAdapterPosition();
//                        if (fromPos < toPos) {
//                            //分别把中间所有的item的位置重新交换
//                            for (int i = fromPos; i < toPos; i++) {
//                                Collections.swap(mAdapter.getDataSet(), i, i + 1);
//                            }
//                        } else {
//                            for (int i = fromPos; i > toPos; i--) {
//                                Collections.swap(mAdapter.getDataSet(), i, i - 1);
//                            }
//                        }
//
//                        mAdapter.notifyItemMoved(fromPos, toPos);
//
//                        return true;
//                        return onRecyclerItemMove(recyclerView, viewHolder, target);
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        int position = viewHolder.getAdapterPosition();
//                        mDataSet.remove(position);
//                        mAdapter.notifyItemRemoved(position);
//                        onRecyclerItemSwiped(viewHolder, direction);
//                    }
//                };
//
//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//        helper.attachToRecyclerView(mRecyclerView);

    }

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
