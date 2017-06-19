package com.sandradita.testapptopostindustria.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sandradita.testapptopostindustria.utils.AppLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sandradita on 6/19/2017.
 */

public class TwitterDateHelper {

    private static final String TAG = "TwitterDateHelper";

    /**
     * Twitter API date format
     */
    private static final String DATE_FORMAT_FULL = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    private TwitterDateHelper() {
    }

    /**
     * Parses string in format 'EEE MMM dd HH:mm:ss ZZZZZ yyyy' to {@link Date} object.
     */
    @Nullable
    public static Date parseStringToDate(@Nullable String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FULL, Locale.ENGLISH);
        dateFormat.setLenient(true);

        if (dateStr != null) {
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                AppLogger.error(TAG, e);
            }
        }
        return null;
    }

    /**
     * Parses {@link Date} object to string in format 'yyyy-mm-dd HH:mm:ss'
     */
    @NonNull
    public static String parseDateToStringShort(@Nullable Date date) {
        if (date == null) return "";
        return SimpleDateFormat.getDateTimeInstance().format(date);
    }

    /**
     * Parses date string in format 'EEE MMM dd HH:mm:ss ZZZZZ yyyy' to format 'yyyy-mm-dd HH:mm:ss'.
     */
    @NonNull
    public static String parseLongDateToShort(@Nullable String dateStr) {
        Date date = parseStringToDate(dateStr);
        return parseDateToStringShort(date);
    }

}
