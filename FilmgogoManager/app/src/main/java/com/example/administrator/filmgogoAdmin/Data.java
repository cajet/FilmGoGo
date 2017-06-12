package com.example.administrator.filmgogoAdmin;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by ypy on 2017/6/9.
 */
public class Data extends Application {
    private ArrayList<HashMap<String, Object>> OldMovieList, showMovieList, showTimeList;
    private boolean isZero;
    private int movieId = -1;
    private int movie_data_id;
    @Override
    public void onCreate() {
        super.onCreate();
        OldMovieList = new ArrayList<HashMap<String, Object>>();

        new Thread(List_votemovies_Task).start();
        showMovieList = new ArrayList<HashMap<String, Object>>();
        new Thread(OldMovie_Task).start();
        showTimeList =  new ArrayList<HashMap<String, Object>>();

    }

    public void setShowTimeList(int id) {
        movieId = id;
        showTimeList =  new ArrayList<HashMap<String, Object>>();
        new Thread(OldShowtime_Task).start();
    }
    public void setOldMovieList() {
        OldMovieList = new ArrayList<HashMap<String, Object>>();

        new Thread(List_votemovies_Task).start();
    }
    public void setShowMovieList() {
        showMovieList = new ArrayList<HashMap<String, Object>>();
        new Thread(OldMovie_Task).start();
    }

    public ArrayList<HashMap<String, Object>> getShowTimeList() {
        return showTimeList;
    }
    public ArrayList<HashMap<String, Object>> getOldMovieList() {
        findShow();

        return OldMovieList;
    }


    public ArrayList<HashMap<String, Object>> getShowMovieList() {
        return showMovieList;
    }

    private void findShow() {
       for (int i = 0; i < OldMovieList.size(); i++) {
           OldMovieList.get(i).put("showState", "上映");
           int j;
           for (j = 0; j < showMovieList.size(); j++) {
               if (OldMovieList.get(i).get("name").equals(showMovieList.get(j).get("name"))) {
                   OldMovieList.get(i).put("showState", "已上映");
               }
           }
       }
    }
    private void findVote() {
        for (int i = 0; i < showMovieList.size(); i++) {
            int j;
            for (j = 0; j < OldMovieList.size(); j++) {
                if (OldMovieList.get(j).get("name").equals(showMovieList.get(i).get("name"))) {
                    showMovieList.get(i).put("voteId", OldMovieList.get(j).get("id"));
                    showMovieList.get(i).put("votes", OldMovieList.get(j).get("votes"));
                }
            }
        }
    }
    public void sort(ArrayList<HashMap<String, Object>> list) {
        for (int i = 0; i <list.size()-1; i++) {
            int j = i;
            for (j = i+1; j < list.size(); j++) {
                if (Integer.parseInt(list.get(i).get("votes").toString()) < Integer.parseInt(list.get(j).get("votes").toString())) {
                    HashMap<String, Object> temp1 = new HashMap<String, Object>();

                    temp1.put("id", list.get(i).get("id"));
                    temp1.put("name", list.get(i).get("name"));
                    temp1.put("type", list.get(i).get("type"));
                    temp1.put("description", list.get(i).get("description"));
                    temp1.put("icon_url",list.get(i).get("icon_url"));
                    temp1.put("score", list.get(i).get("score"));
                    temp1.put("star", list.get(i).get("star"));
                    temp1.put("icon", list.get(i).get("icon"));
                    temp1.put("votes", list.get(i).get("votes"));

                    list.get(i).put("id", list.get(j).get("id"));
                    list.get(i).put("name", list.get(j).get("name"));
                    list.get(i).put("type", list.get(j).get("type"));
                    list.get(i).put("description", list.get(j).get("description"));
                    list.get(i).put("icon_url",list.get(j).get("icon_url"));
                    list.get(i).put("score", list.get(j).get("score"));
                    list.get(i).put("star", list.get(j).get("star"));
                    list.get(i).put("icon", list.get(j).get("icon"));
                    list.get(i).put("votes", list.get(j).get("votes"));


                    list.get(j).put("id", temp1.get("id"));
                    list.get(j).put("name", temp1.get("name"));
                    list.get(j).put("type", temp1.get("type"));
                    list.get(j).put("description", temp1.get("description"));
                    list.get(j).put("icon_url",temp1.get("icon_url"));
                    list.get(j).put("score",temp1.get("score"));
                    list.get(j).put("star", temp1.get("star"));
                    list.get(j).put("icon", temp1.get("icon"));
                    list.get(j).put("votes", temp1.get("votes"));
                }
            }
        }
    }


    public void setZero(final int movieId) {

       movie_data_id = movieId;
        new Thread(setVoteZero_Task).start();
    }


    Runnable OldShowtime_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/oldshowtime";
            String result= "";
            String TAG= "getOldShowTime";
            int movieID= movieId;

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


                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);

                    HashMap<String, Object> item1 = new HashMap<String, Object>();


                    item1.put("id", temp.get("id"));
                    item1.put("time",temp.getString("oldTime"));
                    item1.put("price",temp.get("oldprice").toString());
                    showTimeList.add(item1);

                }

            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }

        }
    };

    Runnable setVoteZero_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/votemovie";
            int movie_id= movie_data_id;  //这里指定要票数清零的电影id
            String result= "";
            try{
                String url = baseURL + "/setVoteZero/"+ movie_id;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", result);
            msg.setData(data);
            VoteMovie_handler.sendMessage(msg);
        }
    };
    Handler VoteMovie_handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try{
                result= new JSONObject(val).getString("setVoteZero");
                if (result.equals("success")) {
                    isZero = true;
                } else  {
                    isZero = false;
                }
            }
            catch (Exception e) {
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
                    final JSONObject temp = array.getJSONObject(i);
                    final int ii = i;
                    final HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("id", temp.get("id").toString());
                    item.put("name", temp.getString("name"));
                    item.put("type", temp.getString("type"));
                    item.put("description", temp.getString("description"));
                    item.put("icon_url",temp.getString("img"));
                    item.put("score", temp.get("score").toString());
                    item.put("star", temp.get("star").toString());
                    showMovieList.add(item);
                    new Thread(new Runnable() { //返回所有电影的信息
                        @Override
                        public void run() {

                            try {

                                Bitmap bitmap = getBitmap(temp.getString("img"));

                                showMovieList.get(ii).put("icon", bitmap);

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

            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
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
                    final JSONObject temp = array.getJSONObject(i);
                    final int ii = i;
                    final HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("id", temp.get("id").toString());
                    item.put("name", temp.getString("name"));
                    item.put("type", temp.getString("type"));
                    item.put("description", temp.getString("description"));
                    item.put("icon_url",temp.getString("img"));
                    item.put("score", temp.get("score").toString());
                    item.put("star", temp.get("star").toString());
                    item.put("votes", temp.get("votes").toString());
                    OldMovieList.add(item);
                    new Thread(new Runnable() { //返回所有电影的信息
                        @Override
                        public void run() {

                            try {

                                Bitmap bitmap = getBitmap(temp.getString("img"));

                                OldMovieList.get(ii).put("icon", bitmap);


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
//                    result= result + temp.get("id").toString()+" "+ temp.getString("name") + " "+ temp.getString("type")+" "
//                    + temp.getString("description")+ " "+temp.getString("img")+ " "
//                    + temp.get("score").toString()+" "+temp.getString("star")+" 票数:"+temp.get("votes").toString()+"\n";
//        }

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

            return (Integer.parseInt(item2.get("votes").toString()) - Integer.parseInt(item1.get("votes").toString()));
        }
    }


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
}