package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText userName = (EditText)findViewById(R.id.userName);
        final EditText passWord = (EditText)findViewById(R.id.passWord);
        final Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {   //创建ui，主线程中运行不安全
                    @Override
                    public void run() {
                        try {
                            String json = "{\n" +
                                    "    \"username\":" + "\"" + userName.getText().toString() + "\"" + ",\n" +
                                    "    \"password\":" + "\"" + passWord.getText().toString() + "\"" + "\n" +
                                    "}";
                            System.out.println(json);

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.123.188:8080/user/login")   //本电脑的ip地址
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))   //创建http客户端
//                                    .header("token",token)
                                    .build();  //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                            String responseData = response.body().string(); //获取返回回来的json结果
//                            JSONArray jsonArray = new JSONArray(responseData);  //转换为json数组格式
//                            for (int i = 0;i < jsonArray.length();i++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                Log.d("detail",""+jsonObject.getString("detail"));
//                                Log.d("token",""+jsonObject.getString("token"));
//                            }
                            JSONObject jsonObject = new JSONObject(responseData);
                            String token = jsonObject.getString("token");
                            String avatar = jsonObject.getString("detail");
                            Log.d("msg",""+jsonObject.getString("msg"));

                            if ( jsonObject.getString("msg").equals("登录成功")){
                                Intent intent = new Intent();
                                intent.setAction("activity2");
                                startActivity(intent);
                            }else {
                                runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this,"账号密码错误",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });


        final Button newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = "{\n" +
                                    "    \"username\":" + "\"" + userName.getText().toString() + "\"" + ",\n" +
                                    "    \"password\":" + "\"" + passWord.getText().toString() + "\"" + "\n" +
                                    "}";
                            System.out.println(json);

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.123.188:8080/user/register")   //本电脑的ip地址
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))   //创建http客户端
//                                    .header("token",token)
                                    .build();  //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                            String responseData = response.body().string(); //获取返回回来的json结果
                            JSONObject jsonObject = new JSONObject(responseData);
                            Log.d("msg",""+jsonObject.getString("msg"));
                            final String msg = jsonObject.getString("msg");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

}