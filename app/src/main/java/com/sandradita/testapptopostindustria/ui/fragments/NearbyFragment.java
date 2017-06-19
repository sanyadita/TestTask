package com.sandradita.testapptopostindustria.ui.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sandradita.testapptopostindustria.R;
import com.sandradita.testapptopostindustria.helpers.PermissionHelper;
import com.sandradita.testapptopostindustria.managers.TwitterDataManager;
import com.sandradita.testapptopostindustria.ui.fragments.abs.BaseTweetRequestListFragment;

import butterknife.BindView;

public class NearbyFragment extends BaseTweetRequestListFragment {

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_PERMISSIONS = 1;

    @BindView(R.id.rv_tweets)
    RecyclerView rvTweets;
    @BindView(R.id.pb_reading_progress)
    ProgressBar pbReadingProgress;
    @BindView(R.id.srl_tweets)
    SwipeRefreshLayout srlTweets;

    private FusedLocationProviderClient mLocationProviderClient;

    public NearbyFragment() {
    }

    @Override
    protected void onViewInflated(View view, Bundle savedInstanceState) {
        super.onViewInflated(view, savedInstanceState);
        srlTweets.setOnRefreshListener(this);
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        configureRecyclerView(rvTweets);

        onReadStart();
        if (PermissionHelper.isPermissionAllowed(getContext(), REQUIRED_PERMISSIONS)) {
            mLocationProviderClient.getLastLocation().addOnSuccessListener(locationListener);
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nearby;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    onLoadTweetsError();
                    return;
                }
            }
            mLocationProviderClient.getLastLocation().addOnSuccessListener(locationListener);
        }
    }

    private void onLoadTweetsError() {
        Snackbar.make(getView(), R.string.message_error_get_tweets, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Takes user location
     */
    private final OnSuccessListener<Location> locationListener = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location == null) {
                onReadFinished();
                Snackbar.make(getView(), R.string.message_error_get_location, Snackbar.LENGTH_SHORT).show();
            } else {
                TwitterDataManager.getInstance().search(location, mSearchCallback);
            }
        }
    };

}
