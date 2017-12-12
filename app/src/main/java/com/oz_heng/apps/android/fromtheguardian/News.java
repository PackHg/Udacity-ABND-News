package com.oz_heng.apps.android.fromtheguardian;

import android.graphics.Bitmap;

/**
 * {@link News} class consisting of data we're interested in.
 */
public class News {
    private String title;
    private String date;
    private String url;
    // TODO: is thumbnailUrl required? i.e. useless once the Bitmap has been saved.
    private String thumbnailUrl;
    private Bitmap thumbnailImage;
    private String category;

    public News(String title, String date, String url,
                String thumbnailUrl, Bitmap thumbnailImage, String category) {
        this.title = title;
        this.date = date;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Bitmap getThumbnailBitmap() {
        return thumbnailImage;
    }

    public String getCategory() {
        return category;
    }
}
