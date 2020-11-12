package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.Adapter.inaccountAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InActivity extends AppCompatActivity {

    public Map<String,Object> map;
    public List<Map<String,Object>> list;
    public RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        list = new ArrayList<Map<String,Object>>();
        recyclerview = (RecyclerView) this.findViewById(R.id.recy);

        Intent intent = getIntent();
        final String avatar = intent.getStringExtra("avatar");
        final String token = intent.getStringExtra("token");

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
                        map.put("mark",jsonObject.getString("mark"));
                        map.put("user_id",jsonObject.getString("user_id"));
                        list.add(map);
                    }
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

        final ImageView new_account = findViewById(R.id.new_account);
        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upaccount = new Intent("AddInformation");
                upaccount.putExtra("token",token);
                upaccount.putExtra("avatar",avatar);
                startActivity(upaccount);
            }
        });
    }

    public Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //添加分割线
                    recyclerview.addItemDecoration(new DividerItemDecoration(
                            InActivity.this, DividerItemDecoration.VERTICAL));
                    inaccountAdapter recy = new inaccountAdapter(list, InActivity.this);
                    //设置布局显示格式
                    recyclerview.setLayoutManager(new LinearLayoutManager(InActivity.this));
                    recyclerview.setAdapter(recy);
                    break;
            }
        }
    };



}
