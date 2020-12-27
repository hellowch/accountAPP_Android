package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.finalproject.pojo.Urls;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 账单的记账与更新账单模块
 */
public class AddInformation extends AppCompatActivity {

    public Button type_btn,update;
    public RadioGroup inorout;
    public RadioButton radioButton ;
    public EditText add_money,add_mark,add_time,add_handler;
    public String token,avatar,purpose;
    public String update_id,update_money,update_mark,update_time,update_handler,update_type,update_chart = "无信息";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_information);


        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        avatar = intent.getStringExtra("avatar");
        purpose = intent.getStringExtra("purpose");

        update_money = intent.getStringExtra("money");
        update_time = intent.getStringExtra("time");
        update_type = intent.getStringExtra("type");
        update_handler = intent.getStringExtra("handler");
        update_mark = intent.getStringExtra("mark");
        update_id = intent.getStringExtra("id");
        update_chart = intent.getStringExtra("chart");

        add_money = (EditText) findViewById((R.id.add_money));
        add_time = (EditText) findViewById((R.id.add_time));
        type_btn = (Button) this.findViewById(R.id.add_type);
        add_handler = (EditText) findViewById((R.id.add_handler));
        add_mark = (EditText) findViewById((R.id.add_mark));

        update = findViewById(R.id.update);
        inorout = (RadioGroup) findViewById(R.id.in_or_out);
        //为单选按钮设置监听
        inorout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
            }
        });

        if (purpose.equals("new")){
            add_new();
        }else if (purpose.equals("update")){
            add_money.setText(update_money);
            add_time.setText(update_time);
            type_btn.setHint(update_type);
            add_handler.setText(update_handler);
            add_mark.setText(update_mark);
            inorout.setVisibility(View.INVISIBLE);
            update();
        }

    }

    public void update(){
        final Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {   //创建ui，主线程中运行不安全
                    @Override
                    public void run() {
                        try {

                            OkHttpClient client = new OkHttpClient();
                            Request request = null;
                            if (update_chart.equals("收入信息")){
                                String json = "id=" + update_id + "&" +
                                        "money=" + add_money.getText().toString() + "&" +
                                        "time="  + add_time.getText().toString() + "&" +
                                        "type="  + type_btn.getHint().toString() + "&" +
                                        "handler=" + add_handler.getText().toString() + "&" +
                                        "mark="  + add_mark.getText().toString();
                                request = new Request.Builder()
                                        .url(Urls.getUrl() +"inaccount/inaccountUpdate")   //本电脑的ip地址
                                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                                        .header("token",token)
                                        .build();  //创造http请求

                            }else if (update_chart.equals("支出信息")){
                                String json = "id=" + update_id + "&" +
                                        "money=" + add_money.getText().toString() + "&" +
                                        "time="  + add_time.getText().toString() + "&" +
                                        "type="  + type_btn.getHint().toString() + "&" +
                                        "address=" + add_handler.getText().toString() + "&" +
                                        "mark="  + add_mark.getText().toString();
                                request = new Request.Builder()
                                        .url(Urls.getUrl() +"outaccount/outaccountUpdate")   //本电脑的ip地址
                                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                                        .header("token",token)
                                        .build();  //创造http请求
                            }


                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                            String responseData = response.body().string(); //获取返回回来的json结果

                            JSONObject jsonObject = new JSONObject(responseData);
                            Log.d("msg",""+jsonObject.getString("msg"));

                            if ( jsonObject.getString("msg").equals("修改成功")){
                                Intent intent = new Intent("TabWidget");
                                intent.putExtra("token",token);
                                intent.putExtra("avatar",avatar);
                                startActivity(intent);
                            }else {
                                runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddInformation.this,"修改失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                @Override
                                public void run() {
                                    Toast.makeText(AddInformation.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    public void add_new(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {   //创建ui，主线程中运行不安全
                    @Override
                    public void run() {
                        try {

                            OkHttpClient client = new OkHttpClient();
                            Request request = null;
                            if (radioButton.getText().equals("收入信息")){
                                String json =
                                        "money=" + add_money.getText().toString() + "&" +
                                                "time="  + add_time.getText().toString() + "&" +
                                                "type="  + type_btn.getHint().toString() + "&" +
                                                "handler=" + add_handler.getText().toString() + "&" +
                                                "mark="  + add_mark.getText().toString();

                                request = new Request.Builder()
                                        .url(Urls.getUrl() +"inaccount/inaccountNew")
                                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                                        .header("token",token)
                                        .build();  //创造http请求
                            }else if (radioButton.getText().equals("支出信息")){
                                String json =
                                        "money=" + add_money.getText().toString() + "&" +
                                                "time="  + add_time.getText().toString() + "&" +
                                                "type="  + type_btn.getHint().toString() + "&" +
                                                "address=" + add_handler.getText().toString() + "&" +
                                                "mark="  + add_mark.getText().toString();

                                request = new Request.Builder()
                                        .url(Urls.getUrl() +"outaccount/outaccountNew")   //本电脑的ip地址
                                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                                        .header("token",token)
                                        .build();  //创造http请求
                            }


                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中

                            String responseData = response.body().string(); //获取返回回来的json结果

                            JSONObject jsonObject = new JSONObject(responseData);
                            Log.d("msg",""+jsonObject.getString("msg"));

                            if ( jsonObject.getString("msg").equals("添加成功")){
                                Intent intent = new Intent("TabWidget");
                                intent.putExtra("token",token);
                                intent.putExtra("avatar",avatar);
                                startActivity(intent);
                            }else {
                                runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddInformation.this,"添加失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {  //只有主线程能改变ui，这是在子线程中改变ui的方式
                                @Override
                                public void run() {
                                    Toast.makeText(AddInformation.this,"网络失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    public void popupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        //添加点击事件
        if (update_chart == null){
            if ("收入信息".equals(radioButton.getText()) ){
                popupMenu.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()){
                        case R.id.investment:
                            type_btn.setHint("投资理财");
                            return true;
                        case R.id.wage:
                            type_btn.setHint("工资");
                            return true;
                        case R.id.part_time:
                            type_btn.setHint("兼职");
                            return true;
                        case R.id.transfer:
                            type_btn.setHint("转账");
                            return true;
                        case R.id.other:
                            type_btn.setHint("其他");
                            return true;
                        default:
                            return false;
                    }
                });
                inflater.inflate(R.menu.popupmenu1, popupMenu.getMenu());
            }else if ( "支出信息".equals(radioButton.getText()) ){
                popupMenu.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()){
                        case R.id.investment:
                            type_btn.setHint("投资理财");
                            return true;
                        case R.id.clothing:
                            type_btn.setHint("服装美容");
                            return true;
                        case R.id.life:
                            type_btn.setHint("生活日用");
                            return true;
                        case R.id.food:
                            type_btn.setHint("餐饮美食");
                            return true;
                        case R.id.other:
                            type_btn.setHint("其他");
                            return true;
                        default:
                            return false;
                    }
                });
                inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());
            }

        }else {
            if ("收入信息".equals(update_chart) ){
                popupMenu.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()){
                        case R.id.investment:
                            type_btn.setHint("投资理财");
                            return true;
                        case R.id.wage:
                            type_btn.setHint("工资");
                            return true;
                        case R.id.part_time:
                            type_btn.setHint("兼职");
                            return true;
                        case R.id.transfer:
                            type_btn.setHint("转账");
                            return true;
                        case R.id.other:
                            type_btn.setHint("其他");
                            return true;
                        default:
                            return false;
                    }
                });
                inflater.inflate(R.menu.popupmenu1, popupMenu.getMenu());
            }else if ("支出信息".equals(update_chart)){
                popupMenu.setOnMenuItemClickListener((item) -> {
                    switch (item.getItemId()){
                        case R.id.investment:
                            type_btn.setHint("投资理财");
                            return true;
                        case R.id.clothing:
                            type_btn.setHint("服装美容");
                            return true;
                        case R.id.life:
                            type_btn.setHint("生活日用");
                            return true;
                        case R.id.food:
                            type_btn.setHint("餐饮美食");
                            return true;
                        case R.id.other:
                            type_btn.setHint("其他");
                            return true;
                        default:
                            return false;
                    }
                });
                inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());
            }
        }
        

        popupMenu.show();
    }


}