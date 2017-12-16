package com.oz_heng.apps.android.fromtheguardian;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.oz_heng.apps.android.fromtheguardian.Utils.DateAndTime.addDaysToCurrentDate;
import static com.oz_heng.apps.android.fromtheguardian.Utils.FetchRemoteData.isNetworkConnected;
import static com.oz_heng.apps.android.fromtheguardian.Utils.Helper.showSnackBar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<News>> {

    /** Base URL for querying The Guardian API */
    private static final String THE_GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?";

    /** Constant value for the news loader ID */
    private static final int NEWS_LOADER_ID = 1;

    private List<News> newsList = new ArrayList<News>();
    private ListView listView;
    private NewsApdapter newsApdapter;

    /** Loading indicator */
    private View progressBar;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

         // Setup listView.
        listView = (ListView) findViewById(R.id.list_view);
        newsApdapter = new NewsApdapter(this, new ArrayList<News>());
        listView.setAdapter(newsApdapter);

        progressBar = (View) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);

        // setOnItemClickListener to open the Web link correspoding to the list
        // item the user has clicked on.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = newsApdapter.getItem(i);
                if (news != null) {
                    if (!news.getUrl().isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(news.getUrl()));
                        startActivity(intent);
                    }
                    else {
                        showSnackBar(view, getString(R.string.no_link));
                    }
                }
            }
        });

        if (isNetworkConnected(MainActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            showSnackBar(coordinatorLayout, "No Internet connection.");
        }

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        /* Number of days to add to today. Used for setting the value associated to the
         * key "from-date" when fetching data from The Guardian. By default set to -7
         * for last week. */
        final int DAYS = -7;


        Uri baseUri = Uri.parse(THE_GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section", "technology");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("order-by", "newest");

        // Set the value for "from-date" to last week (today - 7 days) by detault.
        String pastDate = addDaysToCurrentDate(DAYS);
        uriBuilder.appendQueryParameter("from-date", pastDate);

        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("api-key", "test");

        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {

        progressBar.setVisibility(View.GONE);
        newsApdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {
            newsApdapter.addAll(newsList);
        } else {
            showSnackBar(coordinatorLayout, "Now news data found.");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsApdapter.clear();
    }
}