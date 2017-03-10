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
import android.view.MenuItem;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.AboutActivity;
import me.stupidme.cooker.view.LoginActivity;
import me.stupidme.cooker.view.book.BookActivity;

public class CookerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_BOOK = 0x02;
    private static final int REQUEST_CODE_ABOUT = 0x05;

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
            super.onBackPressed();
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
            case R.id.nav_about:
                startActivityForResult(new Intent(CookerActivity.this, AboutActivity.class), REQUEST_CODE_ABOUT);
                break;
            case R.id.nav_feedback:

                break;
            case R.id.nav_settings:

                break;
            case R.id.nav_exit:
                Intent intent = new Intent(CookerActivity.this, LoginActivity.class);
                intent.putExtra("isExitApp", true);
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
}
