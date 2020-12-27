package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * 便签模块
 */
public class MemoActivity extends AppCompatActivity {

    public String token,avatar;
    public String purpose;   //用于便签编辑模块模块判断当前是新便签还是修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        avatar = intent.getStringExtra("avatar");
    }

    public void newClick(View v){
        purpose = "new";
        Intent intent = new Intent(MemoActivity.this,MemoUpdateActivity.class);
        intent.putExtra("token",token);
        intent.putExtra("avatar",avatar);
        intent.putExtra("purpose",purpose);
        startActivity(intent);
    }
}