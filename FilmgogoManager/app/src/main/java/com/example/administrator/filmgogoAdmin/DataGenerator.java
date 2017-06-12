package com.example.administrator.filmgogoAdmin;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Pipin on 2017/6/6.
 */
public class DataGenerator {
//    public static final int []mTabRes = new int[]{R.drawable.tab_movie_selector,R.drawable.tab_cinema_selector,R.drawable.tab_discover_selector,R.drawable.tab_profile_selector};
//    public static final int []mTabResPressed = new int[]{R.mipmap.ic_tab_movie_selected,R.mipmap.ic_cinema_selected,R.mipmap.ic_discover_selected,R.mipmap.ic_my_selector};
    public static final String []mTabTitle = new String[]{"老电影","上映中"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[2];
        fragments[0] = VoteFragment.newInstance(from);
        fragments[1] = ShowFragment.newInstance(from);
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context,int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
//        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
//        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
