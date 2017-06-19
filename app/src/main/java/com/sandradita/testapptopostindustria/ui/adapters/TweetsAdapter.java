package com.sandradita.testapptopostindustria.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sandradita.testapptopostindustria.helpers.TextHelper;
import com.sandradita.testapptopostindustria.model.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandradita on 6/16/2017.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private ArrayList<Tweet> mTweetList;
    private TweetViewHolder.OnChangeFavouriteStatus mOnChangeFavouriteStatus;
    private TextHelper.OnClickSpanListener mOnClickSpanListener;

    public TweetsAdapter(ArrayList<Tweet> tweetList) {
        mTweetList = tweetList;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = TweetViewHolder.inflateView(parent);
        TweetViewHolder viewHolder = new TweetViewHolder(view);
        viewHolder.setOnChangeFavouriteStatus(mLocalOnChangeFavouriteStatus);
        viewHolder.setOnClickSpanListener(mLocalOnClickSpanListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = mTweetList.get(position);
        holder.setItem(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    /**
     * Adds tweet to the end of the RecyclerView
     */
    public void addTweet(Tweet tweet) {
        mTweetList.add(tweet);
        notifyDataSetChanged();
    }

    /**
     * Removes tweet from RecyclerView
     */
    public void removeTweet(Tweet tweet) {
        mTweetList.remove(tweet);
        notifyDataSetChanged();
    }

    /**
     * Adds tweet list to the end of RecyclerView
     */
    public void appendTweets(List<Tweet> tweets) {
        mTweetList.addAll(tweets);
        notifyDataSetChanged();
    }

    /**
     * Removes all tweets from RecyclerView and adds tweet list
     */
    public void replaceTweets(List<Tweet> tweets) {
        mTweetList.clear();
        appendTweets(tweets);
    }

    /**
     * Removes all tweets from RecyclerView
     */
    public void clear() {
        mTweetList.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Tweet> getTweetList() {
        return mTweetList;
    }

    public void setOnChangeFavouriteStatus(TweetViewHolder.OnChangeFavouriteStatus onChangeFavouriteStatus) {
        this.mOnChangeFavouriteStatus = onChangeFavouriteStatus;
    }

    public void setOnClickSpanListener(TextHelper.OnClickSpanListener onClickSpanListener) {
        this.mOnClickSpanListener = onClickSpanListener;
    }

    /**
     * Listener is called when user makes tweet favourite and unfavourite
     */
    private final TweetViewHolder.OnChangeFavouriteStatus mLocalOnChangeFavouriteStatus = new TweetViewHolder.OnChangeFavouriteStatus() {
        @Override
        public void onChangeStatus(Tweet tweet, boolean isFavourite) {
            if (mOnChangeFavouriteStatus != null) {
                mOnChangeFavouriteStatus.onChangeStatus(tweet, isFavourite);
            }
        }
    };

    /**
     * Listener is called when user clicks on hashtag or mention.
     */
    private final TextHelper.OnClickSpanListener mLocalOnClickSpanListener = new TextHelper.OnClickSpanListener() {
        @Override
        public void onClickSpan(String word) {
            if (mOnClickSpanListener != null) mOnClickSpanListener.onClickSpan(word);
        }
    };
}
