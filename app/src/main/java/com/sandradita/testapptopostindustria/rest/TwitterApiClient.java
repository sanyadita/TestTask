package com.sandradita.testapptopostindustria.rest;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sandradita.testapptopostindustria.model.SearchResponse;
import com.sandradita.testapptopostindustria.model.TokenResponse;
import com.sandradita.testapptopostindustria.model.Tweet;

import okhttp3.Credentials;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sandradita on 6/16/2017.
 */

public class TwitterApiClient {

    public static final String BASE_URL = "https://api.twitter.com/";

    private static final String API_KEY = "iInh42BF5LLLlCnJjYMT1qeBB";
    private static final String API_SECRET = "49H6o36bSE69oNBC9YnapXwQR666PnYHOLK8kCWWA5t3k3imzZ";

    private static final String NEARBY_RADIUS = "10km";

    private static TwitterApi sTwitterApi = null;

    private static Retrofit getClient() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Tweet.class, new Tweet.Deserializer()).create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static TwitterApi getApi() {
        if (sTwitterApi == null) {
            sTwitterApi = getClient().create(TwitterApi.class);
        }
        return sTwitterApi;
    }

    /**
     * Sends request to Twitter server to get application token.
     */
    public static void getToken(Callback<TokenResponse> callback) {
        String authorization = Credentials.basic(API_KEY, API_SECRET);
        getApi().getToken(authorization, "client_credentials").enqueue(callback);
    }

    public static void searchByUrl(String auth, String url, Callback<SearchResponse> callback) {
        String searchUrl = TwitterApi.URL_SEARCH + url;
        getApi().twitterUrl(auth, searchUrl).enqueue(callback);
    }

    /**
     * Looks for tweets by selected query.
     *
     * @param auth application token.
     */
    public static void search(String auth, String query, int itemsPerPage, Callback<SearchResponse> callback) {
        getApi().searchTweets(auth, query, itemsPerPage).enqueue(callback);
    }

    /**
     * Looks for tweets by selected location in radius that is equal to {@link #NEARBY_RADIUS}.
     *
     * @param auth     application token
     * @param location current user location
     */
    public static void search(String auth, Location location, int itemsPerPage, Callback<SearchResponse> callback) {
        StringBuilder builder = new StringBuilder();
        if (location != null) {
            builder.append("geocode:")
                    .append(location.getLatitude()).append(",")
                    .append(location.getLongitude()).append(",")
                    .append(NEARBY_RADIUS);
        }
        search(auth, builder.toString(), itemsPerPage, callback);
    }

}
