package me.stupidme.cooker.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.BookModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;

/**
 * Created by StupidL on 2017/3/9.
 */

public class BookAddActivity2 extends AppCompatActivity {

    private ProgressDialog mDialog;

    private CookerService mService;

    private BookModel mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_add);

        mService = CookerRetrofit.getInstance().getCookerService();
        mModel = BookModel.getInstance();

        initProgressDialog();

    }

    private void initProgressDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle(getResources().getString(R.string.title_book_add_activity_progress));
        mDialog.setMessage(getResources().getString(R.string.message_book_add_activity_dialog));
        mDialog.setCancelable(false);
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> {
                    mDialog.dismiss();
                    //TODO  Stop push data to server

                });
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

        mDialog.show();

        BookBean book = new BookBean();

        mService.rxPostNewBook(book)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BookBean value) {
                        mModel.addBookToDataBase(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        mDialog.dismiss();
                    }
                });

    }
}
