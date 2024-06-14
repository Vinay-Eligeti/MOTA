package com.example.mota.AdminFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mota.R;
import com.example.mota.Utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScheduleRemove extends Fragment {


    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View view=inflater.inflate(R.layout.fragment_schedule_remove, container, false);
        Button Close=view.findViewById(R.id.btn_test_remove_no);
        Button Continue=view.findViewById(R.id.btn_test_remove_yes);
        TextView exam_name_textview=view.findViewById(R.id.exam_name);
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        String examID=sharedPreferences.getString("examID", "");
        String examName=sharedPreferences.getString("examName", "");
        exam_name_textview.setText("Are you sure to remove "+examName+" ?");
        DocumentReference docRef = db.collection("Exams").document(examID);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new AdminTestSchedule());
                    fragmentTransaction.commit();
                }
            }
        });
        Continue.setOnClickListener(v->{
            docRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, new AdminTestSchedule());
                        fragmentTransaction.commit();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed!!", Toast.LENGTH_LONG).show();
                    });
        });
        return view;
    }
}