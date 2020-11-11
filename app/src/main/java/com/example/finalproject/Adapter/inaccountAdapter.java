package com.example.finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class inaccountAdapter extends RecyclerView.Adapter<inaccountAdapter.ViewHolder> {

    public List<Map<String,Object>> list=new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;
    public  inaccountAdapter(List<Map<String,Object>> list, Context con) {
        this.con = con;
        this.list = list;
        inflater = LayoutInflater.from(con);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.recyclerview_item,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recy_time.setText(list.get(position).get("time").toString());
        holder.recy_type.setText(list.get(position).get("type").toString());
        holder.recy_handler.setText(list.get(position).get("handler").toString());
        holder.recy_money.setText(list.get(position).get("money").toString());
        holder.recy_mark.setText(list.get(position).get("mark").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recy_time,recy_type,recy_handler,recy_money,recy_mark;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recy_time = itemView.findViewById(R.id.recy_time);
            recy_type = itemView.findViewById(R.id.recy_type);
            recy_handler = itemView.findViewById(R.id.recy_handler);
            recy_money = itemView.findViewById(R.id.recy_money);
            recy_mark = itemView.findViewById(R.id.recy_mark);
        }
    }
}
