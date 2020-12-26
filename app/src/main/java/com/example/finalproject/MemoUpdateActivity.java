package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


/**
 * 添加修改便签模块
 */
public class MemoUpdateActivity extends AppCompatActivity {

    public String token,avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        avatar = intent.getStringExtra("avatar");
    }
}