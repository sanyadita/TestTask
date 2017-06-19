package com.sandradita.testapptopostindustria.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.sandradita.testapptopostindustria.R;
import com.sandradita.testapptopostindustria.ui.fragments.FavouritesFragment;
import com.sandradita.testapptopostindustria.ui.fragments.NearbyFragment;
import com.sandradita.testapptopostindustria.ui.fragments.SearchFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(drawerListener);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String searchQuery = intent.getStringExtra(SearchFragment.ARG_SEARCH_QUERY);
        if (searchQuery != null) {
            navigationView.setCheckedItem(R.id.nav_search);
            changeFragment(SearchFragment.newInstance(searchQuery));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.nav_search:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.nav_nearby:
                fragmentClass = NearbyFragment.class;
                break;
            case R.id.nav_favourites:
                fragmentClass = FavouritesFragment.class;
                break;
        }
        changeFragment(fragmentClass);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grants) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grants);
                }
            }
        }
    }

    /**
     * Replaces shows fragment created by selected class, if fragment by selected class wasn't added.
     *
     * @param fragmentClass class of fragment that should be created.
     */
    private void changeFragment(@Nullable Class fragmentClass) {
        if (fragmentClass == null) return;

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
        if (fragment == null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                changeFragment(fragment);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Replaces fragment
     */
    private void changeFragment(@Nullable Fragment fragment) {
        if (fragment == null) return;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment, fragment.getClass().getSimpleName()).commit();
    }

    /**
     * Listener overrides method {@link android.support.v4.widget.DrawerLayout.DrawerListener#onDrawerOpened(View)}
     * to hide keyboard when drawer is opened.
     */
    private final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    };

}
