package me.stupidme.cooker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import me.stupidme.cooker.R;
import me.stupidme.cooker.presenter.BookPresenter;

/**
 * Created by StupidL on 2017/3/6.
 * 增加预定界面
 */

public class BookAddActivity extends AppCompatActivity implements IBookAddView {

    private Spinner mCookerName;

    private Spinner mCookeTaste;

    private EditText mPeopleCount;

    private EditText mRiceWeight;

    private EditText mCookTime;

    private BookPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        mCookerName = (Spinner) findViewById(R.id.book_spinner_name);
        mCookeTaste = (Spinner) findViewById(R.id.book_spinner_taste);
        mPeopleCount = (EditText) findViewById(R.id.book_et_people);
        mRiceWeight = (EditText) findViewById(R.id.book_et_rice);
        mCookTime = (EditText) findViewById(R.id.book_et_time);

        mPresenter = new BookPresenter(this);

        Log.i("BookAddActivity", "Activity onCreate()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            tryAddBook();
        }
        return true;
    }

    private void tryAddBook() {

    }

    @Override
    public void addBookSuccess() {

    }

    @Override
    public void addBokFailed() {

    }
}
