package com.example.finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.Utils.MyRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class outaccountAdapter extends RecyclerView.Adapter<outaccountAdapter.ViewHolder>{

    public List<Map<String,Object>> list=new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;
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
        holder.recy_address.setText(list.get(position).get("address").toString());
        holder.recy_money.setText(list.get(position).get("money").toString());
        holder.recy_mark.setText(list.get(position).get("mark").toString());

        //恢复状态
        holder.recyclerViewItem.apply();
        holder.modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), "编辑："+position, Toast.LENGTH_LONG).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), "删除："+position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recy_time,recy_type,recy_address,recy_money,recy_mark;
        public MyRecyclerViewItem recyclerViewItem;
        public TextView delete,modification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recy_time = itemView.findViewById(R.id.recy_time);
            recy_type = itemView.findViewById(R.id.recy_type);
            recy_address = itemView.findViewById(R.id.recy_address);
            recy_money = itemView.findViewById(R.id.recy_money);
            recy_mark = itemView.findViewById(R.id.recy_mark);
            recyclerViewItem=itemView.findViewById(R.id.scroll_item);
            delete = itemView.findViewById(R.id.delete);
            modification = itemView.findViewById(R.id.modification);
        }
    }
}
