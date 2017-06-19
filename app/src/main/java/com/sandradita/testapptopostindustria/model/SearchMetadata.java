package com.sandradita.testapptopostindustria.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandradita on 6/18/2017.
 */

public class SearchMetadata {

    @SerializedName("next_results")
    private String nextResults;
    @SerializedName("refresh_url")
    private String refreshUrl;
    private String query;

    public String getNextResults() {
        return nextResults;
    }

    public void setNextResults(String nextResults) {
        this.nextResults = nextResults;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
