package com.example.pipin.Filmgogo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.util.RequestData;

/**
 * Created by ypy on 2017/6/11.
 */
public class LikeMovie extends Activity {
    ImageView back;

    ListView movieList;

    public SharedPreferences accountState;

    private SimpleAdapter adapterMovie;

    private RequestData app;
    private android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_movie);

        app = (RequestData)getApplication();
        app.setUserLikeMovie();

        init();
        setListener();


        adapterMovie = new SimpleAdapter(this, app.getUserLikeMovie(), R.layout.item_like_movie,
                new String[]{"icon", "name", "sore", "votes", "type"}, new int[]{R.id.movie_img
                , R.id.movie_name, R.id.movie_score, R.id.movie_votes, R.id.movie_type});
        adapterMovie.setViewBinder(new SimpleAdapter.ViewBinder() {

            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                //判断是否为我们要处理的对象
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView iv = (ImageView) view;

                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }else
                    return false;
            }
        });
        movieList.setAdapter(adapterMovie);


        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);


    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getUserLikeMovie().size() == 0) {
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getUserLikeMovie().size(); )
                if (app.getUserLikeMovie().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getUserLikeMovie().size()) {

                handler.postDelayed(this, 150);
            }
            else {
                adapterMovie.notifyDataSetChanged();
            }
        }
    };


    void init() {
        back = (ImageView)findViewById(R.id.id_my_reservation_back);
        movieList = (ListView)findViewById(R.id.id_reservation_list);


        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

    }

    void setListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
