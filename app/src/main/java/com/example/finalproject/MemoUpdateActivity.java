package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.finalproject.pojo.Urls;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 添加修改便签模块
 */
public class MemoUpdateActivity extends AppCompatActivity {

    public String token,avatar,purpose;
    public EditText memoText;
    public Button update;
    public String memo,memoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        avatar = intent.getStringExtra("avatar");
        purpose = intent.getStringExtra("purpose");
        memoId = intent.getStringExtra("id");
        memo = intent.getStringExtra("memo");

        memoText = (EditText) findViewById(R.id.edit_text);
        update = (Button) findViewById(R.id.update);

        //判断上传接口是新建还是更改
        if (purpose.equals("new")){
            memoNew();
        }else if (purpose.equals("update")){
            //将原本的内容写到编辑页面
            memoText.setText(memo);
            memoUpdate();
        }

    }

    public void memoNew(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启子线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = null;

                            //发送内容
                            String json = "memo=" + memoText.getText().toString();

                            //配置接口，打包发送内容
                            request = new Request.Builder()
                                    .url(Urls.getUrl() + "memo/memoNew")
                                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))
                                    .header("token",token)
                                    .build();

                            //执行发送指令
                            Response response = client.newCall(request).execute();

                            //获取返回值
                            String responseDate = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseDate);
                            Log.d("msg",""+jsonObject.getString("msg"));

                            if (jsonObject.getString("msg").equals("添加成功")){
                                Intent intent = new Intent(MemoUpdateActivity.this,MemoActivity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("avatar",avatar);
                                startActivity(intent);
                            }else {
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        Toast.makeText(MemoUpdateActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MemoUpdateActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    public void memoUpdate(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = null;

                            //发送内容
                            String json = "id=" + memoId + "&" +
                                    "memo=" + memoText.getText().toString();

                            //配置接口，打包发送内容
                            request = new Request.Builder()
                                    .url(Urls.getUrl() + "memo/memoUpdate")
                                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))
                                    .header("token",token)
                                    .build();

                            //执行发送指令
                            Response response = client.newCall(request).execute();

                            //获取返回值
                            String responseDate = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseDate);
                            Log.d("msg",""+jsonObject.getString("msg"));

                            if (jsonObject.getString("msg").equals("添加成功")){
                                Intent intent = new Intent(MemoUpdateActivity.this,MemoActivity.class);
                                intent.putExtra("token",token);
                                intent.putExtra("avatar",avatar);
                                startActivity(intent);
                            }else {
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        Toast.makeText(MemoUpdateActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MemoUpdateActivity.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }


}