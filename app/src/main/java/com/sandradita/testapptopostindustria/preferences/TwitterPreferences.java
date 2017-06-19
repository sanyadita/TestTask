package com.sandradita.testapptopostindustria.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sandradita on 6/17/2017.
 */

public class TwitterPreferences {

    private static final String PREFERENCES_NAME = "app_token";
    private static final String PARAM_TOKEN = "twitter_app_token";

    private SharedPreferences mPreferences;

    public TwitterPreferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves application token, that was taken from Twitter server, to preferences
     */
    public void setAppToken(String token) {
        mPreferences.edit().putString(PARAM_TOKEN, token).apply();
    }

    /**
     * @return token, that applications received from Twitter
     */
    public String getAppToken() {
        return mPreferences.getString(PARAM_TOKEN, null);
    }

}
