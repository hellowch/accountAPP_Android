package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddInformation extends AppCompatActivity {

    public Button type_btn;
    public RadioGroup inorout;
    public RadioButton radioButton;
    public String type = "";
    public EditText add_money,add_mark,add_time,add_handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_information);


        Intent intent = getIntent();
        final String token = intent.getStringExtra("token");
        final String avatar = intent.getStringExtra("avatar");

        add_money = (EditText) findViewById((R.id.add_money));
        add_mark = (EditText) findViewById((R.id.add_mark));
        add_time = (EditText) findViewById((R.id.add_time));
        add_handler = (EditText) findViewById((R.id.add_handler));

        inorout = (RadioGroup) findViewById(R.id.in_or_out);
        //为单选按钮设置监听
        inorout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
            }
        });


        final Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {   //创建ui，主线程中运行不安全
                    @Override
                    public void run() {
                        try {
                            String json =
                                        "money=" + add_money.getText().toString() + "&" +
                                        "time="  + add_time.getText().toString() + "&" +
                                        "type="  + type + "&" +
                                        "handler=" + add_handler.getText().toString() + "&" +
                                        "mark="  + add_mark.getText().toString();

                            System.out.println(json);

                            OkHttpClient client = new OkHttpClient();
                            Request request = null;
                            if (radioButton.getText().equals("收入信息")){
                                request = new Request.Builder()
                                        .url("http://192.168.123.188:8080/inaccount/inaccountNew")   //本电脑的ip地址
                                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                                        .header("token",token)
                                        .build();  //创造http请求
                            }else if (radioButton.getText().equals("支出信息")){
                                request = new Request.Builder()
                                        .url("http://192.168.123.188:8080/outaccount/outaccountNew")   //本电脑的ip地址
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
        type_btn = (Button) this.findViewById(R.id.add_type);
        //添加点击事件
         if (radioButton.getText().equals("收入信息")){
            popupMenu.setOnMenuItemClickListener((item) -> {
                switch (item.getItemId()){
                    case R.id.investment:
                        type_btn.setHint("投资理财");
                        type = "投资理财";
                        return true;
                    case R.id.wage:
                        type_btn.setHint("工资");
                        type = "工资";
                        return true;
                    case R.id.part_time:
                        type_btn.setHint("兼职");
                        type = "兼职";
                        return true;
                    case R.id.transfer:
                        type_btn.setHint("转账");
                        type = "转账";
                        return true;
                    case R.id.other:
                        type_btn.setHint("其他");
                        type = "其他";
                        return true;
                    default:
                        return false;
                }
            });
            inflater.inflate(R.menu.popupmenu1, popupMenu.getMenu());
        } else if (radioButton.getText().equals("支出信息")){
            popupMenu.setOnMenuItemClickListener((item) -> {
                switch (item.getItemId()){
                    case R.id.investment:
                        type_btn.setHint("投资理财");
                        type = "投资理财";
                        return true;
                    case R.id.clothing:
                        type_btn.setHint("服装美容");
                        type = "服装美容";
                        return true;
                    case R.id.life:
                        type_btn.setHint("生活日用");
                        type = "生活日用";
                        return true;
                    case R.id.food:
                        type_btn.setHint("餐饮美食");
                        type = "餐饮美食";
                        return true;
                    case R.id.other:
                        type_btn.setHint("其他");
                        type = "其他";
                        return true;
                    default:
                        return false;
                }
            });
            inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());
        }
        popupMenu.show();
    }
}