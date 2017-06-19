package com.sandradita.testapptopostindustria.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.sandradita.testapptopostindustria.R;
import com.sandradita.testapptopostindustria.managers.TwitterDataManager;
import com.sandradita.testapptopostindustria.model.Tweet;
import com.sandradita.testapptopostindustria.ui.adapters.TweetViewHolder;
import com.sandradita.testapptopostindustria.ui.fragments.abs.BaseTweetListFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class FavouritesFragment extends BaseTweetListFragment {

    @BindView(R.id.rv_tweets)
    RecyclerView rvTweets;
    @BindView(R.id.pb_reading_progress)
    ProgressBar pbReadingProgress;
    @BindView(R.id.srl_tweets)
    SwipeRefreshLayout srlTweets;

    private Handler mBackgroundHandler;
    private HandlerThread mHandlerThread;
    private int mNextPage;
    private int mLastResultCount = -1;

    public FavouritesFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favourites;
    }

    @Override
    protected void onViewInflated(View view, Bundle savedInstanceState) {
        super.onViewInflated(view, savedInstanceState);
        configureRecyclerView(rvTweets);
        mAdapter.setOnChangeFavouriteStatus(mOnChangeFavouriteStatus);
        srlTweets.setOnRefreshListener(this);

        mHandlerThread = new HandlerThread("");
        mHandlerThread.start();
        mBackgroundHandler = new Handler(mHandlerThread.getLooper());

        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quitSafely();
    }

    @Override
    protected void onReadStart() {
        pbReadingProgress.setVisibility(View.VISIBLE);
        setIsLoading(true);
    }

    @Override
    protected void onReadFinished() {
        setIsLoading(false);
        pbReadingProgress.setVisibility(View.GONE);
        srlTweets.setRefreshing(false);
    }

    @Override
    protected void onLoadNextTweets() {
        loadData();
    }

    @Override
    public void onRefresh() {
        if (!isLoading()) {
            loadData();
        } else {
            srlTweets.setRefreshing(false);
        }
    }

    private void loadData() {
        if (!isLoading() && mLastResultCount != 0) {
            onReadStart();
            mBackgroundHandler.postDelayed(mReadThread, 100);
        }
    }

    private void showTweets(ArrayList<Tweet> tweets) {
        mLastResultCount = tweets.size();
        if (mLastResultCount > 0) {
            if (mNextPage == 0) {
                mAdapter.replaceTweets(tweets);
            } else {
                mAdapter.appendTweets(tweets);
            }
            mNextPage++;
        }
        onReadFinished();
    }

    /**
     * Listener that changes list when some tweet was deleted from favourites.
     */
    private final TweetViewHolder.OnChangeFavouriteStatus mOnChangeFavouriteStatus =
            new TweetViewHolder.OnChangeFavouriteStatus() {
                @Override
                public void onChangeStatus(Tweet tweet, boolean isFavourite) {
                    if (!isFavourite) mAdapter.removeTweet(tweet);
                }
            };

    private final Runnable mReadThread = new Runnable() {

        @Override
        public void run() {
            final ArrayList<Tweet> tweets = TwitterDataManager.getInstance().getFavourites(mNextPage);
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTweets(tweets);
                    }
                });
            }
        }
    };

}
