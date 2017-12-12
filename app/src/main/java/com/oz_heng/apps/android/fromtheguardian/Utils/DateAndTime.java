package com.oz_heng.apps.android.fromtheguardian.Utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper class with static methods for handling date and time.
 */
public final class DateAndTime {
    private static String LOG_TAG = DateAndTime.class.getSimpleName();

    private DateAndTime() {}

    /**
     * Convert to a date string to a {@link Date} with UTC time zone.
     * Return null if the {@param s} is null or empty.
     *
     * @param s
     * @return
     */
    public static Date stringToDate(String s) {
        SimpleDateFormat dateFormatUTC = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'" /** ISO-8601 format */,
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
     * @param date
     * @return
     */
    public static String dateToDateString(Date date) {
        DateFormat df = new SimpleDateFormat("MMM dd", Locale.getDefault());;
        return date != null ? df.format(date) : "";
    }

    /**
     * Return a time string from a {@link Date} with the format "h:mm a".
     *
     * @param date
     * @return
     */
    public static String dateToTimeString(Date date) {
        DateFormat tf = new SimpleDateFormat("h:mm a", Locale.getDefault());;
        return date != null ? tf.format(date) : "";
    }

    /**
     * Add days (int) to today and return a date string in the format "yyyy-MM-dd".
     *
     * @param days (int)
     * @return (String) in the format "yyyy-MM-dd"
     */
    public static String addDaysToCurrentDate(int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DATE, days);
        return df.format(calendar.getTime());
    }
}
