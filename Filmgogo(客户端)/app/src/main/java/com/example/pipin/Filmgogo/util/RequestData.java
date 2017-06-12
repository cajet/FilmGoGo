package com.example.pipin.Filmgogo.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Pipin on 2017/6/7.
 */



public class RequestData extends Application{
    public SharedPreferences accountState;

    private ArrayList<HashMap<String, Object>> listItemMovie, listItemCinema, listItemOldMovie, listItemVoteOldMovie
            , listItemMovieShowTime, listItemOldMovieShowTime, listItemMovieSeat, listItemOldMovieSeat;

    private int userId;



    @Override
    public void onCreate(){

        super.onCreate();
        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

        setListItemMovie();
        setListItemCinema();
        setListItemOldMovie();
        setListItemVoteOldMovie();

        listItemOldMovieShowTime = new ArrayList<HashMap<String, Object>>();
        listItemMovieShowTime = new ArrayList<HashMap<String, Object>>();

        listItemMovieSeat = new ArrayList<HashMap<String, Object>>();
        listItemOldMovieSeat = new ArrayList<HashMap<String, Object>>();

    }


    public ArrayList<HashMap<String, Object>> getListItemMovie() {
        return listItemMovie;
    }

    public ArrayList<HashMap<String, Object>> getListItemCinema() {
        return listItemCinema;
    }

    public ArrayList<HashMap<String, Object>> getListItemOldMovie() {
        return listItemOldMovie;
    }

    public ArrayList<HashMap<String, Object>> getListItemVoteOldMovie() {
        return listItemVoteOldMovie;
    }

    public void setListItemVoteOldMovie() {
        listItemVoteOldMovie = new ArrayList<HashMap<String, Object>>();
        new Thread(List_votemovies_Task).start();
    }

    public void setListItemOldMovie() {
        listItemOldMovie = new ArrayList<HashMap<String, Object>>();
        new Thread(OldMovie_Task).start();
    }

    public void setListItemCinema() {
        listItemCinema = new ArrayList<HashMap<String, Object>>();
        new Thread(cinemaTask).start();
    }

    public void setListItemMovie() {
        listItemMovie = new ArrayList<HashMap<String, Object>>();
        new Thread(movieTask).start();
    }

    public ArrayList<HashMap<String, Object>> getListItemOldMovieShowTime() {

        return listItemOldMovieShowTime;
    }

    public ArrayList<HashMap<String, Object>> getListItemMovieShowTime() {

        return listItemMovieShowTime;
    }

    public ArrayList<HashMap<String, Object>> getListItemMovieSeat() {

        return listItemMovieSeat;
    }
    public ArrayList<HashMap<String, Object>> getListItemOldMovieSeat() {

        return listItemOldMovieSeat;
    }

    public int getUserId() {
        new Thread(GetIdByName_Task).start();
        return userId;
    }

    public void addReservation() {
        new Thread(Add_Reservation_Task).start();
    }
    public void addOldMovieReservation() {

        new Thread(Add_oldreservation_Task).start();
    }


    public void setListItemOldMovieShowTime() {

        listItemOldMovieShowTime = new ArrayList<HashMap<String, Object>>();
        new Thread(OldShowtime_Task).start();
        Comparator comp = new SortComparator();
        Collections.sort(listItemOldMovieShowTime,comp);
    }

    public void setListItemMovieShowTime() {

        listItemMovieShowTime = new ArrayList<HashMap<String, Object>>();
        new Thread(showtimeTask).start();

    }

    public void setListItemOldMovieSeat() {

        listItemOldMovieSeat = new ArrayList<HashMap<String, Object>>();
        new Thread(OldSeat_Task).start();

    }

    public void setListItemMovieSeat() {

        listItemMovieSeat = new ArrayList<HashMap<String, Object>>();
        new Thread(seatTask).start();

    }



