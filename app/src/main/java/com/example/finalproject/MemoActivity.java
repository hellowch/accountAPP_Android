package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.Adapter.inaccountAdapter;
import com.example.finalproject.Adapter.memoAdapter;
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
 * 便签模块
 */
public class MemoActivity extends AppCompatActivity {

    public String token,avatar;
    public Map<String,Object> map;
    public List<Map<String,Object>> list;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        avatar = intent.getStringExtra("avatar");

        list = new ArrayList<>();
        recyclerView = this.findViewById(R.id.recy);
        memoList();
    }

    public void memoList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Urls.getUrl() + "memo/memoAll")
                            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))
                            .header("token",token)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseData); //将获得的数据转换为json数组格式
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map = new HashMap<>();
                        map.put("id",jsonObject.getString("id"));
                        map.put("memo",jsonObject.getString("memo"));
                        map.put("user_id",jsonObject.getString("user_id"));
                        map.put("token",token);
                        map.put("avatar",avatar);
                        list.add(map);
                    }
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MemoActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    //添加分割线
                    recyclerView.addItemDecoration(new DividerItemDecoration(
                            MemoActivity.this, DividerItemDecoration.VERTICAL));
                    memoAdapter recy = new memoAdapter(list, MemoActivity.this);
                    //设置布局显示格式
                    recyclerView.setLayoutManager(new LinearLayoutManager(MemoActivity.this));
                    recyclerView.setAdapter(recy);
                    break;
            }
        }
    };

    //返回到主页面
    public void goBackClick(View v){
        Intent intent = new Intent(MemoActivity.this,TabWidget.class);
        intent.putExtra("token",token);
        intent.putExtra("avatar",avatar);
        startActivity(intent);
    }

    //进入编辑页面
    public void newClick(View v){
        Intent intent = new Intent(MemoActivity.this,MemoUpdateActivity.class);
        intent.putExtra("token",token);
        intent.putExtra("avatar",avatar);
        intent.putExtra("purpose","new");
        startActivity(intent);
    }
}