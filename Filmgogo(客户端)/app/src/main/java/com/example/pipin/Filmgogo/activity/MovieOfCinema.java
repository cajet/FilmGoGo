package com.example.pipin.Filmgogo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.adapter.BuyButtonListener;
import com.example.pipin.Filmgogo.adapter.MovieAdapter;
import com.example.pipin.Filmgogo.util.RequestData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ypy on 2017/6/6.
 */
public class MovieOfCinema extends AppCompatActivity implements BuyButtonListener {

    private ImageView back;
    private TextView cinemaName;
    private ListView movieList;

    private List<HashMap<String, Object>> listItemMovie;
    private MovieAdapter mAdapter;
    private RequestData app;

    public SharedPreferences accountState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list_in_cinema);

        init();
        listItemMovie = new ArrayList<HashMap<String, Object>>();
        app = (RequestData) getApplication();

        mAdapter = new MovieAdapter(getLayoutInflater(), app.getListItemMovie());
        mAdapter.setBuyButtonClickListener((BuyButtonListener) this);
        movieList.setAdapter(mAdapter);

        setListener();
    }

    void init() {
        back = (ImageView)findViewById(R.id.id_cinema_back);
        cinemaName = (TextView)findViewById(R.id.id_cinema_name);
        movieList = (ListView)findViewById(R.id.id_movie_list_in_cinema);

        Intent intent = getIntent();
        cinemaName.setText(intent.getStringExtra("name"));

        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

    }

    void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemAndButtonClick(position);
            }
        });
    }


    private void onItemAndButtonClick(int position) {
        SharedPreferences.Editor editor = accountState.edit();
        editor.putInt("movieId", Integer.parseInt(app.getListItemMovie().get(position).get("id").toString()));
        editor.putInt("nowOrOld", 1);
        editor.commit();
        Intent intent = new Intent(MovieOfCinema.this, ShowTimeSelect.class);
        intent.putExtra("position", position);
        intent.putExtra("cinemaName", cinemaName.getText().toString());
        intent.putExtra("movieName", app.getListItemMovie().get(position).get("name").toString());
        startActivity(intent);
    }

    @Override
    public void BuyButtonClick(final int position) {
        onItemAndButtonClick(position);
    }
}
