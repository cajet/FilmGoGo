package com.example.pipin.Filmgogo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pipin.Filmgogo.fragment.ShowTimeListFragment0;
import com.example.pipin.Filmgogo.fragment.ShowTimeListFragment1;
import com.example.pipin.Filmgogo.fragment.ShowTimeListFragment2;

/**
 * Created by Pipin on 2017/6/11.
 */

public class ShowTimeFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"1", "2", "3"};

    public ShowTimeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ShowTimeListFragment0();
        } else if (position == 1) {
            return new ShowTimeListFragment1();
        }
        return new ShowTimeListFragment2();
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
