package com.sandradita.testapptopostindustria.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sandradita.testapptopostindustria.model.Tweet;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sandradita on 6/19/2017.
 */

public class FavouritesDatabaseManager {

    private static final String APP_DATABASE_NAME = "twitter_statuses.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper mDatabaseHelper;

    public FavouritesDatabaseManager(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /**
     * Saves selected tweet to favourites table.
     */
    public void saveTweet(@Nullable Tweet tweet) {
        if (tweet == null) return;
        ContentValues contentValues = tweetToContentValues(tweet);
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        database.insert(Tweet.Entry.TABLE_NAME, null, contentValues);
        database.close();
    }

    /**
     * Removes selected tweet from favourites table.
     */
    public void removeTweet(@Nullable Tweet tweet) {
        if (tweet == null) return;
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        database.delete(Tweet.Entry.TABLE_NAME, Tweet.Entry.FIELD_TWEET_ID + "=?",
                new String[]{tweet.getTweetId()});
        database.close();
    }

    /**
     * @return true if selected tweet contains in DB. Checks by {@link Tweet#tweetId}.
     */
    public boolean containsTweet(@Nullable Tweet tweet) {
        if (tweet == null) return false;
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query(Tweet.Entry.TABLE_NAME, null,
                Tweet.Entry.FIELD_TWEET_ID + "=?",
                new String[]{tweet.getTweetId()}, null, null, null);
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        database.close();
        return contains;
    }

    /**
     * @return list of all favourite tweets.
     */
    @NonNull
    public ArrayList<Tweet> getTweets(int page, int itemsPerPage) {
        ArrayList<Tweet> tweetList = new ArrayList<>();
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        int offset = itemsPerPage * page;
        String query = String.format(Locale.getDefault(),
                "SELECT * FROM %s LIMIT %d OFFSET %d",
                Tweet.Entry.TABLE_NAME, itemsPerPage, offset);
        Cursor cursor = database.rawQuery(query, null, null);
        if (cursor.moveToFirst()) {
            do {
                tweetList.add(tweetFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return tweetList;
    }

    private ContentValues tweetToContentValues(Tweet tweet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tweet.Entry.FIELD_TWEET_ID, tweet.getTweetId());
        contentValues.put(Tweet.Entry.FIELD_AUTHOR_NAME, tweet.getAuthorName());
        contentValues.put(Tweet.Entry.FIELD_CREATED_AT, tweet.getCreatedAt());
        contentValues.put(Tweet.Entry.FIELD_TEXT, tweet.getText());
        contentValues.put(Tweet.Entry.FIELD_AUTHOR_IMAGE_URL, tweet.getAuthorImageUrl());
        return contentValues;
    }

    private Tweet tweetFromCursor(Cursor cursor) {
        Tweet tweet = new Tweet();
        tweet.setTweetId(cursor.getString(cursor.getColumnIndex(Tweet.Entry.FIELD_TWEET_ID)));
        tweet.setAuthorName(cursor.getString(cursor.getColumnIndex(Tweet.Entry.FIELD_AUTHOR_NAME)));
        tweet.setCreatedAt(cursor.getString(cursor.getColumnIndex(Tweet.Entry.FIELD_CREATED_AT)));
        tweet.setText(cursor.getString(cursor.getColumnIndex(Tweet.Entry.FIELD_TEXT)));
        tweet.setAuthorImageUrl(cursor.getString(cursor.getColumnIndex(Tweet.Entry.
                FIELD_AUTHOR_IMAGE_URL)));
        tweet.setFavourite(true);
        return tweet;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, APP_DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        private void createTable(SQLiteDatabase db) {
            db.execSQL(Tweet.Entry.CREATE_TABLE_QUERY);
        }

    }

}
