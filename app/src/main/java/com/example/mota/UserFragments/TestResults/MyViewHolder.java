package com.example.mota.UserFragments.TestResults;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView questionView, choosenanswerView, resultView, correctanswerView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        questionView = itemView.findViewById(R.id.question);
        choosenanswerView = itemView.findViewById(R.id.chosen_answer);
        resultView = itemView.findViewById(R.id.result);
        correctanswerView = itemView.findViewById(R.id.correct_answer);
    }
}
