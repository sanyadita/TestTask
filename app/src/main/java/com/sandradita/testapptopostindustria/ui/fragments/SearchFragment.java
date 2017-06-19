package com.sandradita.testapptopostindustria.ui.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;

import com.sandradita.testapptopostindustria.R;
import com.sandradita.testapptopostindustria.managers.TwitterDataManager;
import com.sandradita.testapptopostindustria.ui.fragments.abs.BaseTweetRequestListFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;

public class SearchFragment extends BaseTweetRequestListFragment implements SearchView.OnQueryTextListener {

    public static final String ARG_SEARCH_QUERY = "SearchFragment.searchQuery";

    @BindView(R.id.se_query)
    SearchView seQuery;
    @BindView(R.id.rv_tweets)
    RecyclerView rvSearchResults;
    @BindView(R.id.pb_reading_progress)
    ProgressBar pbReadingProgress;
    @BindView(R.id.srl_tweets)
    SwipeRefreshLayout srlTweets;

    /**
     * Returns new instance of {@link SearchFragment} and sets the query that should be found in
     * this fragment
     */
    public static SearchFragment newInstance(String searchQuery) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_QUERY, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
    }

    @Override
    protected void onViewInflated(View view, Bundle savedInstanceState) {
        super.onViewInflated(view, savedInstanceState);
        seQuery.setOnQueryTextListener(this);
        srlTweets.setOnRefreshListener(this);

        configureRecyclerView(rvSearchResults);

        if (getArguments() != null) {
            seQuery.setQuery(getArguments().getString(ARG_SEARCH_QUERY), true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void onReadStart() {
        pbReadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onReadFinished() {
        setIsLoading(false);
        pbReadingProgress.setVisibility(View.GONE);
        srlTweets.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return true;
    }

    private void search(String query) {
        if (query == null || query.isEmpty()) {
            mAdapter.clear();
            return;
        }
        onReadStart();
        String result = null;
        try {
            result = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TwitterDataManager.getInstance().search(result, mSearchCallback);
    }
}
