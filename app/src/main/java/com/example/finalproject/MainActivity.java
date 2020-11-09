package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        EditText username = (EditText)findViewById(R.id.userName);
        EditText password = (EditText)findViewById(R.id.passWord);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {   //创建ui，主线程中运行不安全
                    @Override
                    public void run() {
                        try {
                            String json = "{\n" +
                                    "    \"username\":\"wch\",\n" +
                                    "    \"password\":123456\n" +
                                    "}";
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.123.188:8080/user/login")   //本电脑的ip地址
                                    .post(RequestBody.create(MediaType.parse("application/json"),json))   //创建http客户端
                                    .build();  //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络连接成功",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    public void click_01(View v){
        Intent intent = new Intent();
        intent.setAction("activity2");
        startActivity(intent);
    }
}