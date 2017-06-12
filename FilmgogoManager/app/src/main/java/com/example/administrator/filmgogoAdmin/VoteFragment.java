package com.example.administrator.filmgogoAdmin;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by Pipin on 2017/6/6.
 */
public class VoteFragment extends BaseFragment {
    private List<HashMap<String, Object>> listItem;
    private MySimpleAdapter adapter;
    private android.os.Handler handler;
    private Data app;
    private ListView movieList;
    private TextView load;
    private Button add,sort;
    private int p = 0;
    private String votes = "";

    public static VoteFragment newInstance(String from){
        VoteFragment fragment = new VoteFragment();
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
        View view;
        view = inflater.inflate(R.layout.old_movie_list_fragment,null);

        app = (Data) getActivity().getApplication();
        app.setOldMovieList();
        app.setShowMovieList();
        listItem = new ArrayList<HashMap<String, Object>>();

        movieList = (ListView) view.findViewById(R.id.vote_movie_list);
        load = (TextView)view.findViewById(R.id.tv_empty);
        add = (Button)view.findViewById(R.id.id_add_vote) ;
        sort = (Button)view.findViewById(R.id.id_sort);

        adapter = new MySimpleAdapter(getActivity(), app.getOldMovieList(), R.layout.movie_vote_item,
                new String[]{"icon", "name", "votes", "showState"}, new int[]{R.id.movie_img
                , R.id.movie_name, R.id.movie_score, R.id.bt_show});

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


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Manager_add_votemovies.class);
                startActivity(intent);
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.sort(app.getOldMovieList());
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
            if (app.getOldMovieList().size() == 0) {
                movieList.setVisibility(View.INVISIBLE);
                load.setVisibility(View.VISIBLE);
                handler.postDelayed(this, 150);
                return;
            }
            int i = 0;
            for (; i < app.getOldMovieList().size(); )
                if (app.getOldMovieList().get(i).get("icon") instanceof Bitmap) i++;
                else break;
            if (i < app.getOldMovieList().size()) {
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


    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {

    }
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
            final Button showButton=(Button) v.findViewById(R.id.bt_show);
            Button delete = (Button)v.findViewById(R.id.bt_delete);
            Button zero = (Button)v.findViewById(R.id.bt_zero);


            if (app.getOldMovieList().get(position).get("showState").equals("已上映")){
                showButton.setTextColor(getResources().getColor(R.color.grey));
            } else {
                showButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            }


            showButton.setTag(position);
            delete.setTag(position);
            zero.setTag(position);
            showButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stu
                    if(showButton.getText().equals("上映")){
                        Intent intent = new Intent(getActivity(), AddShowMovie.class);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
//                    Intent intent = new Intent(ShowTimeSelect.this, SelectSeat.class);
//                    startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Runnable Delete_Votemovie_Task= new Runnable() {
                        @Override
                        public void run() {
                            String baseURL = "http://172.18.71.17:8080/FilmGoGo/deletevotemovie";
                            int votemovie_id= Integer.parseInt(app.getOldMovieList().get(position).get("id").toString()); //指定要删除的投票电影id
                            try{
                                String url = baseURL + '/'+ votemovie_id;
                                HttpGet httpGet = new HttpGet(url);
                                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    new Thread(Delete_Votemovie_Task).start();
                    app.getOldMovieList().remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            zero.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    app.setZero(Integer.parseInt(app.getOldMovieList().get(position).get("id").toString()));
                    app.getOldMovieList().get(position).put("votes", 0);
                    adapter.notifyDataSetChanged();
                }
            });
            return v;
        }
    }



}
