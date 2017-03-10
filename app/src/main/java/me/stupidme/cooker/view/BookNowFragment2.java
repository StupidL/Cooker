package me.stupidme.cooker.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.view.book.BookBaseFragment;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookNowFragment2 extends BookBaseFragment {

    public BookNowFragment2() {

    }

    public static BookNowFragment2 newInstance() {
        BookNowFragment2 fragment2 = new BookNowFragment2();
        Bundle args = new Bundle();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    protected void setItemTouchHelperCallback() {
        ItemTouchHelper.Callback callback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {
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
                        BookBean bookBean = mDataSet.get(position);
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        Snackbar.make(viewHolder.itemView,
                                getResources().getString(R.string.snackbar_text_book_now),
                                Snackbar.LENGTH_LONG)
                                .setAction("CANCEL", v -> {
                                    mDataSet.add(position, bookBean);
                                    mAdapter.notifyItemInserted(position);
                                }).show();

                        //TODO cancel this book on server

                    }
                };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }
}
