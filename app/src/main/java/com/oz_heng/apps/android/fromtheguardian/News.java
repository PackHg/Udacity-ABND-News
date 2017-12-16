package com.oz_heng.apps.android.fromtheguardian;

import android.graphics.Bitmap;

/**
 * {@link News} class consisting of data we're interested in.
 */
public class News {
    private String title;
    private String date;
    private String url;
    private Bitmap thumbnailImage;
    private String section;

    public News(String title, String date, String url, Bitmap thumbnailImage,
                String section) {
        this.title = title;
        this.date = date;
        this.url = url;
        this.thumbnailImage = thumbnailImage;
        this.section = section;
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

    public Bitmap getThumbnailBitmap() {
        return thumbnailImage;
    }

    public String getSection() {
        return section;
    }
}
