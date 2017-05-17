package me.stupidme.cooker.view.about;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.custom.SpaceItemDecoration;

public class AboutActivity extends AppCompatActivity {

    private Dialog mDialog;

    private RecyclerView mRecyclerView;

    private AboutRecyclerAdapter mAdapter;

    private List<BaseAboutBean> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_about);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> mDialog.show());

        mDataSet = new ArrayList<>();
        mAdapter = new AboutRecyclerAdapter(mDataSet);

        mRecyclerView = (RecyclerView) findViewById(R.id.about_recycler_view);
        initRecyclerView();

        ContentHelper helper = new ContentHelper(this);
        mDataSet.addAll(helper.contents());
        mAdapter.notifyDataSetChanged();
        Log.v("AboutActivity", "data set size: " + mDataSet.size());
    }

    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        ItemTouchHelper.Callback callback =
//                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
//                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//                        int fromPos = viewHolder.getAdapterPosition();
//                        int toPos = target.getAdapterPosition();
//                        if (fromPos < toPos) {
//                            //分别把中间所有的item的位置重新交换
//                            for (int i = fromPos; i < toPos; i++) {
//                                Collections.swap(mDataSet, i, i + 1);
//                            }
//                        } else {
//                            for (int i = fromPos; i > toPos; i--) {
//                                Collections.swap(mDataSet, i, i - 1);
//                            }
//                        }
//                        mAdapter.notifyItemMoved(fromPos, toPos);
//                        return true;
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//                    }
//                };
//
//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//        helper.attachToRecyclerView(mRecyclerView);
    }
}
