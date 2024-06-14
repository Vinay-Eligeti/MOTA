package com.example.mota.UserFragments.TestResults;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Result> items;

    public MyAdapter(Context context, List<Result> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.result_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.questionView.setText(items.get(position).getQuestion());
        holder.choosenanswerView.setText(items.get(position).getChosenAnswer());
        holder.resultView.setText(items.get(position).getCheck());
        if(holder.resultView.getText().toString().equals("✓")){
            holder.resultView.setBackgroundColor(Color.parseColor("#4CAF50"));
        }else {
            holder.resultView.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        holder.correctanswerView.setText(items.get(position).getCorrectAnswer());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
