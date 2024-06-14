package com.example.mota.AdminFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.mota.AdminFragments.TestScheduleAdmin.MyAdpater;
import com.example.mota.AdminFragments.TestScheduleAdmin.TestList;
import com.example.mota.AdminFragments.UserList.MyAdapater;
import com.example.mota.AdminFragments.UserList.UserList;
import com.example.mota.R;
import com.example.mota.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdminUserList extends Fragment {

    MyAdapater adapter;
    private FirebaseFirestore db;
    private static final String TAG = "AdminUserList";

    private List<UserList> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_list, container, false);

        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        items = new ArrayList<>();
        adapter = new MyAdapater(getActivity(), items);
        recyclerView.setAdapter(adapter);
        // Listen for real-time updates
        db.collection("Students_Table")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        items.clear();
                        for (QueryDocumentSnapshot document : snapshots) {
                            String name = document.getString("Name");
                            String id = document.getId();
                            items.add(new UserList(id,name));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        Button addUser = view.findViewById(R.id.add_user);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new UserAdd());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;

    }
}