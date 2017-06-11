package com.example.pipin.Filmgogo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.adapter.MovieFragmentPagerAdapter;
import com.example.pipin.Filmgogo.util.FixedSpeedScroller;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.lang.reflect.Field;


/**
 * Created by Pipin on 2017/6/6.
 */
public class MovieFragment extends BaseFragment {
    private ViewPager mViewPager;
    private FixedSpeedScroller mScroller;
    private ImageView ic_search;
    private MovieFragmentPagerAdapter myFragmentPagerAdapter;
    private TextView tv_now_head_tab, tv_old_head_tab;
    private View view;

    // 未被选中的选项卡字体颜色
    private int COLOR_NORMAL = 0xfff2d6d5;

    public static MovieFragment newInstance(String from){
        MovieFragment fragment = new MovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            myFragmentPagerAdapter.notifyDataSetChanged();
            return view;
        }



        view = inflater.inflate(R.layout.fragment_movie_layout,null);
        ic_search = (ImageView) view.findViewById(R.id.ic_search);
        //使用适配器将ViewPager与Fragment绑定在一起
        tv_now_head_tab = (TextView) view.findViewById(R.id.tv_now_head_tab);
        tv_old_head_tab = (TextView) view.findViewById(R.id.tv_old_head_tab);
        mViewPager= (ViewPager) view.findViewById(R.id.viewPager);

        myFragmentPagerAdapter = new MovieFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(mViewPager.getContext(),new AccelerateInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UnderlinePageIndicator mIndicator = (UnderlinePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setFades(false);
        mIndicator.setSelectedColor(0xffffffff);
        mIndicator.setShape(1);
        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int pos) {
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        if (arg0 == 1) {
                            tv_now_head_tab.setTextColor(COLOR_NORMAL);
                            tv_old_head_tab.setTextColor(COLOR_NORMAL);
                        } else if (arg0 == 0) {
                            setIndicatorViewSelected(mViewPager.getCurrentItem());
                        }
                    }
                });
        // 初始化，第0项被选中
        setIndicatorViewSelected(0);
        return view;
    }


    // 在这里设置被选中时候选项卡变化的效果
    private void setIndicatorViewSelected(int pos) {
            if (pos == 0) {
                // Android Holo 样式的蓝色
                tv_now_head_tab.setTextColor(0xffdc3c38);
                tv_old_head_tab.setTextColor(COLOR_NORMAL);
            } else {
                tv_now_head_tab.setTextColor(COLOR_NORMAL);
                tv_old_head_tab.setTextColor(0xffdc3c38);
            }
    }

    protected int getItemsCount() {
        return 2;
    }

    private void set(int pos) {
        mViewPager.setCurrentItem(pos, true);
        mScroller.setmDuration(180);
        setIndicatorViewSelected(pos);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {
        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Main2Activity.class);
//                startActivity(intent);
            }
        });

        tv_now_head_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set(0);
            }
        });

        tv_old_head_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set(1);
            }
        });
    }
}
