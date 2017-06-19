package com.sandradita.testapptopostindustria.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandradita on 6/16/2017.
 */

public class TokenResponse {

    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("access_token")
    private String accessToken;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return tokenType + " " + accessToken;
    }
}
