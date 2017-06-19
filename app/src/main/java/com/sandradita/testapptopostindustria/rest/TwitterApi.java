package com.sandradita.testapptopostindustria.rest;

import com.sandradita.testapptopostindustria.model.SearchResponse;
import com.sandradita.testapptopostindustria.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by sandradita on 6/16/2017.
 */

public interface TwitterApi {

    String URL_SEARCH = "1.1/search/tweets.json";

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<TokenResponse> getToken(@Header("Authorization") String authorization, @Field("grant_type") String grantType);

    @GET(URL_SEARCH)
    Call<SearchResponse> searchTweets(@Header("Authorization") String authorization, @Query(value = "q", encoded = true) String query, @Query("count") int perPage);

    @GET
    Call<SearchResponse> twitterUrl(@Header("Authorization") String authorization, @Url String twitterUrl);

}
