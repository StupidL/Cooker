package me.stupidme.cooker.view.book;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/5
 */

public class BookHistoryFragment extends BookBaseFragment {

    public BookHistoryFragment() {
        // Required empty public constructor
    }

    public static BookHistoryFragment newInstance() {
        BookHistoryFragment fragment = new BookHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mFab = (FloatingActionButton) view.findViewById(R.id.fab);
            mFab.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    protected void setItemTouchHelperCallback() {
        ItemTouchHelper.Callback callback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {

                        int fromPos = viewHolder.getAdapterPosition();
                        int toPos = target.getAdapterPosition();
                        if (fromPos < toPos) {
                            //分别把中间所有的item的位置重新交换
                            for (int i = fromPos; i < toPos; i++) {
                                Collections.swap(mAdapter.getDataSet(), i, i + 1);
                            }
                        } else {
                            for (int i = fromPos; i > toPos; i--) {
                                Collections.swap(mAdapter.getDataSet(), i, i - 1);
                            }
                        }

                        mAdapter.notifyItemMoved(fromPos, toPos);

                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        BookBean bean = mDataSet.get(position);
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        new AlertDialog.Builder(viewHolder.itemView.getContext())
                                .setMessage(getString(R.string.snackbar_text_cooker_fragment))
                                .setTitle(getString(R.string.tips_title))
                                .setNegativeButton("CANCEL", (dialog12, which) -> {
                                    dialog12.dismiss();
                                    mDataSet.add(position, bean);
                                    mAdapter.notifyItemInserted(position);
                                })
                                .setPositiveButton("DELETE", (dialog1, which) -> {
                                    dialog1.dismiss();
                                    mPresenter.deleteBookHistory(bean);
                                })
                                .show();
                    }
                };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }
}
