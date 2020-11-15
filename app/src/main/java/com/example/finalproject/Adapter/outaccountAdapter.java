package com.example.finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.Utils.MyRecyclerViewItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class outaccountAdapter extends RecyclerView.Adapter<outaccountAdapter.ViewHolder>{

    public List<Map<String,Object>> list=new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;
    int position2;

    public  outaccountAdapter(List<Map<String,Object>> list, Context con) {
        this.con = con;
        this.list = list;
        inflater = LayoutInflater.from(con);
    }

    @NonNull
    @Override
    public outaccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.recyclerview_item1,null);
        outaccountAdapter.ViewHolder viewHolder=new outaccountAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull outaccountAdapter.ViewHolder holder, int position) {

        holder.recy_time.setText(list.get(position).get("time").toString());
        holder.recy_type.setText(list.get(position).get("type").toString());
        holder.recy_handler.setText(list.get(position).get("handler").toString());
        holder.recy_money.setText(list.get(position).get("money").toString());
        holder.recy_mark.setText(list.get(position).get("mark").toString());

        String token = list.get(position).get("token").toString();
        String avatar = list.get(position).get("avatar").toString();

        //恢复状态
        holder.recyclerViewItem.apply();
        holder.modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("AddInformation");
                intent.putExtra("token",token);
                intent.putExtra("avatar",avatar);
                intent.putExtra("purpose","update");
                intent.putExtra("money",list.get(position).get("money").toString());
                intent.putExtra("time",list.get(position).get("time").toString());
                intent.putExtra("type",list.get(position).get("type").toString());
                intent.putExtra("handler",list.get(position).get("handler").toString());
                intent.putExtra("mark",list.get(position).get("mark").toString());
                intent.putExtra("id",list.get(position).get("id").toString());
                intent.putExtra("chart","支出信息");
                con.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            192.168.123.188
                            String json = "id="  + list.get(position).get("id").toString();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://weichenhao.cn:8080/outaccount/outaccountDelete")   //本电脑的ip地址
                                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),json))   //创建http客户端
                                    .header("token",token)
                                    .build();  //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的指令 ,若有返回值则储存其中
                            String responseData = response.body().string(); //获取返回回来的json结果
                            JSONObject jsonObject = new JSONObject(responseData);
                            String msg1 = jsonObject.getString("msg");
                            System.out.println(msg1);

                            position2 = position;
                            Message msg=new Message();
                            msg.what=1;
                            handler.sendMessage(msg);

                        }catch (Exception e){
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recy_time,recy_type,recy_handler,recy_money,recy_mark;
        public MyRecyclerViewItem recyclerViewItem;
        public TextView delete,modification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recy_time = itemView.findViewById(R.id.recy_time);
            recy_type = itemView.findViewById(R.id.recy_type);
            recy_handler = itemView.findViewById(R.id.recy_address);
            recy_money = itemView.findViewById(R.id.recy_money);
            recy_mark = itemView.findViewById(R.id.recy_mark);
            recyclerViewItem=itemView.findViewById(R.id.scroll_item);
            delete = itemView.findViewById(R.id.delete);
            modification = itemView.findViewById(R.id.modification);
        }
    }
}
