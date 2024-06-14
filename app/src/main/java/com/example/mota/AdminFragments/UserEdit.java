package com.example.mota.AdminFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mota.R;
import com.example.mota.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserEdit extends Fragment {

    Button Close, Edit;
    EditText rollNo, firstName, lastName, password, mobileNumber, emailId;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    private SharedPreferences sharedPreferences;
    String rollNoUser = "";
    private static final String TAG = "UserEdit";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        rollNoUser = sharedPreferences.getString("RollNoUser", "");
        collectionReference = db.collection("Students_Table");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_edit, container, false);
        Close = view.findViewById(R.id.btn_user_close);
        Edit = view.findViewById(R.id.btn_user_edit);
        rollNo = view.findViewById(R.id.rollno);
        firstName = view.findViewById(R.id.user_edit_firstname);
        lastName = view.findViewById(R.id.user_edit_lastname);
        password = view.findViewById(R.id.user_edit_password);
        mobileNumber = view.findViewById(R.id.user_edit_mobile);
        emailId = view.findViewById(R.id.user_edit_email);
        rollNo.setText(rollNoUser);
        getUserDetails();
        Edit.setOnClickListener(v -> {
            if (rollNo.getText().toString().equals(rollNoUser)) {
                updateUserIntoFirebase();
            } else {
                addUserIntoFirebase();
                deleteUserIntoFirebase();
            }
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminUserList());
            fragmentTransaction.commit();
        });
        Close.setOnClickListener(v -> {
            if (getContext() instanceof FragmentActivity) {
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AdminUserList());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void getUserDetails() {
        collectionReference.document(rollNoUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    firstName.setText("" + document.getString("Name").split(" ")[0]);
                    lastName.setText("" + document.getString("Name").split(" ")[1]);
                    password.setText("" + document.getString("Password"));
                    mobileNumber.setText("" + document.getString("mobileNumber"));
                    emailId.setText("" + document.getString("emailId"));
                }
            }
        });
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
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Updating Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteUserIntoFirebase() {
        collectionReference.document("" + rollNoUser).delete();
    }

    private void updateUserIntoFirebase() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("Name", firstName.getText().toString() + " " + lastName.getText().toString());
        updates.put("Password", password.getText().toString());
        updates.put("emailId", "" + emailId.getText().toString());
        updates.put("mobileNumber", "" + mobileNumber.getText().toString());
        updates.put("modified_timestamp", Timestamp.now());
        collectionReference
                .document("" + rollNoUser)
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document updated successfully
                        Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(getActivity(), "Updating Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}