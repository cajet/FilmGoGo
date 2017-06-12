package com.example.administrator.filmgogoAdmin;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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
 * Created by Pipin on 2017/6/6.
 */
public class ShowFragment extends BaseFragment {


    private MySimpleAdapter adapter;
    private android.os.Handler handler;
    private Data app;
    private ListView movieList;
    private TextView load;
    private String votes = "";
    private int p;


    public static ShowFragment newInstance(String from){
        ShowFragment fragment = new ShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.on_show_list_fragment,null);

        app = (Data) getActivity().getApplication();

        app.setShowMovieList();

        movieList = (ListView) view.findViewById(R.id.show_movie_list);
        load = (TextView)view.findViewById(R.id.tv_empty);


        adapter = new MySimpleAdapter(getActivity(), app.getShowMovieList(), R.layout.movie_on_show_item,
                new String[]{"icon", "name"}, new int[]{R.id.movie_img
                , R.id.movie_name});

        movieList.setAdapter(adapter);

        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                //判断是否为我们要处理的对象
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView iv = (ImageView) view;

                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }else
                    return false;
            }
        });
        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);




        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {

    }
    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getShowMovieList().size() == 0) {
                movieList.setVisibility(View.INVISIBLE);
                load.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getShowMovieList().size(); )
                if (app.getShowMovieList().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getShowMovieList().size()) {
                movieList.setVisibility(View.INVISIBLE);
                load.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
            }
            else {
                adapter.notifyDataSetChanged();
                movieList.setVisibility(View.VISIBLE);
                load.setVisibility(View.INVISIBLE);
            }
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
            final Button showButton=(Button) v.findViewById(R.id.bt_add_show_time);
            Button delete = (Button)v.findViewById(R.id.bt_delete);



            showButton.setTag(position);
            delete.setTag(position);

            showButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                        Intent intent = new Intent(getActivity(), ShowTimeActivity.class);


                        intent.putExtra("position", position);
                        startActivity(intent);


                }
            });
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    p = position;
                    new Thread(Delete_old_movie_Task).start();
                }
            });

            return v;
        }
    }

    Runnable Delete_old_movie_Task = new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/deleteAlloldmovie";
            String TAG = "DeleteAllMovie";
            String retSrc = "null";
            try {
                HttpPost request = new HttpPost(baseURL);
                JSONObject param = new JSONObject();
                param.put("name", app.getShowMovieList().get(p).get("name"));
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
            delete_all_handler.sendMessage(msg);
        }
    };

    Handler delete_all_handler = new Handler() {
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
                    Toast.makeText(getActivity(), "下架电影成功", Toast.LENGTH_SHORT).show();
                    app.getShowMovieList().remove(p);
                    adapter.notifyDataSetChanged();
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

}
