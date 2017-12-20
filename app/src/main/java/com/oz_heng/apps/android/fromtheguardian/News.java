package com.oz_heng.apps.android.fromtheguardian;

import android.graphics.Bitmap;

/**
 * {@link News} class consisting of news data we're interested in.
 */
class News {
    private String title;
    private String authors;
    private String date;
    private String url;
    private Bitmap thumbnailImage;
    private String section;

    News(String title, String authors, String date, String url, Bitmap thumbnailImage,
                String section) {
        this.title = title;
        this.authors = authors;
        this.date = date;
        this.url = url;
        this.thumbnailImage = thumbnailImage;
        this.section = section;
    }

    String getTitle() {
        return title;
    }

    String getAuthors() {
        return authors;
    }

    String getDate() {
        return date;
    }

    String getUrl() {
        return url;
    }

    Bitmap getThumbnailBitmap() {
        return thumbnailImage;
    }

    String getSection() {
        return section;
    }
}
