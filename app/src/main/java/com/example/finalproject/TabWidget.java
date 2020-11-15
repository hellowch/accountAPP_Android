package com.example.finalproject;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TabWidget extends TabActivity  {

    public ImageView avatarId;
    public String avatar;
    public String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        avatarId = (ImageView)findViewById(R.id.avatarId);
        Intent intent = getIntent();
        avatar = intent.getStringExtra("avatar");
        token = intent.getStringExtra("token");
        Glide.with(TabWidget.this).load(avatar).into(avatarId);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;
        Intent intentTab;  // Reusable Intent for each tab

        //第一个TAB
        intentTab = new Intent(this, InActivity.class);//新建一个Intent用作Tab1显示的内容
        intentTab.putExtra("token", token);
        intentTab.putExtra("avatar", avatar);
        spec = tabHost.newTabSpec("tab1")//新建一个 Tab
                .setIndicator("收入信息表")//设置名称以及图标
                .setContent(intentTab);//设置显示的intent，这里的参数也可以是R.id.xxx
        tabHost.addTab(spec);//添加进tabHost

        //第二个TAB
        intentTab = new Intent(this, OutActivity.class);//第二个Intent用作Tab1显示的内容
        intentTab.putExtra("token", token);
        intentTab.putExtra("avatar", avatar);
        spec = tabHost.newTabSpec("tab2")//新建一个 Tab
                .setIndicator("支出信息表")//设置名称以及图标
                .setContent(intentTab);//设置显示的intent，这里的参数也可以是R.id.xxx
        tabHost.addTab(spec);//添加进tabHost

        //第三个TAB
        intentTab = new Intent(this, CountActivityIn.class);
        intentTab.putExtra("token", token);
        intentTab.putExtra("avatar", avatar);
        spec = tabHost.newTabSpec("tab3")
                .setIndicator("收入统计")
                .setContent(intentTab);
        tabHost.addTab(spec);

        //第四个TAB
        intentTab = new Intent(this, CountActivityOut.class);
        intentTab.putExtra("token", token);
        intentTab.putExtra("avatar", avatar);
        spec = tabHost.newTabSpec("tab4")
                .setIndicator("支出统计")
                .setContent(intentTab);
        tabHost.addTab(spec);

//        .setIndicator("Tab2", res.getDrawable(android.R.drawable.ic_menu_camera))//设置名称以及图标
        tabHost.setCurrentTab(0);

    }


    public void popupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        //添加点击事件
        popupMenu.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()){
                case R.id.exit:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String json = "";
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://weichenhao.cn:8080/user/logout")   //本电脑的ip地址
                                        .post(RequestBody.create(MediaType.parse("application/json"),json))   //创建http客户端
                                        .header("token",token)
                                        .build();  //创造http请求
                                Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent login = new Intent(TabWidget.this,Login.class);
                                        startActivity(login);
                                    }
                                });

                            }catch (Exception e){
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(TabWidget.this,"网络失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    return true;
                default:
                    return false;
            }
        });
        inflater.inflate(R.menu.popupmenu2, popupMenu.getMenu());
        popupMenu.show();
    }

}
