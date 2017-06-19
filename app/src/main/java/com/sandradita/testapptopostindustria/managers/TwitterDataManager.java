package com.sandradita.testapptopostindustria.managers;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import com.sandradita.testapptopostindustria.database.FavouritesDatabaseManager;
import com.sandradita.testapptopostindustria.model.SearchResponse;
import com.sandradita.testapptopostindustria.model.TokenResponse;
import com.sandradita.testapptopostindustria.model.Tweet;
import com.sandradita.testapptopostindustria.preferences.TwitterPreferences;
import com.sandradita.testapptopostindustria.rest.TwitterApiClient;
import com.sandradita.testapptopostindustria.utils.AppLogger;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandradita on 6/17/2017.
 */

/**
 * Class that manage all Twitter data: local and server.
 */
public class TwitterDataManager {

    /**
     * Items amount that loads on one cycle
     */
    public static final int TWEETS_PER_PAGE = 10;

    private final String TAG = getClass().getSimpleName();

    private static TwitterDataManager instance;

    private TwitterPreferences mPreferences;
    private FavouritesDatabaseManager mDatabaseManager;
    private String mToken;

    /**
     * Callback for getting token from server.
     */
    private Callback<TokenResponse> mTokenCallback;

    private TwitterDataManager(Context context) {
        mPreferences = new TwitterPreferences(context);
        mDatabaseManager = new FavouritesDatabaseManager(context);
    }

    public static TwitterDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new TwitterDataManager(context);
        }
        return instance;
    }

    public static TwitterDataManager getInstance() {
        return instance;
    }

    /**
     * Searches tweets by URL part from next_results or refresh_url
     */
    public void searchByUrl(final String url, final Callback<SearchResponse> callback) {
        TwitterApiClient.searchByUrl(mToken, url, callback);
    }

    /**
     * Searches tweets by query
     */
    public void search(final String query, final Callback<SearchResponse> callback) {
        if (getToken() == null) {
            requestToken(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.isSuccessful()) {
                        TwitterApiClient.search(mToken, query, TWEETS_PER_PAGE, callback);
                    }
                    if (mTokenCallback != null) mTokenCallback.onResponse(call, response);
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    if (mTokenCallback != null) mTokenCallback.onFailure(call, t);
                }
            });
        } else {
            TwitterApiClient.search(mToken, query, TWEETS_PER_PAGE, callback);
        }
    }

    /**
     * Searches tweets by location
     */
    public void search(final Location location, final Callback<SearchResponse> callback) {
        if (getToken() == null) {
            requestToken(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.isSuccessful()) {
                        TwitterApiClient.search(mToken, location, TWEETS_PER_PAGE, callback);
                    }
                    if (mTokenCallback != null) mTokenCallback.onResponse(call, response);
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    if (mTokenCallback != null) mTokenCallback.onFailure(call, t);
                }
            });
        } else {
            TwitterApiClient.search(mToken, location, TWEETS_PER_PAGE, callback);
        }
    }

    public String getToken() {
        if (mToken == null) {
            mToken = mPreferences.getAppToken();
        }
        return mToken;
    }

    public void requestToken(@Nullable final Callback<TokenResponse> callback) {
        TwitterApiClient.getToken(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse = response.body();
                if (tokenResponse != null) {
                    mToken = tokenResponse.toString();
                    mPreferences.setAppToken(mToken);
                }
                if (callback != null) callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                AppLogger.error(TAG, t);
                if (callback != null) callback.onFailure(call, t);
            }
        });
    }

    /**
     * Reads favourites list from database
     */
    public ArrayList<Tweet> getFavourites(int page) {
        return mDatabaseManager.getTweets(page, TWEETS_PER_PAGE);
    }

    /**
     * Checks if tweet is saved to favourites
     */
    public boolean isTweetFavourite(Tweet tweet) {
        return mDatabaseManager.containsTweet(tweet);
    }

    /**
     * Saves tweet to database if it is favourite or remove it, if is not.
     */
    public void setTweetFavourite(Tweet tweet, boolean isFavourite) {
        if (isFavourite) {
            mDatabaseManager.saveTweet(tweet);
        } else {
            mDatabaseManager.removeTweet(tweet);
        }
    }

    public void setTokenCallback(Callback<TokenResponse> callback) {
        this.mTokenCallback = callback;
    }

}
