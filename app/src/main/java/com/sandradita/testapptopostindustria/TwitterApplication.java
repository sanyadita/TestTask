package com.sandradita.testapptopostindustria;

import android.app.Application;

import com.sandradita.testapptopostindustria.managers.TwitterDataManager;

/**
 * Created by sandradita on 6/17/2017.
 */

public class TwitterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterDataManager.getInstance(this);
    }
}
