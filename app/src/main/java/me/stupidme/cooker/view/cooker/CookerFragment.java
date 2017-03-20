package me.stupidme.cooker.view.cooker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;
import me.stupidme.cooker.db.StupidDBHelper;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.presenter.CookerPresenter;
import me.stupidme.cooker.view.SpaceItemDecoration;
import me.stupidme.cooker.view.login.Constants;

/**
 * Created by StupidL on 2017/3/5
 * CookerFragment展示的是用户所有的电饭锅的信息
 */

public class CookerFragment extends Fragment implements ICookerView, CookerDialog.CookerAddListener {

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
        mPresenter = CookerPresenter.getInstance(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cooker, container, false);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.cooker_swipe_layout);

        mSwipeLayout.setOnRefreshListener(() -> mPresenter.queryCookersFromServer(this.getUserId()));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (mDialog == null) {
                mDialog = new CookerDialog(getActivity(), CookerFragment.this);
            }
            mDialog.setTitle(R.string.title_dialog_cooker);
            mDialog.show();
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mPresenter.queryCookersFromDB();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(40));
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

                        Snackbar.make(viewHolder.itemView,
                                getString(R.string.snackbar_text_cooker_fragment),
                                Snackbar.LENGTH_LONG)
                                .setAction("DELETE", v -> mPresenter.deleteCooker(bean)).show();

                        mDataSet.add(position, bean);
                        mAdapter.notifyItemInserted(position);
                    }
                };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

    }

    /**
     * 通过dialog添加一个电饭锅信息
     *
     * @param map 存放电饭锅信息的map
     */
    @Override
    public void onSave(Map<String, String> map) {
        String name = map.get(CookerDialog.COOKER_NAME_KEY);
        String loc = map.get(CookerDialog.COOKER_LOCATION_KEY);

        CookerBean cooker = new CookerBean();
        cooker.setCookerName(name);
        cooker.setCookerLocation(loc);
        cooker.setCookerStatus(StupidDBHelper.COOKER_STATUS_FREE);

        mPresenter.insertCooker(cooker);
    }

    /**
     * 控制刷新控件的显示
     *
     * @param show true则显示， false则不显示
     */
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

    /**
     * 界面上移除一个电饭锅设备
     *
     * @param cooker 要删除的设备
     */
    @Override
    public void removeCooker(CookerBean cooker) {
        int position = mDataSet.indexOf(cooker);
        mDataSet.remove(cooker);
        mAdapter.notifyItemRemoved(position);
    }

    /**
     * 界面上新增一个电饭锅设备
     *
     * @param cooker 要插入的设备
     */
    @Override
    public void insertCooker(CookerBean cooker) {
        mDataSet.add(0, cooker);
        mAdapter.notifyItemInserted(0);
    }

    /**
     * 界面上插入批量电饭锅设备，因为这个方法在数据库查询的时候调用，
     * 所以应该先清除已有的设备信息，然后再添加，避免数据重复
     *
     * @param list 设备信息列表
     */
    @Override
    public void insertCookersFromDB(List<CookerBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 界面上更新一个设备信息
     *
     * @param position 再列表中的位置
     * @param cooker   设备信息
     */
    @Override
    public void updateCooker(int position, CookerBean cooker) {
        mDataSet.remove(position);
        mDataSet.add(position, cooker);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * 批量更新设备信息，在从服务器获取数据的时候调用，
     * 应该先清除已有的数据，然后再添加，避免数据重复
     *
     * @param list 设备信息列表
     */
    @Override
    public void updateCookersFromServer(List<CookerBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 在界面显示Toast信息
     *
     * @param message 要显示的信息
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取用户ID，网络请求需要用户ID
     *
     * @return 用户ID
     */
    @Override
    public long getUserId() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.COOKER_USER_LOGIN, Context.MODE_PRIVATE);
        return preferences.getLong(Constants.USER_ID, 0L);
    }
}
