package me.stupidme.cooker.view.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;

import me.stupidme.cooker.R;

public class SearchFragment extends Fragment {

    private FloatingSearchView mSearchView;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

            }
        });

        mSearchView.setOnHomeActionClickListener(() -> getActivity().finish());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
