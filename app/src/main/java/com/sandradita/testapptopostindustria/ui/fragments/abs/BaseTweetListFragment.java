package com.sandradita.testapptopostindustria.ui.fragments.abs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sandradita.testapptopostindustria.helpers.TextHelper;
import com.sandradita.testapptopostindustria.managers.TwitterDataManager;
import com.sandradita.testapptopostindustria.model.Tweet;
import com.sandradita.testapptopostindustria.ui.activities.MainActivity;
import com.sandradita.testapptopostindustria.ui.adapters.TweetsAdapter;
import com.sandradita.testapptopostindustria.ui.fragments.SearchFragment;

import java.util.ArrayList;

/**
 * Fragment that shows tweets.
 * Created by sandradita on 6/17/2017.
 */
public abstract class BaseTweetListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected TweetsAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;
    private boolean mLoading;

    @Override
    protected void onViewInflated(View view, Bundle savedInstanceState) {
        mAdapter = new TweetsAdapter(new ArrayList<Tweet>());
        mAdapter.setOnClickSpanListener(mOnClickSpanListener);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    /**
     * Is called before start searching
     */
    protected abstract void onReadStart();

    /**
     * Is called when searching was finished
     */
    protected abstract void onReadFinished();

    /**
     * Is called when user scrolls to the end of the list and next part of items should be loaded
     */
    protected abstract void onLoadNextTweets();

    /**
     * Configures recyclerView to show list of tweets
     * @param recyclerView
     */
    protected void configureRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(mScrollListener);
    }

    protected void setIsLoading(boolean loading) {
        mLoading = loading;
    }

    /**
     * @return true if tweets are being read
     */
    protected boolean isLoading() {
        return mLoading;
    }

    /**
     * Listener opens {@link SearchFragment} to search tweets by selected keyword.
     */
    private final TextHelper.OnClickSpanListener mOnClickSpanListener = new TextHelper.OnClickSpanListener() {
        @Override
        public void onClickSpan(String word) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra(SearchFragment.ARG_SEARCH_QUERY, word);
            startActivity(intent);
        }
    };

    /**
     * Is called when user scroll list down
     */
    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + TwitterDataManager.TWEETS_PER_PAGE)) {
                    onLoadNextTweets();
                    mLoading = true;
                }
            }
        }
    };


}
