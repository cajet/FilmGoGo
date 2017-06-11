package com.example.pipin.Filmgogo.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Pipin on 2017/6/9.
 */

public class VoteMovieAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
    private BuyButtonListener mListener;

    public VoteMovieAdapter (LayoutInflater inflater, List<HashMap<String, Object>> data) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.item_movie_vote, viewGroup, false);
        ImageView movie_img = (ImageView) view.findViewById(R.id.movie_img);
        TextView movie_name = (TextView) view.findViewById(R.id.movie_name);
        TextView movie_score = (TextView) view.findViewById(R.id.movie_score);
        TextView movie_type = (TextView) view.findViewById(R.id.movie_type);
        TextView movie_votes = (TextView) view.findViewById(R.id.movie_votes);
        Button bt_buy = (Button) view.findViewById(R.id.bt_buy);

        if(mData.get(i).get("icon") instanceof Bitmap){
            movie_img.setImageBitmap((Bitmap) mData.get(i).get("icon"));
        }

        movie_name.setText(mData.get(i).get("name").toString());
        if (!mData.get(i).get("score").toString().equals("0")) {
            movie_score.setText(mData.get(i).get("score").toString());
        } else {
            movie_score.setText("暂无评分");
        }

        movie_type.setText(mData.get(i).get("type").toString());
        movie_votes.setText(mData.get(i).get("votes").toString());

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.BuyButtonClick(i);
                }
            }
        });

        return view;
    }

    public void setBuyButtonClickListener(BuyButtonListener listener) {
        mListener = listener;
    }
}
