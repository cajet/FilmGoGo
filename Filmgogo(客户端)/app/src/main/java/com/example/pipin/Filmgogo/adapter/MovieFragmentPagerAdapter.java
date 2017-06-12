package com.example.pipin.Filmgogo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pipin.Filmgogo.fragment.NowMovieListFragment;
import com.example.pipin.Filmgogo.fragment.OldMovieListFragment;

/**
 * Created by Pipin on 2017/6/8.
 */

public class MovieFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"正在上映", "老电影"};

    public MovieFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new OldMovieListFragment();
        }
        return new NowMovieListFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
