package com.example.administrator.filmgogoAdmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by ypy on 2017/6/11.
 */
public class AddShowTime extends Activity {

    private Button commit_btn, back;
    private EditText time, movie_price;
    private TextView movie_name;
    private int position;
    private Data app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_add_show_movie);
        init();
        setListener();
    }
    private void init() {
        commit_btn= (Button) findViewById(R.id.commit_movie);
        back= (Button) findViewById(R.id.back);

        movie_name= (TextView) findViewById(R.id.movie_name);

        time= (EditText) findViewById(R.id.movie_showtime);
        movie_price= (EditText) findViewById(R.id.movie_price);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        app = (Data)getApplication();
        movie_name.setText("电影名： "+app.getShowMovieList().get(position).get("name").toString());
    }

    private void setListener() {
        commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(networkTask).start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/addoldmovie";
            String TAG = "AddMovie";
            String retSrc = "null";
            try {
                HttpPost request = new HttpPost(baseURL);
                // 先封装一个 JSON 对象
                JSONObject param = new JSONObject();
                param.put("name",app.getShowMovieList().get(position).get("name"));
                param.put("description", app.getShowMovieList().get(position).get("description"));
                param.put("image", app.getShowMovieList().get(position).get("icon_url"));
                param.put("time", time.getText().toString());
                param.put("price", movie_price.getText().toString());
                param.put("type", app.getShowMovieList().get(position).get("type"));
                param.put("star", app.getShowMovieList().get(position).get("star"));
                param.put("score", app.getShowMovieList().get(position).get("score"));
                // 绑定到请求 Entry
                StringEntity se = new StringEntity(param.toString(),"UTF-8");
                Log.i(TAG, se.toString());
                request.setEntity(se);
                // 发送请求
                HttpResponse httpResponse = new DefaultHttpClient().execute(request);
                // 得到应答的字符串，这也是一个 JSON 格式保存的数据
                retSrc = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Log.i(TAG, retSrc);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", retSrc);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            //Log.i("mylog", "请求结果为-->" + val);
            String TAG = "json";
            try{
                JSONObject result = new JSONObject(val);
                boolean ok = result.getBoolean("success");
                if (ok) {
                    Toast.makeText(AddShowTime.this, "添加排期成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

}
