package me.stupidme.cooker.view.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.stupidme.cooker.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, SearchFragment.newInstance())
                .commit();
    }
}
