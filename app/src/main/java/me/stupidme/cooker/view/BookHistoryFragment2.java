package me.stupidme.cooker.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookHistoryFragment2 extends BookBaseFragment {

    public BookHistoryFragment2() {

    }

    public static BookHistoryFragment2 newInstance() {
        BookHistoryFragment2 fragment2 = new BookHistoryFragment2();
        Bundle bundle = new Bundle();
        fragment2.setArguments(bundle);
        return fragment2;
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
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }
}
