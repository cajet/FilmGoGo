package com.example.administrator.filmgogo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/8.
 */

public class Manager_add_votemovies extends Activity{

    private EditText name, description, image, star, score, type;
    private Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_add_votemovies);
        init();
        setListener();
    }

    private void init() {
        name= (EditText) findViewById(R.id.add_votemovie_name);
        description= (EditText) findViewById(R.id.add_votemovie_description);
        image= (EditText) findViewById(R.id.add_votemovie_imageurl);
        commit= (Button) findViewById(R.id.commit_add_movie);
        star= (EditText) findViewById(R.id.add_votemovie_star);
        score= (EditText) findViewById(R.id.add_votemovie_score);
        type= (EditText) findViewById(R.id.add_votemovie_type);
    }

    private void setListener() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Add_Votemovie_Task).start();
            }
        });
    }
    Runnable Add_Votemovie_Task = new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/addvotemovie";
            String retSrc = "null";
            try {
                HttpPost request = new HttpPost(baseURL);
                JSONObject param = new JSONObject();
                param.put("name", name.getText().toString());
                param.put("description", description.getText().toString());
                param.put("image", image.getText().toString());
                param.put("star", star.getText().toString());
                param.put("score", score.getText().toString());
                param.put("type", type.getText().toString());
                StringEntity se = new StringEntity(param.toString(),"UTF-8");
                request.setEntity(se);
                HttpResponse httpResponse = new DefaultHttpClient().execute(request);
                retSrc = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
            String TAG = "json";
            try{
                JSONObject result = new JSONObject(val);
                boolean ok = result.getBoolean("success");
                if (ok) {
                    Toast.makeText(Manager_add_votemovies.this, "添加投票电影成功", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };
}
