package com.example.pipin.Filmgogo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.pipin.Filmgogo.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by cajet on 2017/4/29.
 */

public class Register_Activity extends Activity{

    private Button register_btn;
    private ImageView back;
    private EditText  password, confirm, email, phone;
    private AutoCompleteTextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        init();
        setListener();
    }

    private void init() {
        register_btn= (Button) findViewById(R.id.finish_sign_up_button);
        user_name= (AutoCompleteTextView) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.sign_up_password);
        confirm= (EditText) findViewById(R.id.confirm_pass);
        email= (EditText) findViewById(R.id.email);
        phone= (EditText) findViewById(R.id.phone);
        back = (ImageView) findViewById(R.id.id_register_back);
    }

    private void setListener() {
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void attemptSignUp() {
        user_name.setError(null);
        password.setError(null);
        confirm.setError(null);
        email.setError(null);
        phone.setError(null);
        //TODO: 插入各种输入判断和对应处理的代码

        new Thread(networkTask).start();
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/register";
            String TAG = "Register";
            String retSrc = "null";
            try {
                HttpPost request = new HttpPost(baseURL);
                // 先封装一个 JSON 对象
                JSONObject param = new JSONObject();
                param.put("name", user_name.getText().toString());
                param.put("password", password.getText().toString());
                param.put("email", email.getText().toString());
                param.put("phone", phone.getText().toString());
                // 绑定到请求 Entry
                StringEntity se = new StringEntity(param.toString(),"UTF-8");
                Log.i(TAG, se.toString());
                request.setEntity(se);
                // 发送请求
                HttpResponse httpResponse = new DefaultHttpClient().execute(request);
                // 得到应答的字符串，这也是一个 JSON 格式保存的数据
                retSrc = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                Log.i(TAG, retSrc);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            // 在这里进行 http request.网络请求相关操作
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", retSrc);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            //Log.i("mylog", "请求结果为-->" + val);
            String TAG = "json";
            try{
                JSONObject result = new JSONObject(val);
                boolean ok = result.getBoolean("success");
                if (ok) {
                    finish();
                    Intent intent = new Intent(Register_Activity.this, SignInActivity.class);
                    startActivity(intent);
                } else {
                    if (result.getBoolean("nameconflict")) {
                        user_name.setError(getString(R.string.error_invalid_username));
                        user_name.requestFocus();
                    } else {
                        password.setError(getString(R.string.error_invalid_password));
                    }
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

}
