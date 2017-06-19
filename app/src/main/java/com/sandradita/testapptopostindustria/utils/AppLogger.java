package com.sandradita.testapptopostindustria.utils;

import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by sandradita on 6/16/2017.
 */

public class AppLogger {

    private static final String APP_TAG = "TweetsApp";
    private static final boolean DEBUG_MODE = true;

    private AppLogger() {
    }

    /**
     * Send a {@link Log#DEBUG DEBUG} message. As a message it uses all selected objects. They will
     * be format into a string separated by whitespace. If objects are null or its amount is 0,
     * shows "(null)" as message. Tag will be shown as "{@link AppLogger#APP_TAG APP_TAG} tag message".
     */
    public static void debugWithTag(@Nullable String tag, @Nullable Object... objects) {
        if (!DEBUG_MODE) return;

        String fullMessage = getFullMessage(tag, objects);

        Log.d(APP_TAG, fullMessage);
    }

    /**
     * Send a {@link Log#DEBUG DEBUG} message. As a message it uses all selected objects. They will
     * be format into a string separated by whitespace. If objects are null or its amount is 0,
     * shows "(null)" as message. Tag will be {@link AppLogger#APP_TAG APP_TAG}.
     */
    public static void debug(@Nullable Object... objects) {
        debugWithTag(null, objects);
    }

    /**
     * Send a {@link Log#INFO INFO} message. As a message it uses all selected objects. They will
     * be format into a string separated by whitespace. If objects are null or its amount is 0,
     * shows "(null)" as message. Tag will be shown as "{@link AppLogger#APP_TAG APP_TAG} tag message".
     */
    public static void info(@Nullable String tag, @Nullable Object... objects) {
        String fullMessage = getFullMessage(tag, objects);
        Log.i(APP_TAG, fullMessage);
    }

    /**
     * Send a {@link Log#INFO INFO} message. As a message it uses all selected objects. They will
     * be format into a string separated by whitespace. If objects are null or its amount is 0,
     * shows "(null)" as message. Tag will be {@link AppLogger#APP_TAG APP_TAG}.
     */
    public static void info(@Nullable Object... objects) {
        info(null, objects);
    }

    /**
     * Send a {@link Log#ERROR ERROR} message.
     */
    public static void error(@Nullable String tag, @Nullable Throwable throwable, @Nullable Object... objects) {
        if (!DEBUG_MODE) return;

        String fullMessage = getFullMessage(tag, objects);
        if (throwable != null) {
            fullMessage += " ::: " + throwable.getMessage();
        }

        Log.e(APP_TAG, fullMessage);
    }

    private static String getFullMessage(@Nullable String tag, @Nullable Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        if (tag != null) {
            stringBuilder.append("<");
            stringBuilder.append(tag);
            stringBuilder.append("> :: ");
        }
        if (objects != null) {
            for (int i = 0; i < objects.length; i++) {
                Object o = objects[i];
                if (o != null) {
                    stringBuilder.append(o);
                } else {
                    stringBuilder.append("(null)");
                }
                if (i != objects.length - 1) stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

}
