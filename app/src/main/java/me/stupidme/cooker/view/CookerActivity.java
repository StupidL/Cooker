package me.stupidme.cooker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import me.stupidme.cooker.R;

public class CookerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_ADD_COOKER = 0x03;
    private static final int REQUEST_CODE_ADD_BOOK = 0x04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFab();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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

    private void initFab() {
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        FloatingActionButton fabCooker = (FloatingActionButton) findViewById(R.id.fab_cooker);
        FloatingActionButton fabBook = (FloatingActionButton) findViewById(R.id.fab_book);
        fabCooker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CookerActivity.this, CookerAddActivity.class), REQUEST_CODE_ADD_COOKER);
            }
        });

        fabBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CookerActivity.this, BookAddActivity.class), REQUEST_CODE_ADD_BOOK);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:

                break;
            case R.id.nav_book:
                Intent i = new Intent(CookerActivity.this, BookActivity.class);
                startActivity(i);
                break;
            case R.id.nav_about:

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
