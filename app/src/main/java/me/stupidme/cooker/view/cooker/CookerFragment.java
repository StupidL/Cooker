package me.stupidme.cooker.view.cooker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;
import me.stupidme.cooker.db.StupidDBHelper;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.presenter.CookerPresenter;
import me.stupidme.cooker.view.BookAddActivity;
import me.stupidme.cooker.view.CookerDialog;
import me.stupidme.cooker.widget.CookerRecyclerAdapter;
import me.stupidme.cooker.widget.SpaceItemDecoration;

/**
 * Created by StupidL on 2017/3/5
 * CookerFragment展示的是用户所有的电饭锅的信息
 */

public class CookerFragment extends Fragment implements ICookerView, CookerDialog.CookerAddListener {

    //请求码，启动添加预约界面
    private static final int REQUEST_CODE_ADD_BOOK = 0x04;

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
        mPresenter = CookerPresenter.getInstance(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getResources().getString(R.string.title_cooker_fragment_progress));
        mProgressDialog.setMessage(getResources().getString(R.string.message_cooker_fragment_dialog));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cooker, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initRecyclerView();

        // init fab
        final FloatingActionMenu menu = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        FloatingActionButton fabCooker = (FloatingActionButton) view.findViewById(R.id.fab_cooker);
        FloatingActionButton fabBook = (FloatingActionButton) view.findViewById(R.id.fab_book);
        fabCooker.setOnClickListener(v -> {

            if (mDialog == null) {
                mDialog = new CookerDialog(getActivity(), CookerFragment.this);
            }
            mDialog.setTitle(R.string.title_dialog_cooker);
            mDialog.show();
            menu.close(true);
        });

        fabBook.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), BookAddActivity.class), REQUEST_CODE_ADD_BOOK);
            menu.close(true);
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mPresenter.queryCookersFromDB();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cooker_fragment_refresh) {

            Map<String, String> map = new ArrayMap<>();

            //TODO inflate map


            mPresenter.queryCookersFromServer(map);

            return true;
        }
        return false;
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
                        CookerBean bean = mDataSet.get(position);
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        Snackbar.make(viewHolder.itemView,
                                getString(R.string.snackbar_text_cooker_fragment),
                                500)
                                .setAction("DELETE", v -> {
                                    mPresenter.deleteCooker(bean);
                                });

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
        cooker.setName(name);
        cooker.setLocation(loc);
        cooker.setStatus(StupidDBHelper.COOKER_STATUS_FREE);

        mPresenter.insertCooker(cooker);
    }

    @Override
    public void showProgressDialog(boolean show) {
        if (show) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    @Override
    public void removeCooker(CookerBean cooker) {
        int position = mDataSet.indexOf(cooker);
        mDataSet.remove(cooker);
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void insertCooker(CookerBean cooker) {
        mDataSet.add(0, cooker);
        mAdapter.notifyItemInserted(0);
    }

    @Override
    public void insertCookers(List<CookerBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCooker(int position, CookerBean cooker) {
        mDataSet.remove(position);
        mDataSet.add(position, cooker);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void updateCookers(List<CookerBean> list) {
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
