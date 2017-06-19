package com.sandradita.testapptopostindustria.ui.fragments.abs;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by sandradita on 6/16/2017.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mView);
        onViewInflated(mView, savedInstanceState);
        return mView;
    }

    /**
     * Current fragment layout id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * Is called after view is inflated and prepared
     */
    protected abstract void onViewInflated(View view, Bundle savedInstanceState);

}
