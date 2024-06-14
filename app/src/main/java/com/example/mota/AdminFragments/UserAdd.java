package com.example.mota.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mota.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserAdd extends Fragment {

    FirebaseFirestore db;
    CollectionReference collectionReference;
    Button Close, Edit;
    EditText rollNo, firstName, lastName, password, mobileNumber, emailId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Students_Table");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);
        Close = view.findViewById(R.id.btn_user_close);
        Edit = view.findViewById(R.id.btn_user_add);
        rollNo = view.findViewById(R.id.rollno);
        firstName = view.findViewById(R.id.user_add_firstname);
        lastName = view.findViewById(R.id.user_add_lastname);
        password = view.findViewById(R.id.user_add_password);
        mobileNumber = view.findViewById(R.id.user_add_mobile);
        emailId = view.findViewById(R.id.user_add_email);

        Edit.setOnClickListener(v -> {
            addUserIntoFirebase();
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminUserList());
            fragmentTransaction.commit();
        });


        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new AdminUserList());
                    fragmentTransaction.commit();
                }
            }
        });
        return view;
    }

    private void addUserIntoFirebase() {
        Map<String, Object> add = new HashMap<>();
        add.put("Name", firstName.getText().toString() + " " + lastName.getText().toString());
        add.put("Password", password.getText().toString());
        add.put("emailId", "" + emailId.getText().toString());
        add.put("mobileNumber", "" + mobileNumber.getText().toString());
        add.put("created_timestamp", Timestamp.now());
        collectionReference
                .document("" + rollNo.getText().toString())
                .set(add)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Updating Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}