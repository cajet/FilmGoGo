package com.example.pipin.Filmgogo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pipin.Filmgogo.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfirmOrderActivity extends AppCompatActivity {
    private ImageView back;
    private TextView confirm, movie_name, movie_time, cinema_name;
    private String[] orderInfo;

    private SharedPreferences accountState;

    private int[] seatId;
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        init();
        setOnClick();
    }

    private void init() {
        Intent intent = getIntent();
        seatId = intent.getIntArrayExtra("seatId");
        orderInfo = intent.getStringArrayExtra("orderInfo");
        p = 0;

        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

        back = (ImageView) findViewById(R.id.iv_back);
        confirm = (TextView) findViewById(R.id.confirm_button);
        movie_name = (TextView) findViewById(R.id.movie_name);
        movie_time = (TextView) findViewById(R.id.movie_time);
        cinema_name = (TextView) findViewById(R.id.cinema_name);

        movie_name.setText(orderInfo[0]);
        movie_time.setText(orderInfo[1] + " " + orderInfo[2]);
        cinema_name.setText(orderInfo[2]);
    }

    private void setOnClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (p = 0; p < 4; p++) {
                    if (seatId[p] != -1) {
                        new Thread(Pay_Task).start();
                        Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(ConfirmOrderActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Runnable Pay_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/reservation";
            String result = "null";
            int customer_id= accountState.getInt("id", -1);
            int seat_id= seatId[p];  //这里根据订单里选中的座位来指定id
            try{
                String url = baseURL + '/'+ String.valueOf(customer_id)+"/pay/"+ String.valueOf(seat_id);
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", result);
                msg.setData(data);
                Pay_handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler Pay_handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String TAG = "json";
            String result= "";
            try {
                JSONArray array= new JSONObject(val).getJSONArray("payticket");
                //response_content.setText(val);
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject temp = array.getJSONObject(i);
                    result= result + temp.get("id").toString()+"\n";
                }
//                if (!result.isEmpty()) response_content.setText("支付订单成功");
//                else response_content.setText("支付订单失败");
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };
}
