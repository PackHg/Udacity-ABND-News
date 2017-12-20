package com.oz_heng.apps.android.fromtheguardian.Utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper class with static methods for handling date and time.
 */
public final class DateAndTime {
    private static final String LOG_TAG = DateAndTime.class.getSimpleName();

    private DateAndTime() {}

    /**
     * Convert a date string to a {@link Date} object with UTC time zone.
     * Return null if the {@param s} is null or empty.
     *
     * @param s a date String.
     * @return a {@link Date} object.
     */
    public static Date stringToDate(String s) {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'" /* ISO-8601 format */,
                Locale.ENGLISH);
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (s == null || s.isEmpty()) {
            return null;
        }

        Date date = new Date();
        try {
            date = dateFormatUTC.parse(s);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Unable to parse the string parameter to a Date.");
            e.printStackTrace();
        }
        return date;
    }


    /**
     * Return a date string from a {@link Date} using the local {@link DateFormat} with the format
     * "MMM dd".
     *
     * @param date a {@link Date} object.
     * @return a String.
     */
    public static String dateToDateString(Date date) {
        DateFormat df = new SimpleDateFormat("MMM dd", Locale.getDefault());;
        return date != null ? df.format(date) : "";
    }

    /**
     * Return a time string from a {@link Date} with the format "h:mm a".
     *
     * @param date a {@link Date} object.
     * @return a String.
     */
    public static String dateToTimeString(Date date) {
        DateFormat tf = new SimpleDateFormat("h:mm a", Locale.getDefault());;
        return date != null ? tf.format(date) : "";
    }
}
