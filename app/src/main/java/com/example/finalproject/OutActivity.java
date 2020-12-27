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
import com.example.finalproject.Adapter.outaccountAdapter;
import com.example.finalproject.pojo.Urls;

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

/**
 * 支出情况列表界面模块
 */
public class OutActivity extends AppCompatActivity {

    public String token,avatar;
    public Map<String,Object> map;
    public List<Map<String,Object>> list;
    public RecyclerView recyclerview;
    public ImageView new_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        list = new ArrayList<Map<String,Object>>();
        recyclerview = (RecyclerView) this.findViewById(R.id.recy);

        Intent intent = getIntent();
        avatar = intent.getStringExtra("avatar");
        token = intent.getStringExtra("token");

        outaccountList();
        
        new_account = findViewById(R.id.new_account);
        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upaccount = new Intent("AddInformation");
                upaccount.putExtra("token",token);
                upaccount.putExtra("avatar",avatar);
                upaccount.putExtra("purpose","new");
                startActivity(upaccount);
            }
        });
    }


    public void outaccountList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Urls.getUrl() +"outaccount/outaccountAll")   //本电脑的ip地址
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
                        map.put("handler",jsonObject.getString("address"));
                        map.put("mark",jsonObject.getString("mark"));
                        map.put("user_id",jsonObject.getString("user_id"));
                        map.put("token",token);
                        map.put("avatar",avatar);
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
                            Toast.makeText(OutActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
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
                            OutActivity.this, DividerItemDecoration.VERTICAL));
                    outaccountAdapter recy = new outaccountAdapter(list, OutActivity.this);
                    //设置布局显示格式
                    recyclerview.setLayoutManager(new LinearLayoutManager(OutActivity.this));
                    recyclerview.setAdapter(recy);
                    break;
            }
        }
    };

}