package com.example.administrator.filmgogoAdmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ypy on 2017/6/10.
 */
public class ShowTimeActivity extends Activity{
    private MySimpleAdapter adapter;
    private android.os.Handler handler;
    private Data app;
    private TextView name;
    private ListView showTimeList;
    private Button add;
    private int p;
    private int positionInShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_time);
        app = (Data) getApplication();
        Intent intent = getIntent();
        positionInShow = intent.getIntExtra("position", -1);
        app.setShowTimeList(Integer.parseInt(app.getShowMovieList().get(positionInShow).get("id").toString()));

        name = (TextView)findViewById(R.id.movie_name);

        name.setText(app.getShowMovieList().get(positionInShow).get("name").toString());

        showTimeList = (ListView) findViewById(R.id.show_time_list);

        add = (Button)findViewById(R.id.id_add_show_time) ;

        adapter = new MySimpleAdapter(this, app.getShowTimeList(), R.layout.item_show_time,
                new String[]{"time", "price"}, new int[]{R.id.id_show_time
                , R.id.id_show_time_price});

        showTimeList.setAdapter(adapter);

        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTimeActivity.this, AddShowTime.class);
                intent.putExtra("position", positionInShow);
                startActivity(intent);
//                Intent intent = new Intent(ShowTimeActivity.this, AddShowMovie.class);
//                startActivity(intent);
            }
        });


    }
    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getShowTimeList().size() == 0) {
                handler.postDelayed(this, 150);
                return;
            }

            adapter.notifyDataSetChanged();

        }
    };
    private class MySimpleAdapter extends SimpleAdapter {
        public MySimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
            // TODO Auto-generated constructor stub
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub
            View v= super.getView(position, convertView, parent);

            Button delete = (Button)v.findViewById(R.id.delete);


            delete.setTag(position);

            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    p = position;
                    new Thread(Delete_one_showtime_Task).start();
                    app.getShowTimeList().remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            return v;
        }
    }
    Runnable Delete_one_showtime_Task = new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/deleteOneShowtime";
            String TAG = "DeleteOneShowtime";
            String retSrc = "null";
            try {
                HttpPost request = new HttpPost(baseURL);
                JSONObject param = new JSONObject();
                param.put("name", name.getText().toString());
                param.put("time", app.getShowTimeList().get(p).get("time"));
                StringEntity se = new StringEntity(param.toString(),"UTF-8");
                Log.i(TAG, se.toString());
                request.setEntity(se);
                HttpResponse httpResponse = new DefaultHttpClient().execute(request);
                retSrc = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Log.i(TAG, retSrc);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", retSrc);
            msg.setData(data);
            delete_one_handler.sendMessage(msg);
        }
    };

    Handler delete_one_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            try{
                JSONObject result = new JSONObject(val);
                boolean ok = result.getBoolean("success");
                if (ok) {
                    Toast.makeText(ShowTimeActivity.this, "删除场次成功", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

}
