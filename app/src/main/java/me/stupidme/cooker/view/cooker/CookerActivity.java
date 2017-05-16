package me.stupidme.cooker.view.cooker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupidme.cooker.R;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.about.AboutActivity;
import me.stupidme.cooker.view.book.BookActivity;
import me.stupidme.cooker.view.feedback.FeedbackHelper;
import me.stupidme.cooker.view.login.Constants;
import me.stupidme.cooker.view.login.LoginActivity;
import me.stupidme.cooker.view.search.SearchActivity;
import me.stupidme.cooker.view.settings.SettingsActivity;

public class CookerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_BOOK = 0x02;
    private static final int REQUEST_CODE_ABOUT = 0x05;
    private static final int REQUEST_CODE_USER = 0x07;

    CircleImageView mCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView);
        mCircleImageView = (CircleImageView) view.findViewById(R.id.user_head);
        mCircleImageView.setOnClickListener(v -> {
            startActivity(new Intent(CookerActivity.this, UserActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        });
        TextView name = (TextView) view.findViewById(R.id.user_name);
        name.setText(SharedPreferenceUtil.getAccountUserName("Cooker"));

        CookerFragment cookerFragment = CookerFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cookerFragment)
                .commit();

        Log.i("CookerActivity", "Activity onCreate()");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:

                break;
            case R.id.nav_book:
                Intent i = new Intent(CookerActivity.this, BookActivity.class);
                startActivityForResult(i, REQUEST_CODE_BOOK);
                break;
            case R.id.nav_search:
                startActivity(new Intent(CookerActivity.this, SearchActivity.class));
                break;
            case R.id.nav_about:
                startActivityForResult(new Intent(CookerActivity.this, AboutActivity.class), REQUEST_CODE_ABOUT);
                break;
            case R.id.nav_feedback:
                FeedbackHelper helper = new FeedbackHelper.Builder(this)
                        .colorPrimary(getResources().getColor(R.color.colorPrimary))
                        .colorPrimaryDark(getResources().getColor(R.color.colorPrimaryDark))
                        .emailTo("562117676@qq.com")
                        .build();
                helper.start();
                break;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(CookerActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.nav_exit:
                Intent intent = new Intent(CookerActivity.this, LoginActivity.class);
                intent.setAction(Constants.ACTION_EXIT_ACCOUNT);
                startActivity(intent);
                CookerActivity.this.finish();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
