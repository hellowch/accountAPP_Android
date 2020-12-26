package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.pojo.Urls;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册模块
 */
public class Register extends AppCompatActivity {

    public EditText passWordRegister,avatarRegister,userNameRegister;
    public Button trueRegister,falseRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        passWordRegister = findViewById(R.id.passWordRegister);
        avatarRegister = findViewById(R.id.avatarRegister);
        userNameRegister = findViewById(R.id.userNameRegister);
        trueRegister = findViewById(R.id.trueRegister);
        falseRegister = findViewById(R.id.falseRegister);

        trueRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = "{\n" +
                                    "    \"username\":" + "\"" + userNameRegister.getText().toString() + "\"" + ",\n" +
                                    "    \"password\":" + "\"" + passWordRegister.getText().toString() + "\"" + ",\n" +
                                    "    \"avatar\":" + "\"" + avatarRegister.getText().toString() + "\"" + "\n" +
                                    "}";
                            System.out.println(json);

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(Urls.getUrl() +"user/register")   //本电脑的ip地址
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
                                    Toast.makeText(Register.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        falseRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passWordRegister.setText(null);
                avatarRegister.setText(null);
                userNameRegister.setText(null);
            }
        });
    }
}