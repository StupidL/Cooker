package me.stupidme.cooker.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.presenter.CookerPresenter;
import me.stupidme.cooker.widget.CookerRecyclerAdapter;
import me.stupidme.cooker.widget.SpaceItemDecoration;

/**
 * Created by StupidL on 2017/3/5
 * CookerFragment展示的是用户所有的电饭锅的信息
 */

public class CookerFragment extends Fragment implements ICookerFragmentView, CookerObserver {

    private RecyclerView mRecyclerView;

    private List<CookerBean> mDataSet;

    private CookerRecyclerAdapter mAdapter;

    private CookerPresenter mPresenter;

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
        mDataSet = new ArrayList<>();
        mAdapter = new CookerRecyclerAdapter(mDataSet);
        mPresenter = CookerPresenter.getInstance(this);
    }

    /**
     * 初始化控件
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cooker, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();
        return view;
    }

    /**
     * 控件创建之后，从数据库加载电饭锅数据
     *
     * @param view
     * @param bundle
     */
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mPresenter.onLoadFromDataBase();
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
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
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
//                        mDataSet.remove(position);
//                        mAdapter.notifyItemRemoved(position);
                        mPresenter.onDeleteCooker(mDataSet.get(position));
                    }
                };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 界面上更新该电饭锅的信息
     *
     * @param bean     更新后的电饭锅
     * @param position 电饭锅位置
     */
    @Override
    public void updateStatusToView(int position, CookerBean bean) {

    }

    /**
     * 从界面中移除该项目
     *
     * @param bean 电饭锅
     */
    @Override
    public void removeItem(CookerBean bean) {
        mDataSet.remove(bean);
        mAdapter.notifyItemRemoved(mDataSet.indexOf(bean));
    }

    /**
     * 在界面的列表第一项插入该电饭锅信息
     *
     * @param bean 电饭锅
     */
    @Override
    public void insertItem(CookerBean bean) {
        mDataSet.add(0, bean);
        mAdapter.notifyItemInserted(0);
    }

    /**
     * 从数据库中加载所有的电饭锅信息
     *
     * @param list 电饭锅列表
     */
    @Override
    public void loadCookersFromDataBase(List<CookerBean> list) {
        mDataSet = list;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 该电饭锅的状态更新后，本观察者收到通知，并且通过mPresenter更新界面和数据库
     *
     * @param bean 电饭锅
     */
    @Override
    public void notifyObserver(int position, CookerBean bean) {
        mPresenter.onUpdateStatus(position, bean);
    }
}
