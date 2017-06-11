package com.example.pipin.Filmgogo.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.activity.movieDescription;
import com.example.pipin.Filmgogo.adapter.BuyButtonListener;
import com.example.pipin.Filmgogo.adapter.MovieAdapter;
import com.example.pipin.Filmgogo.util.RequestData;

import static com.example.pipin.Filmgogo.activity.MainActivity.accountState;

/**
 * Created by Pipin on 2017/6/8.
 */

public class OldMovieListFragment extends BaseFragment implements BuyButtonListener{
    private ListView OldMovieList;
    private TextView tv_empty;
    private RequestData app;
    //    private TextView appbar_title;
    private MovieAdapter mAdapter;
    private android.os.Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_old_movie_list,null);
        OldMovieList = (ListView) view.findViewById(R.id.old_movie_list);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
//        appbar_title = (TextView) view.findViewById(R.id.appbar_title);


        app = (RequestData) getActivity().getApplication();
        app.setListItemOldMovie();
        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);
        mAdapter = new MovieAdapter(getActivity().getLayoutInflater(), app.getListItemOldMovie());
        mAdapter.setBuyButtonClickListener((BuyButtonListener) this);
//        appbar_title.setText("正在上映");


        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {
        OldMovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemAndButtonClick(position);
            }
        });
    }

    private void onItemAndButtonClick(int position) {
        Intent intent = new Intent(getActivity(), movieDescription.class);
        SharedPreferences.Editor editor = accountState.edit();
        editor.putInt("movieId", Integer.parseInt(app.getListItemOldMovie().get(position).get("id").toString()));
        editor.putInt("nowOrOld", 0);
        editor.commit();
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getListItemMovie().size() == 0) {
                OldMovieList.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getListItemMovie().size(); )
                if (app.getListItemMovie().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getListItemMovie().size()) {
                OldMovieList.setVisibility(View.INVISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
            }
            else {
                OldMovieList.setAdapter(mAdapter);
                OldMovieList.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void BuyButtonClick(final int position) {
        onItemAndButtonClick(position);
    }
}
