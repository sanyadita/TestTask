package com.sandradita.testapptopostindustria.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandradita on 6/16/2017.
 */

public class SearchResponse {

    private Tweet[] statuses;
    @SerializedName("search_metadata")
    private SearchMetadata metadata;

    public Tweet[] getStatuses() {
        return statuses;
    }

    public void setStatuses(Tweet[] statuses) {
        this.statuses = statuses;
    }

    public SearchMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SearchMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "" + statuses.length;
    }
}
