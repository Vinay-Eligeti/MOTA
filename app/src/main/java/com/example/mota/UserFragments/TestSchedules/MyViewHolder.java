package com.example.mota.UserFragments.TestSchedules;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.R;
import com.example.mota.UserFragments.QR_Code;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView timestampView,examView, statusView, subjectView;
    String documentId;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        timestampView = itemView.findViewById(R.id.TimeStamp);
        examView = itemView.findViewById(R.id.ExamName);
        statusView = itemView.findViewById(R.id.Status);
        subjectView = itemView.findViewById(R.id.SubjectName);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message with the document ID when the item is clicked
                if (documentId != null) {
                    Toast.makeText(itemView.getContext(), "Document ID: " + documentId, Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = ((FragmentActivity) itemView.getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    QR_Code qrCodeFragment = new QR_Code();
                    Bundle bundle = new Bundle();
                    bundle.putString("examID", documentId);
                    qrCodeFragment.setArguments(bundle);
                    transaction.replace(R.id.frame_layout, qrCodeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
