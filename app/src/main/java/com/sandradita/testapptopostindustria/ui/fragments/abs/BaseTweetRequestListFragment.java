package com.sandradita.testapptopostindustria.ui.fragments.abs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.sandradita.testapptopostindustria.R;
import com.sandradita.testapptopostindustria.managers.TwitterDataManager;
import com.sandradita.testapptopostindustria.model.SearchMetadata;
import com.sandradita.testapptopostindustria.model.SearchResponse;
import com.sandradita.testapptopostindustria.model.TokenResponse;
import com.sandradita.testapptopostindustria.model.Tweet;
import com.sandradita.testapptopostindustria.utils.AppLogger;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandradita on 6/18/2017.
 */

public abstract class BaseTweetRequestListFragment extends BaseTweetListFragment {

    protected final SearchCallback mSearchCallback = new SearchCallback(true);
    protected final SearchCallback mSearchNextCallback = new SearchCallback(false);

    private SearchMetadata mLastSearchMetadata;
    private CheckTweetsAsyncTask mCheckTweetsAsyncTask;

    @Override
    protected void onViewInflated(View view, Bundle savedInstanceState) {
        super.onViewInflated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        TwitterDataManager.getInstance().setTokenCallback(mTokenCallback);
    }

    @Override
    public void onPause() {
        stopTweetsCheck();
        TwitterDataManager.getInstance().setTokenCallback(null);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        if (mLastSearchMetadata != null && mLastSearchMetadata.getQuery() != null) {
            setIsLoading(true);
            TwitterDataManager.getInstance().search(mLastSearchMetadata.getQuery(), mSearchCallback);
        } else {
            onReadFinished();
        }
    }

    @Override
    protected void onLoadNextTweets() {
        if (mLastSearchMetadata != null && mLastSearchMetadata.getNextResults() != null) {
            onReadStart();
            TwitterDataManager.getInstance().searchByUrl(mLastSearchMetadata.getNextResults(), mSearchNextCallback);
        } else {
            onReadFinished();
        }
    }

    private void showNoTokenError() {
        onReadFinished();
        Snackbar.make(getView(), R.string.message_error_get_token, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Gets array of tweets from {@link Response} and response metadata
     * @return null if app can't take tweets
     */
    @Nullable
    private List<Tweet> getTweetsFromResponse(Response<SearchResponse> response) {
        SearchResponse searchResponse = response.body();
        if (searchResponse != null) {
            Tweet[] tweets = searchResponse.getStatuses();
            mLastSearchMetadata = searchResponse.getMetadata();
            return Arrays.asList(tweets);
        }
        onReadFailed();
        return null;
    }

    private void onReadFailed() {
        Snackbar.make(getView(), R.string.message_error_get_tweets, Snackbar.LENGTH_SHORT).show();
        onReadFinished();
    }

    private void stopTweetsCheck() {
        if (mCheckTweetsAsyncTask != null) {
            mCheckTweetsAsyncTask.cancel(false);
            mCheckTweetsAsyncTask = null;
        }
    }

    protected final Callback<TokenResponse> mTokenCallback = new Callback<TokenResponse>() {
        @Override
        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
            if (!response.isSuccessful()) {
                showNoTokenError();
                AppLogger.error("GetToken", null, response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<TokenResponse> call, Throwable t) {
            showNoTokenError();
        }
    };

    /**
     * Shows the result of searching tweets.
     */
    private class SearchCallback implements Callback<SearchResponse> {

        private boolean mReplaceResults;

        public SearchCallback(boolean replaceResults) {
            mReplaceResults = replaceResults;
        }

        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            onReadFinished();
            List<Tweet> tweetList = getTweetsFromResponse(response);
            if (tweetList != null) {
                if (mReplaceResults) {
                    mAdapter.replaceTweets(tweetList);
                } else {
                    mAdapter.appendTweets(tweetList);
                }
                stopTweetsCheck();
                mCheckTweetsAsyncTask = new CheckTweetsAsyncTask(tweetList);
                mCheckTweetsAsyncTask.execute();
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            onReadFailed();
        }

    }

    /**
     * Checks for tweets that are saved as favourite
     */
    private class CheckTweetsAsyncTask extends AsyncTask<Void,Void,Void> {

        private List<Tweet> mTweetList;

        public CheckTweetsAsyncTask(List<Tweet> tweetList) {
            mTweetList = tweetList;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (Tweet tweet : mTweetList) {
                tweet.setFavourite(TwitterDataManager.getInstance().isTweetFavourite(tweet));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled()) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
