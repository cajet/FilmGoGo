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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/8.
 */

public class Manager_delete_votemovies extends Activity{
    private Button commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_delete_votemovies);
        init();
        setListener();
    }
    private void init() {
        commit= (Button) findViewById(R.id.delete_votemovie);
    }
    private void setListener() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Delete_Votemovie_Task).start();
            }
        });
    }
    Runnable Delete_Votemovie_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/deletevotemovie";
            int votemovie_id= 22; //指定要删除的投票电影id
            try{
                String url = baseURL + '/'+ votemovie_id;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