    public void setVoteMovie(final int movieId, final int userId) {
        Runnable Vote_movie_Task= new Runnable() {
            @Override
            public void run() {
                String baseURL = "http://172.18.71.17:8080/FilmGoGo/votemovie";
                int movie_id= movieId;  //这里指定要投票的电影id
                int customer_id= userId;  //传用户id
                try{
                    String url = baseURL + "/vote"+ '/'+ customer_id+'/'+movie_id;
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(Vote_movie_Task).start();
    }


    Runnable movieTask = new Runnable() { //返回所有电影的信息
        @Override
        public void run() {

            String baseURL = "http://172.18.71.17:8080/FilmGoGo/movie";
            String result = "";
            String TAG = "getMovie";

            int cinemaID = 1;//根据数据库第一个影院行数据的id改，前端暂不需要改这个数据



            try {

                String url = baseURL + "/" + String.valueOf(cinemaID);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
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

     Handler movie_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";

            try {
                //response_content.setText(val); /*显示JSON数据格式*/

                JSONArray array = new JSONObject(val).getJSONArray("movies");

                /*-----------------下面是根据JSON数据提取出各个数据的例子，几个请求都给出了处理数据的例子---------------*/
                for (int i = 0; i < array.length(); ++i) {
                    final int ii = i;
                    final HashMap<String, Object> item = new HashMap<String, Object>();
                    final JSONObject temp = array.getJSONObject(i);
                    item.put("id", temp.get("id").toString());
                    item.put("name", temp.getString("name"));
                    item.put("type", temp.getString("type"));
                    item.put("description", temp.getString("description"));
                    item.put("icon_url",temp.getString("img"));
                    item.put("score", temp.get("score").toString());
                    item.put("star", temp.get("star").toString());
                    listItemMovie.add(item);
                    new Thread(new Runnable() { //返回所有电影的信息
                        @Override
                        public void run() {

                            try {

                                Bitmap bitmap = getBitmap(temp.getString("img"));

                                listItemMovie.get(ii).put("icon", bitmap);
//                                MovieAdapter.notifyDataSetChanged();
//                                Message msg = new Message();
//                                Bundle data = new Bundle();
//                                data.putString("value", result);
//                                msg.setData(data);
//                                movie_handler.sendMessage(msg);
                            } catch (Exception e){}
                        }
                    }).start();
                }

            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

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
                    final int ii = i;
                    final HashMap<String, Object> item = new HashMap<>();
                    final JSONObject temp = array.getJSONObject(i);
                    item.put("id", temp.get("id").toString());
                    item.put("name", temp.getString("name"));
                    item.put("score", temp.get("score").toString());
                    item.put("star", temp.get("star").toString());
                    item.put("type", temp.getString("type"));
                    item.put("description", temp.getString("description"));
                    item.put("icon_url",temp.getString("img"));
                    listItemOldMovie.add(item);
                    new Thread(new Runnable() { //返回所有电影的信息
                        @Override
                        public void run() {

                            try {

                                Bitmap bitmap = getBitmap(temp.getString("img"));

                                listItemOldMovie.get(ii).put("icon", bitmap);

                            } catch (Exception e){}
                        }
                    }).start();

                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    public Bitmap getBitmap(String UrlPath) {
        Bitmap bm = null;
        // 1、确定网址
        // http://pic39.nipic.com/20140226/18071023_164300608000_2.jpg
        String urlpath = UrlPath;
        // 2、获取Uri
        try {
            URL uri = new URL(urlpath);

            // 3、获取连接对象、此时还没有建立连接
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4、初始化连接对象
            // 设置请求的方法，注意大写
            connection.setRequestMethod("GET");
            // 读取超时
            connection.setReadTimeout(5000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // 5、建立连接
            connection.connect();

            // 6、获取成功判断,获取响应码
            if (connection.getResponseCode() == 200) {
                // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = connection.getInputStream();
                // 8、从流中读取数据，构造一个图片对象GoogleAPI
                bm = BitmapFactory.decodeStream(is);
                // 9、把图片设置到UI主线程
                // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；

                Log.i("", "网络请求成功");

            } else {
                Log.v("tag", "网络请求失败");
                bm = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

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
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    JSONObject temp = array.getJSONObject(i);
                    item.put("id", temp.get("id").toString());
                    item.put("name", temp.getString("name"));
                    item.put("location", temp.getString("address"));
                    listItemCinema.add(item);
                }

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
            int movie_id= accountState.getInt("movieId", 1);//这里根据选中的电影和电影院来指定id
            int cinema_id= accountState.getInt("cinemaId", 1);
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
                int j = 0;
                ArrayList<HashMap<String, Object>> listItemtemp = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    JSONObject time = temp.getJSONObject("time");
                    Date d = new Date(time.getLong("time"));

                    HashMap<String, Object> item1 = new HashMap<String, Object>();



                    listItemtemp.add(spiltString(d.toString()));


                    item1.put("id", temp.get("id"));
                    item1.put("buttonTitle", listItemtemp.get(i).get("buttonTitle"));
                    item1.put("time",listItemtemp.get(i).get("time"));
                    item1.put("price",temp.get("price").toString());
                    listItemMovieShowTime.add(item1);

                }

            } catch (Exception e) {
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
            int movieID= accountState.getInt("movieId", 1);

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
            try {
                //response_content.setText(val);
                JSONArray array= new JSONObject(val).getJSONArray("oldshowtimes");

                ArrayList<HashMap<String, Object>> listItemtemp = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);

                    HashMap<String, Object> item1 = new HashMap<String, Object>();

                    listItemtemp.add(spiltString2(temp.getString("oldTime")));


                    item1.put("id", temp.get("id"));
                    item1.put("buttonTitle", listItemtemp.get(i).get("buttonTitle"));
                    item1.put("time",listItemtemp.get(i).get("time"));
                    item1.put("price",temp.get("oldprice").toString());
                    item1.put("compare",listItemtemp.get(i).get("compare") );
                    listItemOldMovieShowTime.add(item1);

                }

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
            int showtime_id= accountState.getInt("showTimeId", 1);  //这里根据选中的电影场次来指定id

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

    android.os.Handler seat_handler= new android.os.Handler() {
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

                    final HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("id", temp.get("id"));
                    item.put("state", temp.getString("state"));
                    item.put("row", temp.getString("row"));
                    item.put("column", temp.getString("column"));
                    listItemMovieSeat.add(item);

                }

            } catch (Exception e) {
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
            int showtime_id= accountState.getInt("showTimeId", 1);
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
                    final HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("id", temp.get("id"));
                    item.put("state", temp.getString("state"));
                    item.put("row", temp.getString("row"));
                    item.put("column", temp.getString("column"));
                    listItemOldMovieSeat.add(item);

                }

            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };


    Runnable Add_Reservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
            String TAG = "AddReservation";
            String result = "null";
            int customer_id= accountState.getInt("id", -1);
            int seat_id= accountState.getInt("seatId", -1);  //这里根据订单里选中的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/insert/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable Add_oldreservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldreservation";
            String TAG = "AddReservation";
            String result = "null";
            int customer_id=  accountState.getInt("id", -1);
            int seat_id= accountState.getInt("seatId", -1); //这里根据订单里选中的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/insertOld/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //获取投票页电影
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

    private ArrayList<HashMap<String, Object>> listNoPayReservation, listPayReservation, listAllReservation;
    public void setListReservation() {
        listNoPayReservation = new ArrayList<HashMap<String, Object>>();
        listPayReservation = new ArrayList<HashMap<String, Object>>();
        listAllReservation = new ArrayList<HashMap<String, Object>>();
        new Thread(List_ALL_Reservation_Task).start();
    }
    public ArrayList<HashMap<String, Object>> getListAllReservation() {
        return listAllReservation;
    }

    public ArrayList<HashMap<String, Object>> getListNoPayReservation() {
        return listNoPayReservation;
    }

    public ArrayList<HashMap<String, Object>> getListPayReservation() {
        return listPayReservation;
    }

    Runnable List_ALL_Reservation_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
            String result= "";
            int customer_id= accountState.getInt("id", -1);
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
                        final HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("nowOrOld", "now");
                        item.put("id", temp.get("id").toString());
                        item.put("movieName", temp.getString("movieName").toString());
                        item.put("cinemaName", temp.getString("cinemaName").toString());
                        item.put("time", spiltString1(""+d));
                        item.put("ticketPrice", temp.get("ticketPrice").toString());
                        item.put("seatRow", temp.get("seatRow").toString());
                        item.put("seatColumn", temp.get("seatColumn").toString());
                        item.put("seatId", temp.get("seatid").toString());
                        item.put("pay", temp.getBoolean("pay"));
                        if (item.get("pay").equals(true)) {
                            listPayReservation.add(item);
                        } else {
                            listNoPayReservation.add(item);
                        }
                        listAllReservation.add(item);

                    } else {
                        final HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("nowOrOld", "old");
                        item.put("id", temp.get("id").toString());
                        item.put("movieName", temp.getString("oldmovieName").toString());
                        item.put("cinemaName","金逸珠江国际影城(大学城店)");
                        item.put("time", temp.get("oldtime").toString());
                        item.put("ticketPrice", temp.get("oldPrice").toString());
                        item.put("seatRow", temp.get("oldseatRow").toString());
                        item.put("seatColumn", temp.get("oldseatCol").toString());
                        item.put("seatId", temp.get("oldSeatid").toString());
                        item.put("pay", temp.getBoolean("pay"));
                        if (item.get("pay").equals(true)) {
                            listPayReservation.add(item);
                        } else {
                            listNoPayReservation.add(item);
                        }
                        listAllReservation.add(item);
                    }
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    String spiltString1(String str) {
        String result = "";
        String [] temp = null;
        temp = str.split(" ");
        result = result + temp[5] + "-" +monthToChinese(temp[1]) + "-" + temp[2] + "  " +
                temp[3];

        return result;
    }

    public void setDelete_Reservation_Task(final int seatId) {
        Runnable Delete_Reservation_Task= new Runnable() {
            @Override
            public void run() {
                String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
                String TAG = "DeleteReservation";
                int customer_id= accountState.getInt("id", -1);
                int seat_id= seatId;  //这里根据要删除的订单里的座位来指定id
                try{
                    String url = baseURL + '/'+ String.valueOf(customer_id)+"/delete/"+ String.valueOf(seat_id);
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(Delete_Reservation_Task).start();
    }
    public void setDelete_oldreservation_Task(final int seatId) {
        Runnable Delete_oldreservation_Task= new Runnable() {
            @Override
            public void run() {
                String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldreservation";
                String TAG = "DeleteReservation";
                int customer_id= accountState.getInt("id", -1);
                int seat_id= seatId;  //这里根据要删除的订单里的座位来指定id
                try{
                    String url = baseURL + '/'+ String.valueOf(customer_id)+"/deleteOld/"+ String.valueOf(seat_id);
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(Delete_oldreservation_Task).start();
    }


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
                    final JSONObject temp = array.getJSONObject(i);
                    final HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("id", temp.get("id"));
                    item.put("name", temp.getString("name"));
                    item.put("type", temp.getString("type"));
                    item.put("description", temp.getString("description"));
                    item.put("icon", temp.getString("img"));
                    item.put("score", temp.getString("score"));
                    item.put("star", temp.getString("star"));
                    item.put("votes", temp.getString("votes"));
                    listItemVoteOldMovie.add(item);
                   final int ii =i;
                    new Thread(new Runnable() { //返回所有电影的信息
                        @Override
                        public void run() {

                            try {

                                Bitmap bitmap = getBitmap(temp.getString("img"));

                                listItemVoteOldMovie.get(ii).put("icon", bitmap);

                            } catch (Exception e){}
                        }
                    }).start();
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

//根据用户名获取用户id
    Runnable GetIdByName_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL= "http://172.18.71.17:8080/FilmGoGo/customer";
            String result= "";
            String name= accountState.getString("userName", "");
            try {
                String url= baseURL + "/getIdByName/" + name;
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
            GetIdByName_handler.sendMessage(msg);
        }
    };

    Handler GetIdByName_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                //response_content.setText(val); /*显示JSON数据格式*/

                JSONArray array= new JSONObject(val).getJSONArray("customerId");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString();
                }
                userId = Integer.parseInt(result);
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    //根据用户id获取投票电影
    private ArrayList<HashMap<String, Object>> userLikeMovie;

    public void setUserLikeMovie() {
        userLikeMovie = new ArrayList<HashMap<String, Object>>();
        new Thread(getVoteMoviesId_Task).start();
    }

    public ArrayList<HashMap<String, Object>> getUserLikeMovie() {
        for (int i = 0; i < userLikeMovie.size(); i ++) {
            for (int j = i+1; j < userLikeMovie.size(); j++) {
                if(userLikeMovie.get(j).get("id").equals(userLikeMovie.get(i).get("id"))) {
                    userLikeMovie.remove(j);
                }
            }

        }
        return userLikeMovie;
    }


    Runnable getVoteMoviesId_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/votemovie";
            String result= "";
            int customer_id= accountState.getInt("id", -1);
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
            try{
                JSONArray array= new JSONObject(val).getJSONArray("votemovieid");
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    for (int j = 0; j < listItemVoteOldMovie.size(); j++){
                        if (temp.get("id").toString().equals(listItemVoteOldMovie.get(j).get("id").toString())) {
                            userLikeMovie.add(listItemVoteOldMovie.get(j));
//                            item.put("id", temp.get("id"));
//                            item.put("name", temp.getString("name"));
//                            item.put("type", temp.getString("type"));
//                            item.put("description", temp.getString("description"));
//                            item.put("icon", temp.getString("img"));
//                            item.put("score", temp.getString("score"));
//                            item.put("star", temp.getString("star"));
//                            item.put("votes", temp.getString("votes"));
                        }
                    }
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };


    public class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            HashMap<String, Object> item1 = (HashMap<String, Object>)lhs;
            HashMap<String, Object> item2 = (HashMap<String, Object>)rhs;

            return (Integer.parseInt(item1.get("compare").toString()) - Integer.parseInt(item2.get("compare").toString()));
        }
    }


    HashMap<String, Object> spiltString(String str) {
        HashMap<String, Object> result = new HashMap<String, Object>() ;
        String [] temp = null;
        temp = str.split(" ");
        result.put("buttonTitle",xingQiToChinese(temp[0]) + monthToChinese(temp[1]) + "-" + temp[2]);
        result.put("time", temp[3]);
        return result;
    }

    HashMap<String, Object> spiltString2(String str) throws java.text.ParseException {
        HashMap<String, Object> result = new HashMap<String, Object>() ;
        String [] temp = null;
        String [] temp2 = null;
        temp = str.split(" ");
        temp2 = temp[0].split("-");
        result.put("buttonTitle",getWeek(temp[0]) + temp2[1] + "-" + temp2[2]);
        result.put("time", temp[1]);
        result.put("compare", temp2[1]+temp2[2]);
        return result;
    }

    private String getWeek(String pTime) throws java.text.ParseException {


        String Week = "星期";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK)  == Calendar.SUNDAY) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }



        return Week;
    }

    String xingQiToChinese(String str) {
        String result = "";
        if (str.equals("Mon")) {
            result += "星期一";
        } else if (str.equals("Tue")) {
            result += "星期二";
        } else if (str.equals("Wed")) {
            result += "星期三";
        } else if (str.equals("Thu")) {
            result += "星期四";
        } else if (str.equals("Fri")) {
            result += "星期五";
        } else if (str.equals("Sat")) {
            result += "星期六";
        } else if (str.equals("Sun")) {
            result += "星期日";
        }
        return result;

    }

    String monthToChinese(String str) {
        String result = "";
        if (str.equals("Jan")) {
            result += "1";
        } else if (str.equals("Feb")) {
            result += "2";
        } else if (str.equals("Mar")) {
            result += "3";
        } else if (str.equals("Apr")) {
            result += "4";
        } else if (str.equals("May")) {
            result += "5";
        } else if (str.equals("Jun")) {
            result += "6";
        } else if (str.equals("Jul")) {
            result += "7";
        } else if (str.equals("Aug")) {
            result += "8";
        } else if (str.equals("Sep")) {
            result += "9";
        } else if (str.equals("Oct")) {
            result += "10";
        } else if (str.equals("Nov")) {
            result += "11";
        } else if (str.equals("Dec")) {
            result += "12";
        }
        return result;

    }



}
