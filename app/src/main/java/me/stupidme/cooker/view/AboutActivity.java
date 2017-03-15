package me.stupidme.cooker.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.stupidme.cooker.R;

public class AboutActivity extends AppCompatActivity {

    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_about);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(
//                view -> Snackbar.make(view, "This project is opened source on Github", Snackbar.LENGTH_LONG)
//                        .setAction("Github", v -> {
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            Uri content_url = Uri.parse("https://github.com/StupidL/Cooker");
//                            intent.setData(content_url);
//                            startActivity(Intent.createChooser(intent, "Please choose a browser"));
//                        }).show());

        fab.setOnClickListener(v -> mDialog.show());
    }
}
