package com.example.pipin.Filmgogo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.util.RequestData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ypy on 2017/6/6.
 */
public class AccountReservation extends AppCompatActivity {

    ImageView back;
    Button deleteReservation;

    ListView movieList;

    public SharedPreferences accountState;

    private List<HashMap<String, Object>> listItemMovie;

    private MySimpleAdapter adapterMovie;

    private int seatId;
    private RequestData app;
    private android.os.Handler handler;
    private int tag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);

        app = (RequestData)getApplication();
        app.setListReservation();

        init();
        setListener();
        Intent intent = getIntent();
        tag = intent.getIntExtra("tag", 0);

        if (tag == 1) {
            adapterMovie = new MySimpleAdapter(this, app.getListAllReservation(), R.layout.item_reservation,
                    new String[]{"movieName", "cinemaName", "time", "ticketPrice"}, new int[]{R.id.id_reservation_movie_name
                    , R.id.id_reservation_location, R.id.id_reservation_time, R.id.id_reservation_price });


        } else if (tag == 2) {
            adapterMovie = new MySimpleAdapter(this, app.getListNoPayReservation(), R.layout.item_reservation,
                    new String[]{"movieName", "cinemaName", "time", "ticketPrice"}, new int[]{R.id.id_reservation_movie_name
                    , R.id.id_reservation_location, R.id.id_reservation_time, R.id.id_reservation_price });


        } else if (tag == 3) {
            adapterMovie = new MySimpleAdapter(this, app.getListPayReservation(), R.layout.item_reservation,
                    new String[]{"movieName", "cinemaName", "time", "ticketPrice"}, new int[]{R.id.id_reservation_movie_name
                    , R.id.id_reservation_location, R.id.id_reservation_time, R.id.id_reservation_price });
        }

        movieList.setAdapter(adapterMovie);

        handler = new android.os.Handler();
        handler.postDelayed(selectButtonRunnable, 150);


    }

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {
                if (tag == 1) {
                    if (app.getListAllReservation().size() == 0) {
                        handler.postDelayed(this, 150);
                        return;
                    }
                } else if (tag == 2) {
                    if (app.getListNoPayReservation().size() == 0) {
                        handler.postDelayed(this, 150);
                        return;
                    }
                } else if (tag == 3) {
                    if (app.getListPayReservation().size() == 0) {
                        handler.postDelayed(this, 150);
                        return;
                    }
                }

            adapterMovie.notifyDataSetChanged();

        }
    };

    void init() {
        back = (ImageView)findViewById(R.id.id_my_reservation_back);
        movieList = (ListView)findViewById(R.id.id_reservation_list);


        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

    }

    void setListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private class MySimpleAdapter extends SimpleAdapter{
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
            Button btn=(Button) v.findViewById(R.id.id_reservation_delete);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (tag == 1) {
                        seatId =  Integer.parseInt(app.getListAllReservation().get(position).get("seatId").toString());
                        if (app.getListAllReservation().get(position).get("nowOrOld").equals("now")) {
                            app.setDelete_Reservation_Task(seatId);
                        } else {
                            app.setDelete_oldreservation_Task(seatId);
                        }
                        app.getListAllReservation().remove(position);
                    } else if (tag == 2) {
                        seatId =  Integer.parseInt(app.getListNoPayReservation().get(position).get("seatId").toString());
                        if (app.getListNoPayReservation().get(position).get("nowOrOld").equals("now")) {
                            app.setDelete_Reservation_Task(seatId);
                        } else {
                            app.setDelete_oldreservation_Task(seatId);
                        }
                        app.getListNoPayReservation().remove(position);
                    } else if (tag == 3) {
                        seatId =  Integer.parseInt(app.getListPayReservation().get(position).get("seatId").toString());
                        if (app.getListPayReservation().get(position).get("nowOrOld").equals("now")) {
                            app.setDelete_Reservation_Task(seatId);
                        } else {
                            app.setDelete_oldreservation_Task(seatId);
                        }
                        app.getListPayReservation().remove(position);
                    }

                    adapterMovie.notifyDataSetChanged();
                }
            });
            return v;
        }
    }

}