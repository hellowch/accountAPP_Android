package com.example.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.Utils.MyRecyclerViewItem;
import com.example.finalproject.pojo.Urls;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class memoAdapter extends RecyclerView.Adapter<memoAdapter.ViewHolder> {

    public List<Map<String,Object>> list = new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;
    int position2;

    public memoAdapter(List<Map<String, Object>> list, Context con) {
        this.con = con;
        this.list = list;
        inflater = LayoutInflater.from(con);
    }


    @NonNull
    @Override
    public memoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //连接对应的xml文件
        View view = inflater.inflate(R.layout.recyclerview_item2,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //用于接收activity传过来的数据
    @Override
    public void onBindViewHolder(@NonNull memoAdapter.ViewHolder holder, int position) {
        holder.recy_memo.setText(list.get(position).get("memo").toString());
        String token = list.get(position).get("token").toString();
        String avatar = list.get(position).get("avatar").toString();

        //恢复列表的状态
        holder.recyclerViewItem.apply();
        //进入编辑页面，修改便签内容
        holder.modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("MemoUpdateActivity");
                intent.putExtra("token",token);
                intent.putExtra("avatar",avatar);
                intent.putExtra("purpose","update");
                intent.putExtra("memo",list.get(position).get("memo").toString());
                intent.putExtra("id",list.get(position).get("id").toString());
                con.startActivity(intent);
            }
        });

        //删除功能
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = "id=" + list.get(position).get("id").toString();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(Urls.getUrl() + "memo/memoDelete")
                                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))
                                    .header("token",token)
                                    .build();
                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);

                            position2 = position;
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    removeItem(position2);
                    break;
            }
        }
    };

    public void removeItem(int position){
        list.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position,getItemCount()); //刷新被删除数据，以及其后面的数据
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    //定义所有参数和匹配的组件
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recy_memo;
        public MyRecyclerViewItem recyclerViewItem;
        public TextView delete,modification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recy_memo = itemView.findViewById(R.id.recy_memo);

            recyclerViewItem = itemView.findViewById(R.id.scroll_item);
            delete = itemView.findViewById(R.id.delete);
            modification = itemView.findViewById(R.id.modification);
        }
    }
}
