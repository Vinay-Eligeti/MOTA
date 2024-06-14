package com.example.mota.UserFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mota.AdminFragments.AdminAccountSetting;
import com.example.mota.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserChangePassword extends Fragment {

    Button Close, Edit;
    EditText oldPassword, newPassword, confirmPassword;
    FirebaseFirestore db;
    DocumentReference documentReference;
    private SharedPreferences sharedPreferences;
    String rollUser = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        rollUser = sharedPreferences.getString("RollNo", "");
        documentReference = db.collection("Students_Table").document("" + rollUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_change_password, container, false);
        oldPassword = view.findViewById(R.id.old_user_password_et);
        newPassword = view.findViewById(R.id.new_user_password_et);
        confirmPassword = view.findViewById(R.id.confirm_user_password_et);
        Close = view.findViewById(R.id.user_change_password_close_btn);
        Edit = view.findViewById(R.id.user_change_password_edit_btn);
        Edit.setOnClickListener(v -> {
            if(oldPassword.getText().toString().equals(newPassword.getText().toString())){
                Toast.makeText(getActivity(), "The same password has been entered in all fields.", Toast.LENGTH_SHORT).show();
            }else {
                changePasswordIntoFirebase();
            }
        });
        Close.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new UserAccountSetting());
            fragmentTransaction.commit();
        });
        return view;
    }

    private void changePasswordIntoFirebase() {
        Map<String, Object> passwordObject = new HashMap<>();
        passwordObject.put("Password", newPassword.getText().toString());
        passwordObject.put("modified_timestamp", Timestamp.now());
        if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (oldPassword.getText().toString().equals(document.getString("Password"))) {
                            documentReference.update(passwordObject);
                            Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, new UserAccountSetting());
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(), "Old Password Incorrect!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {
            confirmPassword.setError("Not Matching");
        }
    }
}