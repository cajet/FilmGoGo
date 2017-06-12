package com.example.pipin.Filmgogo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.pipin.Filmgogo.activity.MainActivity;

/**
 * Created by Pipin on 2017/6/7.
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
        setListener();

    }


    protected abstract void init();

    protected abstract void setListener();
}