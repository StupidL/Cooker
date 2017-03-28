package me.stupidme.cooker.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.cooker.CookerActivity;

public class StatusFragment extends Fragment {

    public StatusFragment() {
        // Required empty public constructor
    }

    public static StatusFragment newInstance() {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        Button home = (Button) view.findViewById(R.id.status_home);
        Button exit = (Button) view.findViewById(R.id.status_exit);

        home.setOnClickListener(v -> startActivity(new Intent(getActivity(), CookerActivity.class)));
        exit.setOnClickListener(v -> getActivity().finish());

        return view;
    }

}
