package com.example.pipin.Filmgogo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pipin.Filmgogo.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SignInActivity extends Activity {

    private Button login_btn;
    private TextView register_btn;
    private ImageView back;
    private EditText muserName;
    private EditText mPassword;
    private View mProgressView;
    private View mLoginFormView;

    public SharedPreferences accountState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        init();
        setListener();
    }

    private void init() {
        login_btn= (Button) findViewById(R.id.sign_in_button);
        register_btn= (TextView) findViewById(R.id.sign_up_button);
        muserName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        back = (ImageView) findViewById(R.id.id_sign_in_back);

        accountState = getSharedPreferences("account", Context.MODE_PRIVATE);

    }

    private void setListener() {
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignInActivity.this, Register_Activity.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void attemptSignIn() {
        muserName.setError(null);
        mPassword.setError(null);
        new Thread(networkTask).start();

    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            String baseURL = "http://172.18.71.17:8080/FilmGoGo/login";
            String TAG = "Login";
            String retSrc = "null";
            try {
                HttpPost request = new HttpPost(baseURL);
                // 先封装一个 JSON 对象
                JSONObject param = new JSONObject();
                param.put("name", muserName.getText().toString());
                param.put("password", mPassword.getText().toString());
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
                boolean exist= result.getBoolean("exist");
                boolean success = result.getBoolean("loginAble");
                if (success) {
                    SharedPreferences.Editor editor = accountState.edit();
                    editor.putString("userName", muserName.getText().toString());
                    editor.putString("password", mPassword.getText().toString());
                    editor.putInt("state", 1);
                    editor.commit();
                    new Thread(GetIdByName_Task).start();
                    Intent intent = new Intent("fresh");
                    LocalBroadcastManager.getInstance(SignInActivity.this).sendBroadcast(intent);
                    finish();
                } else {
                    if (exist) {
                        mPassword.setError(getString(R.string.error_invalid_password));
                    } else {
                        muserName.setError(getString(R.string.error_username_notexist));
                    }
                }
            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

    Runnable GetIdByName_Task= new Runnable() {
        @Override
        public void run() {
            String baseURL= "http://172.18.71.17:8080/FilmGoGo/customer";
            String result= "";
            String name= accountState.getString("userName", "");  //用一个栈放用户名，登录/注册用户成功后把用户名进栈；退出当前用户，便出栈
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
                    int id;
                    id = Integer.parseInt(temp.get("id").toString());
                    SharedPreferences.Editor editor = accountState.edit();
                    editor.putInt("id", id);
                    editor.commit();
                }

            }
            catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };

}
