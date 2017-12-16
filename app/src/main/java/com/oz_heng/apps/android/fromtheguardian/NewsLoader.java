package com.oz_heng.apps.android.fromtheguardian;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import static com.oz_heng.apps.android.fromtheguardian.TheGuardian.fetchNewsData;


/**
 * Loads a list of News by using an AsyncTaskLoader to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link NewsLoader}
     * @param context Context of the activity.
     * @param url To load the data from.
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    /**
     * Is called in a backaground thread to fetch news data from The Guardian server
     * @return List of News, null if url is an empty String.
     */
    @Override
    public List<News> loadInBackground() {
        if (url.isEmpty()) {
            return null;
        }

        return fetchNewsData(url);
    }

    /**
     * Handles a request to start the Loader..
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
