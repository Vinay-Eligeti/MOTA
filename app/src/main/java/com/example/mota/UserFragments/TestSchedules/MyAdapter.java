package com.example.mota.UserFragments.TestSchedules;

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
    List<TestList> items;

    public MyAdapter(Context context, List<TestList> items) {
        this.context = context;
        this.items = items;
    }

    public void setData(List<TestList> data) {
        items = data;
    }
    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.test_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.timestampView.setText(items.get(position).getTime());
        holder.examView.setText(items.get(position).getExamName());
        holder.statusView.setText(items.get(position).getStatus());
        holder.subjectView.setText(items.get(position).getSubjectName());
        holder.setDocumentId(items.get(position).getDocumentId());
        if (holder.statusView.getText().toString().equals("Assigned")){
            holder.statusView.setBackgroundColor(Color.parseColor("#FFA500"));
        } else if (holder.statusView.getText().toString().equals("Past Due")) {
            holder.statusView.setBackgroundColor(Color.parseColor("#FF0000"));
        }else {
            holder.statusView.setBackgroundColor(Color.parseColor("#4CAF50"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
