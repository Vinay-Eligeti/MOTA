package com.example.mota.AdminFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mota.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminDashboard extends Fragment {

    private FirebaseFirestore db;
    TextView No_of_exam, no_of_student;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        LinearLayout linearLayout1 = view.findViewById(R.id.nav_to_exam_form);
        linearLayout1.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminTestSchedule());
            fragmentTransaction.commit();
        });
        LinearLayout linearLayout2 = view.findViewById(R.id.nav_to_user_list);
        linearLayout2.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminUserList());
            fragmentTransaction.commit();
        });
        db = FirebaseFirestore.getInstance();
        No_of_exam = view.findViewById(R.id.textView20);
        no_of_student = view.findViewById(R.id.total_Student);
        CollectionReference collection = db.collection("Exams");
        collection.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle errors
                No_of_exam.setText("Error fetching data");
                return;
            }
            if (querySnapshot != null) {
                int count = querySnapshot.size();
                No_of_exam.setText(String.valueOf(count));
            } else {
                No_of_exam.setText("No documents found");
            }
        });


        CollectionReference collection2 = db.collection("Students_Table");
        collection2.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                // Handle errors
                no_of_student.setText("Error fetching data");
                return;
            }
            if (querySnapshot != null) {
                int count = querySnapshot.size();
                no_of_student.setText(String.valueOf(count));
            } else {
                no_of_student.setText("No documents found");
            }
        });

        return view;
    }
}