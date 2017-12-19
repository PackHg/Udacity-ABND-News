package com.oz_heng.apps.android.fromtheguardian;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.oz_heng.apps.android.fromtheguardian.Utils.FetchRemoteData.createUrl;
import static com.oz_heng.apps.android.fromtheguardian.Utils.FetchRemoteData.getBitmapFromURL;
import static com.oz_heng.apps.android.fromtheguardian.Utils.FetchRemoteData.makeHttpRequest;

/**
 * Helper class with method to fetch data from the Guardian API.
 */
public final class TheGuardian {
    private static final String LOG_TAG = TheGuardian.class.getSimpleName();

    private TheGuardian() {}

    /**
     * Query the Guardian API and return a {@link List} of {@link News} objects.
     * Return null if {@param urlString} is empty.
     *
     * @param urlString String containing the URL.
     * @return List of News data or null.
     */
    public static List<News> fetchNewsData(String urlString) {

        if (urlString.isEmpty()) {
            return null;
        }

        // Create URL object
        URL url = createUrl(urlString);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //  Return the list of {@link Earthquake}s extracted from the JSON response
        return extractNewsfromJsonString(jsonResponse);
    }

    /**
     * Return an {@link ArrayList<News>} from a JSON string obtained from
     * the Guardian API.
     *
     * @param jsonString    a JSON string.
     * @return {@link ArrayList<News>} or null if the JSON string is null or emptu.
     */
    private static ArrayList<News> extractNewsfromJsonString(String jsonString) {
        final String EMPTY_STRING ="";

        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        // Keys for parsing.
        final String RESPONSE = "response";
        final String RESULTS = "results";
        final String PUBLICATION_DATE = "webPublicationDate";
        final String TITLE = "webTitle";
        final String SECTION_NAME = "sectionName";
        final String URL = "webUrl";
        final String FIELDS = "fields";
        final String THUMBNAIL_URL = "thumbnail";
        final String TAGS = "tags";

        ArrayList<News> newsArrayList = new ArrayList<News>();

        try {
            JSONObject base = new JSONObject(jsonString);

            JSONObject response = base.optJSONObject(RESPONSE);
            if (response == null) {
                return null;
            }

            JSONArray results = response.optJSONArray(RESULTS);
            if (results == null) {
                return null;
            }

            for (int i = 0; i < results.length(); i++) {
                JSONObject resultsItem = results.optJSONObject(i);
                if (resultsItem != null) {
                    String date = resultsItem.optString(PUBLICATION_DATE, EMPTY_STRING);
                    String title = resultsItem.optString(TITLE, EMPTY_STRING);
                    String section = resultsItem.optString(SECTION_NAME, EMPTY_STRING);
                    String url = resultsItem.optString(URL, EMPTY_STRING);

                    String thumbnailUrl;
                    Bitmap thumbnailImage = null;

                    JSONObject fields = resultsItem.optJSONObject(FIELDS);
                    if (fields != null) {
                        thumbnailUrl = fields.optString(THUMBNAIL_URL, EMPTY_STRING);
                        if (!thumbnailUrl.isEmpty()) {
                            thumbnailImage = getBitmapFromURL(thumbnailUrl);
                        }
                    }

                    StringBuilder authors = new StringBuilder();
                    JSONArray tags = resultsItem.optJSONArray(TAGS);
                    String author = EMPTY_STRING;
                    if (tags != null) {
                        for (int j = 0; j < tags.length(); j++) {
                            JSONObject tagsItem = tags.optJSONObject(j);
                            if (tagsItem != null) {
                                author = tagsItem.optString(TITLE, EMPTY_STRING);
                                if (!author.isEmpty()) {
                                    if (j == 0) {
                                        authors.append(author);
                                    } else if (j < tags.length() - 1) {
                                        authors.append(", ");
                                        authors.append(author);
                                    } else {
                                        authors.append(" and ");
                                        authors.append(author);
                                    }
                                }
                            }
                        }
                    }

                    newsArrayList.add(new News(title, authors.toString(), date, url, thumbnailImage, section));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem in parsing the JSON string", e);
        }

        return newsArrayList;
    }
}
