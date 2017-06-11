package com.example.pipin.Filmgogo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.adapter.ShowTimeFragmentPagerAdapter;
import com.example.pipin.Filmgogo.util.FixedSpeedScroller;
import com.example.pipin.Filmgogo.util.RequestData;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ypy on 2017/6/6.
 */
public class ShowTimeSelect extends AppCompatActivity {

    private ImageView back;
    private FixedSpeedScroller mScroller;
    public TextView cinemaName, movieName, movie_score, movie_type, movie_actor, one, two, three;
    private ListView oneList;
    private ViewPager show_time_viewPager;
    private ShowTimeFragmentPagerAdapter myFragmentPagerAdapter;

    private List<HashMap<String, Object>> listItemOne;

    private MySimpleAdapter adapterOne;

    public SharedPreferences accountState;

    private int buttonClick = 1;
    private android.os.Handler handler;

    private RequestData app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time);
        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);
        app = (RequestData) getApplication();
        if (accountState.getInt("nowOrOld", 1) == 1) {

            app.setListItemMovieShowTime();
        } else {
            app.setListItemOldMovieShowTime();

        }
        init();
        setListener();


        listItemOne = new ArrayList<HashMap<String, Object>>();

        adapterOne = new MySimpleAdapter(this, listItemOne, R.layout.item_show_time,
                new String[]{"time", "price"}, new int[]{R.id.id_show_time
                , R.id.id_show_time_price});


        oneList.setAdapter(adapterOne);






        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);


    }
    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {


            if (accountState.getInt("nowOrOld", 1) == 1) {
                if (app.getListItemMovieShowTime().size() == 0) {

                    handler.postDelayed(this, 150);
                    return;
                }

                setListItem();

            } else {
                if (app.getListItemOldMovieShowTime().size() == 0) {

                    handler.postDelayed(this, 150);
                    return;
                }
                //app.setListItemOldMovieShowTime();
                setOldListItem();
            }

        }
    };


    void init() {
        back = (ImageView)findViewById(R.id.id_show_time_back);
        cinemaName = (TextView)findViewById(R.id.id_show_time_cinema_name);
        movieName = (TextView)findViewById(R.id.id_show_time_movie_name);
        movie_score = (TextView)findViewById(R.id.movie_score);
        movie_type = (TextView)findViewById(R.id.movie_type);
        movie_actor = (TextView)findViewById(R.id.movie_actor);
        one = (TextView)findViewById(R.id.id_one);
        two = (TextView)findViewById(R.id.id_two);
        three = (TextView)findViewById(R.id.id_three);
        oneList = (ListView)findViewById(R.id.id_one_list);
        show_time_viewPager= (ViewPager) findViewById(R.id.show_time_viewPager);
        myFragmentPagerAdapter = new ShowTimeFragmentPagerAdapter(getSupportFragmentManager());
        show_time_viewPager.setAdapter(myFragmentPagerAdapter);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(show_time_viewPager.getContext(),new AccelerateInterpolator());
            mField.set(show_time_viewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UnderlinePageIndicator mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(show_time_viewPager);
        mIndicator.setFades(false);
        mIndicator.setSelectedColor(0xffdc3c38);
        mIndicator.setShape(0);
        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int pos) {
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                        if (arg0 == 1) {
                            one.setTextColor(0xff666666);
                            two.setTextColor(0xff666666);
                            three.setTextColor(0xff666666);
                        } else if (arg0 == 0) {
                            setIndicatorViewSelected(show_time_viewPager.getCurrentItem());
                        }
                    }
                });
        // 初始化，第0项被选中
        setIndicatorViewSelected(0);

        one.setVisibility(View.INVISIBLE);
        two.setVisibility(View.INVISIBLE);
        three.setVisibility(View.INVISIBLE);

        oneList.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        cinemaName.setText(intent.getStringExtra("cinemaName"));
        movieName.setText(intent.getStringExtra("movieName"));
        if (accountState.getInt("nowOrOld", 1) == 1) {
            movie_score.setText(app.getListItemMovie().get(position).get("score").toString());
            movie_type.setText(app.getListItemMovie().get(position).get("type").toString());
            movie_actor.setText(app.getListItemMovie().get(position).get("star").toString());
        } else {
            movie_score.setText(app.getListItemOldMovie().get(position).get("score").toString());
            movie_type.setText(app.getListItemOldMovie().get(position).get("type").toString());
            movie_actor.setText(app.getListItemOldMovie().get(position).get("star").toString());
        }
    }

    // 在这里设置被选中时候选项卡变化的效果
    private void setIndicatorViewSelected(int pos) {
        if (pos == 0) {
            // Android Holo 样式的蓝色
            one.setTextColor(0xffdc3c38);
            two.setTextColor(0xff666666);
            three.setTextColor(0xff666666);
        } else if (pos == 1){
            one.setTextColor(0xff666666);
            two.setTextColor(0xffdc3c38);
            three.setTextColor(0xff666666);
        } else {
            one.setTextColor(0xff666666);
            two.setTextColor(0xff666666);
            three.setTextColor(0xffdc3c38);
        }
    }

    private void set(int pos) {
        show_time_viewPager.setCurrentItem(pos, true);
        mScroller.setmDuration(180);
        setIndicatorViewSelected(pos);
    }



    void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        one.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listItemMovie.clear();
