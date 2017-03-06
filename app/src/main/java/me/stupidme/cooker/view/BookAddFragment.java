package me.stupidme.cooker.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/6.
 * 增加预定界面的片段
 */

public class BookAddFragment extends Fragment {

    public BookAddFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookAddFragment", "Fragment onCreateView()");
        return inflater.inflate(R.layout.fragment_book_add, container, false);
    }
}
