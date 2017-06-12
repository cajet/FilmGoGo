package com.example.pipin.Filmgogo.activity;

/**
 * Created by ypy on 2017/6/6.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pipin.Filmgogo.R;
import com.example.pipin.Filmgogo.util.RequestData;
import com.example.pipin.Filmgogo.view.SeatTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectSeat extends AppCompatActivity {
    public SeatTable seatTableView;
    private ImageView back;
    private Button ensureButton;
    TextView cinemaName, movieName, day, time;

    private android.os.Handler handler, handler1;

    private List<HashMap<String, Object>> seatList;

    public SharedPreferences accountState;

    private RequestData app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_seat);

        app = (RequestData) getApplication();

        back = (ImageView)findViewById(R.id.id_show_time_back);
        ensureButton= (Button)findViewById(R.id.id_select_seat_ensure);
        cinemaName = (TextView) findViewById(R.id.id_cinema_name);
        movieName = (TextView) findViewById(R.id.movie_name);
        day = (TextView) findViewById(R.id.movie_day);
        time = (TextView) findViewById(R.id.movie_time);

        Intent intent = getIntent();
        cinemaName.setText(intent.getStringExtra("cinemaName"));
        movieName.setText(intent.getStringExtra("movieName"));
        day.setText(intent.getStringExtra("day"));
        time.setText(intent.getStringExtra("time"));

        seatTableView = (SeatTable) findViewById(R.id.seatView);
        seatTableView.setScreenName("8号厅荧幕");//设置屏幕名称
        seatTableView.setMaxSelected(4);//设置最多选中

        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);
        seatList = new ArrayList<HashMap<String, Object>>();

        handler = new android.os.Handler();

        seatTableView.setData(10,10);

        handler.postDelayed(selectButtonRunnable, 150);

        if (accountState.getInt("nowOrOld", 1) == 1) {
            app.setListItemMovieSeat();
        } else {
            app.setListItemOldMovieSeat();
        }


        handler1 = new android.os.Handler();
        handler1.postDelayed(selectButtonRunnable1, 150);

        setOnClick();

    }

    private void setOnClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ensureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountState.getInt("state", 0) == 0) {
                    Toast.makeText(SelectSeat.this, "请先登录，即将跳转登录页面", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectSeat.this, SignInActivity.class);
                    startActivity(intent);
                } else {
                    int[] seatId = {-1,-1,-1,-1};
                    int z = 0;
                    ArrayList<String> selectList = seatTableView.getSelectedSeat();
                    if (accountState.getInt("nowOrOld", 1) == 1) {
                        for (int i = 0; i < app.getListItemMovieSeat().size(); i++) {
                            String temp = (Integer.parseInt(app.getListItemMovieSeat().get(i).get("row").toString())-1) + "," +
                                    (Integer.parseInt(app.getListItemMovieSeat().get(i).get("column").toString())-1);
                            for (int j = 0; j < selectList.size(); j++) {
                                if (selectList.get(j).equals(temp)) {
                                    SharedPreferences.Editor editor = accountState.edit();
                                    editor.putInt("seatId", Integer.parseInt(app.getListItemMovieSeat().get(i).get("id").toString()));
                                    editor.commit();
                                    seatId[z] = Integer.parseInt(app.getListItemMovieSeat().get(i).get("id").toString());
                                    z++;
                                    app.addReservation();


                                }
                            }
                        }
                    }else {
                        for (int i = 0; i < app.getListItemOldMovieSeat().size(); i++) {
                            String temp = (Integer.parseInt(app.getListItemOldMovieSeat().get(i).get("row").toString())-1) + "," +
                                    (Integer.parseInt(app.getListItemOldMovieSeat().get(i).get("column").toString())-1);
                            for (int j = 0; j < selectList.size(); j++) {
                                if (selectList.get(j).equals(temp)) {
                                    SharedPreferences.Editor editor = accountState.edit();
                                    editor.putInt("seatId", Integer.parseInt(app.getListItemOldMovieSeat().get(i).get("id").toString()));
                                    editor.commit();
                                    seatId[z] = Integer.parseInt(app.getListItemOldMovieSeat().get(i).get("id").toString());
                                    z++;
                                    app.addOldMovieReservation();
                                }
                            }
                        }
                    }
                    Toast.makeText(SelectSeat.this, "生成订单成功", Toast.LENGTH_SHORT).show();
//                    finish();
                    String[] orderInfo = new String[4];
                    orderInfo[0] = movieName.getText().toString();
                    orderInfo[1] = day.getText().toString();
                    orderInfo[2] = time.getText().toString();
                    orderInfo[3] = cinemaName.getText().toString();
                    Intent intent = new Intent(SelectSeat.this, ConfirmOrderActivity.class);
                    intent.putExtra("seatId", seatId);
                    intent.putExtra("orderInfo", orderInfo);
                    startActivity(intent);
                }


            }
        });
    }

    private Runnable selectButtonRunnable1 = new Runnable() {
        @Override
        public void run() {


            if (accountState.getInt("nowOrOld", 1) == 1) {
                if (app.getListItemMovieSeat().size() == 0) {

                    handler.postDelayed(this, 150);
                    return;
                }


            } else {
                if (app.getListItemOldMovieSeat().size() == 0) {

                    handler.postDelayed(this, 150);
                    return;
                }
            }
            seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

                @Override
                public boolean isValidSeat(int row, int column) {

                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    if (accountState.getInt("nowOrOld", 1) == 1) {
                        for (int i = 0; i< app.getListItemMovieSeat().size(); i++) {
                            if (app.getListItemMovieSeat().get(i).get("state").equals("reserved") &&
                                    Integer.parseInt(app.getListItemMovieSeat().get(i).get("row").toString()) == row+1
                                    && Integer.parseInt(app.getListItemMovieSeat().get(i).get("column").toString()) == column+1) {
                                return true;
                            }
                        }
                    } else {
                        for (int i = 0; i< app.getListItemOldMovieSeat().size(); i++) {
                            if (app.getListItemOldMovieSeat().get(i).get("state").equals("reserved") &&
                                    Integer.parseInt(app.getListItemOldMovieSeat().get(i).get("row").toString()) == row+1
                                    && Integer.parseInt(app.getListItemOldMovieSeat().get(i).get("column").toString()) == column+1) {
                                return true;
                            }
                        }
                    }

                    return false;
                }

                @Override
                public void checked(int row, int column) {

                }

                @Override
                public void unCheck(int row, int column) {

                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return null;
                }

            });

        }
    };

    private Runnable selectButtonRunnable = new Runnable() {
        @Override
        public void run() {

            if (seatTableView.getSelectedSeat().isEmpty()) {
                ensureButton.setVisibility(View.INVISIBLE);


            } else {
                ensureButton.setVisibility(View.VISIBLE);
            }

            handler.postDelayed(this, 150);
        }
    };
}

