package com.example.pipin.Filmgogo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Pipin on 2017/6/7.
 */
public class CinemaAdapter extends BaseAdapter{
    private List<HashMap<String, Object>> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

    public CinemaAdapter (LayoutInflater inflater, List<HashMap<String, Object>> data) {
        mInflater = inflater;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.item_cinema, viewGroup, false);
        TextView cinema_name = (TextView) view.findViewById(R.id.cinema_name);
        TextView cinema_location = (TextView) view.findViewById(R.id.cinema_location);

        cinema_name.setText(mData.get(i).get("name").toString());
        cinema_location.setText(mData.get(i).get("location").toString());
        return view;
    }

}
