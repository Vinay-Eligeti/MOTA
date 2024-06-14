package com.example.mota.AdminFragments.UserList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView nameView, rollnoView;
    Button optionButton;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.name);
        rollnoView = itemView.findViewById(R.id.rollno);
        optionButton = itemView.findViewById(R.id.OptionButton);
    }
}
