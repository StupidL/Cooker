package me.stupidme.cooker.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.R;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.CookerModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.widget.CookerRecyclerAdapter;
import me.stupidme.cooker.widget.SpaceItemDecoration;

/**
 * Created by StupidL on 2017/3/9.
 */

public class CookerFragment2 extends Fragment implements CookerDialog.CookerAddListener {

    //请求码，跳转到添加预约界面
    private static final int REQUEST_CODE_ADD_BOOK = 0x04;

    //RecyclerView，用来展示所有的设备信息
    private RecyclerView mRecyclerView;

    //存放所有的设备信息
    private static List<CookerBean> mDataSet;

    //适配器
    private static CookerRecyclerAdapter mAdapter;

    //添加一个设备的对话框
    private CookerDialog mCookerDialog;

    //推送数据到服务器时展示的对话框
    private ProgressDialog mProgressDialog;

    //网络服务
    private CookerService mService;

    //本地数据库服务
    private CookerModel mModel;

    private CookerHandler mHandler = new CookerHandler(CookerFragment2.this);

    private static final int MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED = 0xb1;

    private static List<CookerBean> mLocalList;

    private Runnable testLocalDataRunnable = () -> {
        //从本地数据库加载
//        mLocalList = mModel.queryCookers();

        //模拟加载
        if (mLocalList == null)
            mLocalList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CookerBean cooker = new CookerBean();
            cooker.setName("Cooker" + i);
            cooker.setLocation("Location" + i);
            cooker.setStatus("free");
            mLocalList.add(cooker);
        }

        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED));
    };

    public CookerFragment2() {

    }

    public static CookerFragment2 newInstance() {
        CookerFragment2 fragment2 = new CookerFragment2();
        Bundle args = new Bundle();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSet = new ArrayList<>();
        mAdapter = new CookerRecyclerAdapter(mDataSet);
        mService = CookerRetrofit.getInstance().getCookerService();
        mModel = CookerModel.getInstance();
        initProgressDialog();

        Log.v("CookerFragment2", "onCreated()");
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

            if (mCookerDialog == null) {
                mCookerDialog = new CookerDialog(getActivity(), CookerFragment2.this);
            }
            mCookerDialog.setTitle(R.string.title_dialog_cooker);
            mCookerDialog.show();
            menu.close(true);
        });

        fabBook.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), BookAddActivity.class),
                    REQUEST_CODE_ADD_BOOK);
            menu.close(true);
        });

        Log.v("CookerFragment2", "onCreateView()");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        new Thread(testLocalDataRunnable).start();
        Log.v("CookerFragment2", "onViewCreated()");
    }


    @Override
    public void onSave(Map<String, String> map) {

        mProgressDialog.show();

        CookerBean cooker = new CookerBean();
        //TODO 将map转化为CookerBean对象

        //将新设备传到服务器
//        mService.postNewDevice(cooker)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<CookerBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(CookerBean value) {
//                        mModel.insertCooker(value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mProgressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mProgressDialog.dismiss();
//                    }
//                });
    }


    /**
     * 加载对话框，不能取消
     */
    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getResources().getString(R.string.title_cooker_fragment_progress));
        mProgressDialog.setMessage(getResources().getString(R.string.message_cooker_fragment_dialog));
        mProgressDialog.setCancelable(false);
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
                        CookerBean cooker = mDataSet.get(position);
                        mDataSet.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        Snackbar.make(viewHolder.itemView,
                                getString(R.string.snackbar_text_cooker_fragment),
                                Snackbar.LENGTH_LONG)
                                .setAction("CANCEL", v -> {
                                    mDataSet.add(position, cooker);
                                    mAdapter.notifyItemInserted(position);
                                });

                        //TODO remove this cooker on server if user dit not cancel it

                    }
                };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    private static class CookerHandler extends Handler {

        WeakReference<Fragment> weakReference;

        CookerHandler(Fragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message message) {
            Fragment f = weakReference.get();
            if (f != null) {
                handleAllMessage(message);
            }
        }

        private void handleAllMessage(Message message) {
            switch (message.what) {
                case MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED:
                    mDataSet.clear();
                    mDataSet.addAll(mLocalList);
                    mAdapter.notifyDataSetChanged();
                    Log.v("CookerHandler", "Message what: " + message.what);
                    break;
            }
        }
    }
}
