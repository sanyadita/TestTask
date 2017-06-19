package com.sandradita.testapptopostindustria.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by sandradita on 6/16/2017.
 */

public class Tweet implements Parcelable {

    private String authorImageUrl;
    private String authorName;
    private String createdAt;
    private String text;
    private String tweetId;
    private boolean isFavourite;

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        authorImageUrl = in.readString();
        authorName = in.readString();
        createdAt = in.readString();
        text = in.readString();
        tweetId = in.readString();
        isFavourite = in.readInt() != 0;
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getAuthorImageUrl());
        dest.writeString(getAuthorName());
        dest.writeString(getCreatedAt());
        dest.writeString(getText());
        dest.writeString(getTweetId());
        dest.writeInt(isFavourite ? 1 : 0);
    }

    public static class Deserializer implements JsonDeserializer<Tweet> {

        @Override
        public Tweet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                Tweet tweet = new Tweet();
                JsonObject jsonObject = json.getAsJsonObject();
                tweet.tweetId = jsonObject.get("id_str").getAsString();
                tweet.text = jsonObject.get("text").getAsString();
                tweet.createdAt = jsonObject.get("created_at").getAsString();
                JsonObject userObj = jsonObject.getAsJsonObject("user");
                if (userObj != null) {
                    tweet.authorName = userObj.get("name").getAsString();
                    tweet.authorImageUrl = userObj.get("profile_image_url").getAsString();
                }
                return tweet;
            }
            return null;
        }
    }

    /**
     * Constants for saving {@link Tweet} objects to database.
     */
    public interface Entry {

        String TABLE_NAME = "Tweets";
        String FIELD_TWEET_ID = "tweet_id";
        String FIELD_AUTHOR_NAME = "author_name";
        String FIELD_CREATED_AT = "created_at";
        String FIELD_AUTHOR_IMAGE_URL = "author_image_url";
        String FIELD_TEXT = "text";

        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                FIELD_TWEET_ID + " TEXT UNIQUE not null, " +
                FIELD_AUTHOR_NAME + " TEXT not null, " +
                FIELD_CREATED_AT + " TEXT not null, " +
                FIELD_TEXT + " TEXT not null, " +
                FIELD_AUTHOR_IMAGE_URL + " TEXT not null)";

    }

}
