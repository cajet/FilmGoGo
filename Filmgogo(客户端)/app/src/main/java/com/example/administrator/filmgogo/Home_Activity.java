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

    Button movie_request_btn, cinema_request_btn, showtime_request_btn,
            seat_request_btn, list_reservation_btn, add_reservation_btn, delete_reservation_btn;
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
        seat_request_btn= (Button) findViewById(R.id.seat_request);
        list_reservation_btn= (Button) findViewById(R.id.list_reservation);
        add_reservation_btn= (Button) findViewById(R.id.add_reservation);
        delete_reservation_btn= (Button) findViewById(R.id.delete_reservation);
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

        seat_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(seatTask).start();
            }
        });

        list_reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(ReservationTask).start();
            }
        });

        add_reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Add_Reservation_Task).start();
            }
        });

        delete_reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Delete_Reservation_Task).start();
            }
        });
    }

    Runnable movieTask= new Runnable() { //返回所有电影的信息
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

                /*-----------------下面是根据JSON数据提取出各个数据的例子，几个请求都给出了处理数据的例子---------------*/
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

    Runnable cinemaTask= new Runnable() {  //返回所有的影院信息
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

    Runnable showtimeTask= new Runnable() { //返回具体影院具体电影的场次
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
                //response_content.setText(val);
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

    Runnable seatTask= new Runnable() {  //返回具体电影的场次的座位信息
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/seat";
            String result = "null";
            String TAG = "getSeat";
            int showtime_id= 1;  //这里根据选中的电影场次来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(showtime_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", result);
                msg.setData(data);
                seat_handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler seat_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try {
                //response_content.setText(val);
                JSONArray array= new JSONObject(val).getJSONArray("seats");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "
                            + temp.getString("state")+" "+
                            temp.get("row").toString()+"排"+" "+ temp.get("column").toString()+"座"+"\n";
                }
                response_content.setText(result);
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };
/*
    Runnable setSeatTask= new Runnable() {  //在确定订单或取消订单时调用座位状态改变请求
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/seat";
            String TAG = "setSeat";
            int showtime_id= 1;
            int seat_id= 1;  //这里根据选中的电影场次和座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(showtime_id)+'/'+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
*/
    Runnable ReservationTask= new Runnable() {
        @Override
        public void run() {
            String baseURL= "http://172.18.71.17:8080/FilmGoGo/reservation";
            String result = "null";
            String TAG = "getReservation";
            int customer_id= 4;  //这里根据登录的客户来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", result);
                msg.setData(data);
                Reservation_handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler Reservation_handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try {
                JSONArray array= new JSONObject(val).getJSONArray("reservations");
                //response_content.setText(val);
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    JSONObject time = temp.getJSONObject("showTime");
                    Date d = new Date(time.getLong("time"));
                    result= result + temp.get("id").toString()+" "+ temp.get("movieName")+ " "+temp.get("cinemaName")
                            + " "+ d + " "+ temp.get("ticketPrice") + " "+ temp.get("seatRow").toString()+"排"
                            + temp.get("seatColumn").toString()+"座"+"\n";
                }
                response_content.setText(result);
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable Add_Reservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
            String TAG = "AddReservation";
            int customer_id= 4;
            int seat_id= 1;  //这里根据订单里选中的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/insert/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable Delete_Reservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
            String TAG = "DeleteReservation";
            int customer_id= 4;
            int seat_id= 1;  //这里根据要删除的订单里的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/delete/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
