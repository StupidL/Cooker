package me.stupidme.cooker.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.stupidme.cooker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookAddFragment extends Fragment {

    public BookAddFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_add, container, false);
    }
}
