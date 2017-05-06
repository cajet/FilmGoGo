package com.example.administrator.filmgogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by cajet on 2017/4/29.
 */

public class Home_Activity extends Activity{

    Button movie_request_btn, cinema_request_btn, showtime_request_btn;
    TextView response_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        init();
        setListener();
    }

    private void init() {
        movie_request_btn= (Button) findViewById(R.id.movie_request);
        cinema_request_btn= (Button) findViewById(R.id.cinema_request);
        showtime_request_btn= (Button) findViewById(R.id.showtime_request);
        response_content= (TextView) findViewById(R.id.response_content);
    }

    private void setListener() {
        movie_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(movieTask).start();
            }
        });

        cinema_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(cinemaTask).start();
            }
        });

        showtime_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(showtimeTask).start();
            }
        });
    }

	//返回所有电影的信息
    Runnable movieTask= new Runnable() { 
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/movie";
            String result= "";
            String TAG= "getMovie";
            int cinemaID= 1;//根据数据库第一个影院行数据的id改，前端暂不需要改这个数据
            try {
                String url = baseURL + "/"+String.valueOf(cinemaID);
                HttpGet httpGet= new HttpGet(url);
                HttpResponse httpResponse= new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            movie_handler.sendMessage(msg);
        }
    };

    Handler movie_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                //response_content.setText(val); /*显示JSON数据格式*/

                JSONArray array= new JSONObject(val).getJSONArray("movies");

                /*-----------------下面是根据JSON数据提取出各个数据的例子---------------*/
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "+ temp.getString("name") + " "+ temp.getString("type")+" "
                            + temp.getString("description")+ " "+temp.getString("img")+ "\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    //返回所有的影院信息
    Runnable cinemaTask= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/cinema";
            String result= "";
            String TAG= "getCinema";
            int movieID= 1; //根据数据库第一个电影行数据的id改，前端暂不需要改这个数据
            try {
                String url = baseURL + "/"+String.valueOf(movieID);
                HttpGet httpGet= new HttpGet(url);
                HttpResponse httpResponse= new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            cinema_handler.sendMessage(msg);
        }
    };

    Handler cinema_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                //response_content.setText(val);

                JSONArray array= new JSONObject(val).getJSONArray("cinemas");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "+ temp.getString("name") + " "+ temp.getString("address")+" "
                            + temp.getString("city")+ " "+temp.getString("area")+ "\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };
    
	//返回具体影院具体电影的场次
    Runnable showtimeTask= new Runnable() { 
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/showtime";
            String result = "null";
            String TAG = "getShowtime";
            int movie_id= 1;  //这里根据选中的电影和电影院来指定id
            int cinema_id= 1;
            try{
                String url = baseURL + '/'+ String.valueOf(cinema_id) + '/' + String.valueOf(movie_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", result);
                msg.setData(data);
                showtime_handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler showtime_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try {
                JSONArray array= new JSONObject(val).getJSONArray("showtimes");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    JSONObject time = temp.getJSONObject("time");
                    Date d = new Date(time.getLong("time"));
                    result= result + temp.get("id").toString()+" "+ d + " "
                            + temp.getString("price")+" "+ "\n";
                }
                response_content.setText(result);
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

}
