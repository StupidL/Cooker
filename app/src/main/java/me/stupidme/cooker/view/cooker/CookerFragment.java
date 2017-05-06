package me.stupidme.cooker.view.cooker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.presenter.CookerMockPresenterImpl;
import me.stupidme.cooker.presenter.CookerPresenter;
import me.stupidme.cooker.view.custom.SpaceItemDecoration;

/**
 * Created by StupidL on 2017/3/5
 * <p>
 * This fragment is to manage all cooker devices. You can create a cooker and delete it.
 * All the operations is synchronized with server.
 */

public class CookerFragment extends Fragment implements CookerView, CookerDialog.CookerAddListener {

    private static final String TAG = "CookerFragment";

    //RecyclerView控件，展示所有的Cooker设备信息
    private RecyclerView mRecyclerView;

    //存放所有设备信息的集合
    private List<CookerBean> mDataSet;

    //RecyclerView的适配器，决定每一个子项目的行为和外观
    private CookerRecyclerAdapter mAdapter;

    //Presenter，控制网络请求和数据库读写
    private CookerPresenter mPresenter;

    //对话框，在添加设备的时候会显示
    private CookerDialog mDialog;

    //下拉刷新控件
    private SwipeRefreshLayout mSwipeLayout;

    private FloatingActionButton mFab;

    private TextView mEmptyView;

    private ProgressDialog mProgressDialog;

    public CookerFragment() {
        // Required empty public constructor
    }

    public static CookerFragment newInstance() {
        CookerFragment fragment = new CookerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDataSet = new ArrayList<>();
        mAdapter = new CookerRecyclerAdapter(mDataSet);
//        mPresenter = new CookerPresenter(this);
        mPresenter = new CookerMockPresenterImpl(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.fragment_cooker_dialog_titile));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cooker, container, false);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.cooker_swipe_layout);

        mSwipeLayout.setOnRefreshListener(() -> mPresenter.queryCookersFromServer());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();

        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(v -> {
            if (mDialog == null) {
                mDialog = new CookerDialog(getActivity(), CookerFragment.this);
            }
            mDialog.setTitle(R.string.title_dialog_cooker);
            mDialog.show();
        });

        mEmptyView = (TextView) view.findViewById(R.id.empty_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mPresenter.queryCookersFromDB();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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
                        CookerBean bean = mDataSet.get(position);
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        Long cookerId = bean.getCookerId();

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
                                    mPresenter.deleteCooker(cookerId);
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
    public void onSave(Map<String, String> map) {

        mProgressDialog.setMessage(getString(R.string.fragment_cooker_create_cooker));
        mProgressDialog.show();

        String name = map.get(CookerDialog.COOKER_NAME_KEY);
        String loc = map.get(CookerDialog.COOKER_LOCATION_KEY);
        String id = map.get(CookerDialog.COOKER_ID_KEY);

        CookerBean cooker = new CookerBean();
        cooker.setCookerName(name);
        cooker.setCookerLocation(loc);
        cooker.setCookerId(Long.parseLong(id));
        cooker.setCookerStatus("free");

        mPresenter.insertCooker(cooker);
    }

    @Override
    public void showRefreshing(boolean show) {
        Log.v("CookerFragment", "setRefresh()...");
        if (show) {
            if (!mSwipeLayout.isRefreshing())
                mSwipeLayout.setRefreshing(true);
        } else {
            if (mSwipeLayout.isRefreshing())
                mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void removeCooker(CookerBean cooker) {
        int position = mDataSet.indexOf(cooker);
        mDataSet.remove(cooker);
        mAdapter.notifyItemRemoved(position);
        showEmptyView(mDataSet.size() <= 0);
    }

    @Override
    public void removeCookers(List<CookerBean> cookers) {
        mDataSet.removeAll(cookers);
        mAdapter.notifyDataSetChanged();
        if (mDataSet.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void insertCooker(CookerBean cooker) {
        mProgressDialog.dismiss();
        Log.v(TAG, "insert cooker: " + cooker.toString());
        mDataSet.add(0, cooker);
        mAdapter.notifyItemInserted(0);
        showEmptyView(mDataSet.size() <= 0);
    }

    @Override
    public void insertCookers(List<CookerBean> cookers) {
        updateDataSet(cookers);
    }

    @Override
    public void insertCookersFromDB(List<CookerBean> list) {
        updateDataSet(list);
    }

    @Override
    public void updateCooker(int position, CookerBean cooker) {
        mDataSet.remove(position);
        mDataSet.add(position, cooker);
        mAdapter.notifyItemChanged(position);
        showEmptyView(mDataSet.size() <= 0);
    }

    @Override
    public void updateCookersFromServer(List<CookerBean> list) {
        updateDataSet(list);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateDataSet(List<CookerBean> cookers) {
        mDataSet.clear();
        mDataSet.addAll(cookers);
        mAdapter.notifyDataSetChanged();
        showEmptyView(mDataSet.size() <= 0);
    }

    private void showEmptyView(boolean show) {
        if (show) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

}
