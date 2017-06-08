package com.example.administrator.filmgogo;

import android.app.Activity;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/8.
 */

public class OldMovieInfo extends Activity{

    private Button movie_btn, showtime_btn, seat_btn, list_allvotemovie_btn, votemovie_btn, getVotedInfo_btn,
            list_reservation_btn, add_reservation_btn, delete_reservation_btn;
    private TextView response_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldmovie_page);
        init();
        setListener();
    }
    private void init() {
        movie_btn= (Button) findViewById(R.id.oldmovie_request);
        showtime_btn= (Button) findViewById(R.id.oldshowtime_request);
        seat_btn= (Button) findViewById(R.id.oldseat_request);
        list_reservation_btn= (Button) findViewById(R.id.list_oldmovie_reservation);
        add_reservation_btn= (Button) findViewById(R.id.add_oldmovie_reservation);
        delete_reservation_btn= (Button) findViewById(R.id.delete_oldmovie_reservation);
        response_content= (TextView) findViewById(R.id.oldmovie_response_content);
        list_allvotemovie_btn= (Button) findViewById(R.id.list_votemovie);
        votemovie_btn= (Button) findViewById(R.id.vote_movie);
        getVotedInfo_btn= (Button) findViewById(R.id.getVotedInfo);
    }

    private void setListener() {
        movie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(OldMovie_Task).start();
            }
        });

        showtime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(OldShowtime_Task).start();
            }
        });

        seat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(OldSeat_Task).start();
            }
        });

        list_reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(List_ALL_Reservation_Task).start();
            }
        });

        add_reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Add_oldreservation_Task).start();
            }
        });

        delete_reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Delete_oldreservation_Task).start();
            }
        });

        list_allvotemovie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(List_votemovies_Task).start();
            }
        });

        votemovie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(Vote_movie_Task).start();
            }
        });

        getVotedInfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(getVoteMoviesId_Task).start();
            }
        });
    }

    Runnable OldMovie_Task= new Runnable() { //返回所有老电影的信息
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldmovie";
            String result= "";
            String TAG= "getOldMovie";
            try {
                //String url = baseURL + "/1";
                HttpGet httpGet= new HttpGet(baseURL);
                HttpResponse httpResponse= new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            oldmovie_handler.sendMessage(msg);
        }
    };

    Handler oldmovie_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                //response_content.setText(val); /*显示JSON数据格式*/

                JSONArray array= new JSONObject(val).getJSONArray("oldmovies");

                /*-----------------下面是根据JSON数据提取出各个数据的例子，几个请求都给出了处理数据的例子---------------*/
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "+ temp.getString("name") +" "
                            + temp.getString("description")+ " "+temp.getString("img")+ "\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable OldShowtime_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldshowtime";
            String result= "";
            String TAG= "getOldShowTime";
            int movieID= 8;
            try {
                String url = baseURL + '/'+ String.valueOf(movieID);
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
            oldshowtime_handler.sendMessage(msg);
        }
    };

    Handler oldshowtime_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                //response_content.setText(val); /*显示JSON数据格式*/
                JSONArray array= new JSONObject(val).getJSONArray("oldshowtimes");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "+ temp.getString("oldTime") +" "
                            + temp.getString("oldprice")+ "\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable OldSeat_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldseat";
            String result= "";
            String TAG= "getOldSeat";
            int showtime_id= 6;
            try {
                String url = baseURL + '/'+ String.valueOf(showtime_id);
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
            oldseat_handler.sendMessage(msg);
        }
    };

    Handler oldseat_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                //response_content.setText(val); /*显示JSON数据格式*/
                JSONArray array= new JSONObject(val).getJSONArray("oldseats");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "
                            + temp.getString("state")+" "+
                            temp.get("row").toString()+"排"+" "+ temp.get("column").toString()+"座"+"\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable List_ALL_Reservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
            String result= "";
            int customer_id= 4;
            try {
                String url = baseURL + '/'+ String.valueOf(customer_id);
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
            list_handler.sendMessage(msg);
        }
    };

    Handler list_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                JSONArray array= new JSONObject(val).getJSONArray("reservations");
                //response_content.setText(val);
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    if (temp.get("oldmovieName").equals("")) {
                        JSONObject time = temp.getJSONObject("showTime");
                        Date d = new Date(time.getLong("time"));
                        result = result + temp.get("id").toString() + " " + temp.get("movieName") + " " + temp.get("cinemaName")
                                + " " + d + " " + temp.get("ticketPrice") + " " + temp.get("seatRow").toString() + "排"
                                + temp.get("seatColumn").toString() + "座" + "\n";
                    } else {
                        result = result + temp.get("id").toString() + " " + temp.get("oldmovieName") + " "
                                +"金逸珠江国际影城(大学城店)" + " "
                                + temp.get("oldtime")+" "+temp.get("oldPrice") + " " + temp.get("oldseatRow").toString() + "排"
                                + temp.get("oldseatCol").toString() + "座" + "\n";
                    }
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable Add_oldreservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldreservation";
            String TAG = "AddReservation";
            String result = "null";
            int customer_id= 4;
            int seat_id= 402;  //这里根据订单里选中的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/insertOld/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", result);
                msg.setData(data);
                Add_Reservation_handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler Add_Reservation_handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try {
                JSONArray array= new JSONObject(val).getJSONArray("insert_old_reservations");
                //response_content.setText(val);
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+"\n";
                }
                if (result.isEmpty()) response_content.setText("生成订单成功");
                else response_content.setText("该座位已被预定，生成订单失败");
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable Delete_oldreservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldreservation";
            String TAG = "DeleteReservation";
            int customer_id= 4;
            int seat_id= 402;  //这里根据要删除的订单里的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/deleteOld/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable List_votemovies_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/votemovie";
            String result= "";
            String TAG= "listVotemovies";
            try {
                String url = baseURL + "/list";
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
            list_votemovies_handler.sendMessage(msg);
        }
    };

    Handler list_votemovies_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                JSONArray array= new JSONObject(val).getJSONArray("votemovies");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+" "+ temp.getString("name") +" "
                            + temp.getString("description")+ " "+ temp.getString("img")+ " "
                            + temp.get("votes").toString()+"\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable getVoteMoviesId_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/votemovie";
            String result= "";
            int customer_id= 4;
            try {
                String url = baseURL + "/getVoteInfo/"+ customer_id;
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
            getVoteMoviesId_handler.sendMessage(msg);
        }
    };

    Handler getVoteMoviesId_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                JSONArray array= new JSONObject(val).getJSONArray("votemovieid");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+"\n";
                }
                response_content.setText(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };


    Runnable Vote_movie_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/votemovie";
            int movie_id= 1;  //这里指定要投票的电影id
            int customer_id= 4;  //传用户id
            try{
                String url = baseURL + "/vote"+ '/'+ customer_id+'/'+movie_id;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
