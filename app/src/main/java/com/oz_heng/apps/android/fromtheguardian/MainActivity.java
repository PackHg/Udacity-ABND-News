package com.oz_heng.apps.android.fromtheguardian;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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

import static com.oz_heng.apps.android.fromtheguardian.Utils.FetchRemoteData.isNetworkConnected;
import static com.oz_heng.apps.android.fromtheguardian.Utils.Helper.showSnackBar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<News>> {

    /** Keys for querying news sections of the Guardian API  */
    private static final String SECTION_WORLD = "world";
    private static final String SECTION_AUSTRALIA = "australia-news";
    private static final String SECTION_TECHNOLOGY = "technology";
    private static final String SECTION_SCIENCE = "science";
    private static final String SECTION_SPORT = "sport";

    /** Current news section */
    private String section = SECTION_WORLD;

    /** Constant value for the news loader ID */
    private static final int NEWS_LOADER_ID = 1;

    /** Tag used to save user data in SharedPreferences */
    private static final String USER_DATA = "com.oz_heng.apps.android.fromtheguardian.user_data";
    /** Key for saving the chosen news section */
    private static final String KEY_USER_SECTION = "user section";

    private ListView listView;
    private NewsApdapter newsApdapter;

    /** Loading indicator */
    private View progressBar;

    /** For saving listView state */
    private Parcelable listViewState;

    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        // Restore section from SharedPreferences.
        SharedPreferences sp = getSharedPreferences(USER_DATA, 0);
        if (sp != null) {
            section = sp.getString(KEY_USER_SECTION, section);
        }

        checkNavDrawerMenuItem();

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
    protected void onPause() {
        super.onPause();

        // Save listView state, including scroll position.
        listViewState = listView.onSaveInstanceState();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save section in SharedPreferences.
        SharedPreferences sp = getSharedPreferences(USER_DATA, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_USER_SECTION, section);
        editor.apply();
    }

    /**
     * Get a loader to reload news data.
     */
    private void reloadNewsData() {
        if (isNetworkConnected(MainActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
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
        if (id == R.id.action_refresh) {
            showSnackBar(coordinatorLayout, "Refresh");
            newsApdapter.clear();
            reloadNewsData();
            return true;
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_world) {
            section = SECTION_WORLD;
        } else if (id == R.id.nav_australia) {
            section = SECTION_AUSTRALIA;
        } else if (id == R.id.nav_technology) {
            section = SECTION_TECHNOLOGY;
        } else if (id == R.id.nav_science) {
            section = SECTION_SCIENCE;
        } else if (id == R.id.nav_sport) {
            section = SECTION_SPORT;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        reloadNewsData();
        return true;
    }

    /**
     * Check the corresponding item of NavDrawer's Menu according to
     * section.
     */
    private void checkNavDrawerMenuItem() {
        final int MENU_ITEM_SECTION_WORLD = 0;
        final int MENU_ITEM_SECTION_AUSTRALIA = 1;
        final int MENU_ITEM_SECTION_TECHNOLOGY = 2;
        final int MENU_ITEM_SECTION_SCIENCE = 3;
        final int MENU_ITEM_SECTION_SPORT = 4;

        if (section.equals(SECTION_WORLD)) {
            navigationView.getMenu().getItem(MENU_ITEM_SECTION_WORLD).setChecked(true);
        } else if (section.equals(SECTION_AUSTRALIA)) {
            navigationView.getMenu().getItem(MENU_ITEM_SECTION_AUSTRALIA).setChecked(true);
        } else if (section.equals(SECTION_TECHNOLOGY)) {
            navigationView.getMenu().getItem(MENU_ITEM_SECTION_TECHNOLOGY).setChecked(true);
        } else if (section.equals(SECTION_SCIENCE)) {
            navigationView.getMenu().getItem(MENU_ITEM_SECTION_SCIENCE).setChecked(true);
        } else if (section.equals(SECTION_SPORT)) {
            navigationView.getMenu().getItem(MENU_ITEM_SECTION_SPORT).setChecked(true);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Base URL for querying The Guardian API.
        final String THE_GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?";

        final String FORMAT_JASON = "json";
        final String ORDER_BY_NEWEST = "newest";
        final String SHOW_FIELD_THUMBNAIL = "thumbnail";
        final String API_KEY = "test";

        //Get the number of results per query from SharedPreferences.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String maxResults = sp.getString(getString(R.string.settings_max_results_key),
                getString(R.string.settings_max_results_default));

        Uri baseUri = Uri.parse(THE_GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("format", FORMAT_JASON);
        uriBuilder.appendQueryParameter("order-by", ORDER_BY_NEWEST);
        uriBuilder.appendQueryParameter("page-size", maxResults);
        uriBuilder.appendQueryParameter("show-fields", SHOW_FIELD_THUMBNAIL);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

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

        // Restore previous listView state.
        if (listViewState != null) {
            listView.onRestoreInstanceState(listViewState);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsApdapter.clear();
    }
}
