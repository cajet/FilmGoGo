package com.example.pipin.Filmgogo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.adapter.CinemaAdapter;
import com.example.pipin.Filmgogo.util.RequestData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ypy on 2017/6/6.
 */
public class movieDescription extends AppCompatActivity {

    private ImageView back, iv_movie_img;
    private TextView title_movie_name, name, description, tv_actor, tv_score, tv_type;
    private Button selectCinema;
    private ListView cinemaList;
    private RequestData app;

    private int position;

    private CinemaAdapter adapterCinema;

    public SharedPreferences accountState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        init();
        setListener();
        if (accountState.getInt("nowOrOld", 1) == 1) {
            adapterCinema = new CinemaAdapter(getLayoutInflater(), app.getListItemCinema());
            cinemaList.setAdapter(adapterCinema);
        } else {
            ArrayList<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("name", "金逸珠江国际影城(大学城店)");
            tempMap.put("location", "番禺区小谷围街贝岗村中二横路1号高高新天地商业广场B2B001铺");
            tempList.add(tempMap);
            adapterCinema = new CinemaAdapter(getLayoutInflater(), tempList);
            cinemaList.setAdapter(adapterCinema);
        }

        setListViewHeightBasedOnChildren(cinemaList);

    }

    void init() {
        back = (ImageView) findViewById(R.id.id_description_back);
        name = (TextView) findViewById(R.id.id_description_movie_name);
        description = (TextView) findViewById(R.id.id_description_content);
        tv_actor = (TextView) findViewById(R.id.tv_actor);
        cinemaList = (ListView) findViewById(R.id.id_select_movie_list);
        title_movie_name = (TextView) findViewById(R.id.title_movie_name);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_type = (TextView) findViewById(R.id.tv_type);
        iv_movie_img = (ImageView) findViewById(R.id.iv_movie_img);

        app = (RequestData) getApplication();

        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 1);
        if (accountState.getInt("nowOrOld", 1) == 1) {
            name.setText(app.getListItemMovie().get(position).get("name").toString());
            title_movie_name.setText(app.getListItemMovie().get(position).get("name").toString());
            description.setText(app.getListItemMovie().get(position).get("description").toString());
            tv_actor.setText(app.getListItemMovie().get(position).get("star").toString());
            tv_score.setText(app.getListItemMovie().get(position).get("score").toString());
            tv_type.setText(app.getListItemMovie().get(position).get("type").toString());
            if (app.getListItemMovie().get(position).get("icon") instanceof Bitmap) {
                iv_movie_img.setImageBitmap((Bitmap) app.getListItemMovie().get(position).get("icon"));
            }

        } else {
            name.setText(app.getListItemOldMovie().get(position).get("name").toString());
            title_movie_name.setText(app.getListItemOldMovie().get(position).get("name").toString());
            description.setText(app.getListItemOldMovie().get(position).get("description").toString());
            tv_actor.setText(app.getListItemOldMovie().get(position).get("star").toString());
            tv_score.setText(app.getListItemOldMovie().get(position).get("score").toString());
            tv_type.setText(app.getListItemOldMovie().get(position).get("type").toString());
            if (app.getListItemOldMovie().get(position).get("icon") instanceof Bitmap) {
                iv_movie_img.setImageBitmap((Bitmap) app.getListItemOldMovie().get(position).get("icon"));
            }
        }
    }

    void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cinemaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = accountState.edit();
                editor.putInt("cinemaId", Integer.parseInt(app.getListItemCinema().get(i).get("id").toString()));
                editor.commit();
                Intent intent = new Intent(movieDescription.this, ShowTimeSelect.class);
                intent.putExtra("movieName", name.getText().toString());
                intent.putExtra("cinemaName", app.getListItemCinema().get(i).get("name").toString());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        int totalHeight = 0;
        for (int i = 0; i < adapterCinema.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adapterCinema.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (adapterCinema.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