//                new Thread(movieTask).start();
                buttonClick = 1;
                oneList.setVisibility(View.VISIBLE);
                set(0);
            }
        });
        two.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listItemCinema.clear();
//                new Thread(cinemaTask).start();
                buttonClick = 2;
                oneList.setVisibility(View.GONE);
                set(1);
            }
        });
        three.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listItemCinema.clear();
//                new Thread(cinemaTask).start();
                buttonClick = 3;
                oneList.setVisibility(View.GONE);
                set(2);
            }
        });
    }



    void setListItem() {
        show_time_viewPager.setVisibility(View.VISIBLE);
        oneList.setVisibility(View.GONE);
        int j = 0;
        for (int i = 0; i <app.getListItemMovieShowTime().size(); ++i) {


            HashMap<String, Object> item1 = new HashMap<String, Object>();



            if (i == 0) {
                one.setText(app.getListItemMovieShowTime().get(i).get("buttonTitle").toString());
                j = 1;
                one.setVisibility(View.VISIBLE);

            } else if (!app.getListItemMovieShowTime().get(i).get("buttonTitle").equals(app.getListItemMovieShowTime().get(i - 1).get("buttonTitle"))) {
                if (j == 1) {
                    two.setText(app.getListItemMovieShowTime().get(i).get("buttonTitle").toString());
                    j = 2;
                    two.setVisibility(View.VISIBLE);
                } else if (j == 2) {
                    three.setText(app.getListItemMovieShowTime().get(i).get("buttonTitle").toString());
                    j = 3;
                    three.setVisibility(View.VISIBLE);
                }
            }

            if (j == 1) {
                item1.put("id", app.getListItemMovieShowTime().get(i).get("id"));
                item1.put("time", app.getListItemMovieShowTime().get(i).get("time"));
                item1.put("price",  app.getListItemMovieShowTime().get(i).get("price").toString());
                listItemOne.add(item1);
                adapterOne.notifyDataSetChanged();
            }

        }

    }



   void setOldListItem() {

       for (int i = 0; i <app.getListItemOldMovieShowTime().size(); ++i) {
           HashMap<String, Object> item1 = new HashMap<String, Object>();

           show_time_viewPager.setVisibility(View.GONE);
           oneList.setVisibility(View.VISIBLE);
           item1.put("id", app.getListItemOldMovieShowTime().get(i).get("id"));
           item1.put("time",app.getListItemOldMovieShowTime().get(i).get("buttonTitle").toString()+"\n"+
                   app.getListItemOldMovieShowTime().get(i).get("time"));
           item1.put("price",app.getListItemOldMovieShowTime().get(i).get("price").toString());
           listItemOne.add(item1);
           adapterOne.notifyDataSetChanged();


       }
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
            Button btn=(Button) v.findViewById(R.id.id_buy_ticket);
            final TextView time = (TextView) v.findViewById(R.id.id_show_time);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stu

                    SharedPreferences.Editor editor = accountState.edit();
                    if (buttonClick == 1) {
                        editor.putInt("showTimeId", Integer.parseInt(listItemOne.get(position).get("id").toString()));
                    }
                    editor.commit();
                    Intent intent = new Intent(ShowTimeSelect.this, SelectSeat.class);
                    TextView cinemaName = (TextView) findViewById(R.id.id_show_time_cinema_name);
                    TextView movieName = (TextView) findViewById(R.id.id_show_time_movie_name);
                    TextView day = (TextView) findViewById(R.id.id_one);
                    intent.putExtra("cinemaName", cinemaName.getText().toString());
                    intent.putExtra("movieName", movieName.getText().toString());
                    intent.putExtra("day", day.getText().toString());
                    intent.putExtra("time", time.getText().toString());
                    startActivity(intent);
                }
            });
            return v;
        }
    }



}