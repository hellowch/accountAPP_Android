package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.Adapter.inaccountAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity {

    public List<Map<String,Object>> list;
    public RecyclerView recyclerview;
    public Map<String,Object> map;
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        list = new ArrayList<Map<String,Object>>();
        recyclerview = (RecyclerView) this.findViewById(R.id.recy);

        imageView = (ImageView)findViewById(R.id.imageViewId);

        Intent intent = getIntent();
        String avatar = intent.getStringExtra("avatar");
        final String token = intent.getStringExtra("token");
        Glide.with(MainActivity2.this).load(avatar).into(imageView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.123.188:8080/inaccount/inaccountAll")   //本电脑的ip地址
                            .post(RequestBody.create(MediaType.parse("application/json"),json))   //创建http客户端
                            .header("token",token)
                            .build();  //创造http请求
                    Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                    String responseData = response.body().string(); //获取返回回来的json结果

                    JSONArray jsonArray = new JSONArray(responseData);  //转换为json数组格式
                    for (int i = 0;i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map = new HashMap<String,Object>();
                        map.put("id",jsonObject.getString("id"));
                        map.put("money",jsonObject.getString("money"));
                        map.put("time",jsonObject.getString("time"));
                        map.put("type",jsonObject.getString("type"));
                        map.put("handler",jsonObject.getString("handler"));
                        map.put("user_id",jsonObject.getString("user_id"));
                        list.add(map);
                    }

                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity2.this,"查询成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity2.this,"网络失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();




    }

    public Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //添加分割线
                    recyclerview.addItemDecoration(new DividerItemDecoration(
                            MainActivity2.this, DividerItemDecoration.VERTICAL));
                    inaccountAdapter recy = new inaccountAdapter(list, MainActivity2.this);
                    //设置布局显示格式
                    recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                    recyclerview.setAdapter(recy);
                    break;

            }
        }
    };



}
