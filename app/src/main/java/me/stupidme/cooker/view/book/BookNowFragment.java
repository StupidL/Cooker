package me.stupidme.cooker.view.book;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/5
 */

public class BookNowFragment extends BookBaseFragment implements BookDialog.BookDialogListener {

    private BookDialog mDialog;

    public BookNowFragment() {
        // Required empty public constructor
    }

    public static BookNowFragment newInstance() {
        BookNowFragment fragment = new BookNowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mDialog = new BookDialog(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mFab = (FloatingActionButton) view.findViewById(R.id.fab);
            mFab.setOnClickListener(v -> {
                mDialog.reset();
                mDialog.show();
            });
        }
        return view;
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
                        BookBean bean = mDataSet.get(position);
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);
//
//                        Snackbar.make(viewHolder.itemView,
//                                getString(R.string.snackbar_text_cooker_fragment),
//                                1000)
//                                .setAction("DELETE", v -> {
//                                    mPresenter.deleteBook(bean);
//                                }).show();
//
//                        mDataSet.add(position, bean);
//                        mAdapter.notifyItemInserted(position);
//

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
                                    mPresenter.deleteBook(bean);
                                })
                                .show();
                    }
                };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 5) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });
    }

    @Override
    public void insertBooks(List<BookBean> books) {
        mDataSet.clear();
        for (BookBean book : books) {
            if ("booking".equals(book.getCookerStatus()))
                mDataSet.add(book);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSave(Map<String, String> map) {
        Log.v("BookNowFragment", "onSave()");
        mPresenter.insertBook(map);
    }

    @Override
    public List<String> getCookerNames() {
        return mPresenter.queryCookerNamesFromDB();
    }
}
