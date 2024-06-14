package com.example.mota.AdminFragments;

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

import com.example.mota.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminChangePassword extends Fragment {

    EditText oldPassword, newPassword, confirmPassword;
    Button close_btn, edit_btn;
    FirebaseFirestore db;
    DocumentReference documentReference;
    private SharedPreferences sharedPreferences;
    String emailAdmin = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        emailAdmin = sharedPreferences.getString("Email", "");
        documentReference = db.collection("Admin").document(""+emailAdmin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_change_password, container, false);
        oldPassword = view.findViewById(R.id.old_password_et);
        newPassword = view.findViewById(R.id.new_password_et);
        confirmPassword = view.findViewById(R.id.confirm_password_et);
        edit_btn = view.findViewById(R.id.admin_change_password_edit_btn);
        close_btn = view.findViewById(R.id.admin_change_password_close_btn);
        edit_btn.setOnClickListener(v -> {
            changePasswordIntoFirebase();
        });
        close_btn.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminAccountSetting());
            fragmentTransaction.commit();
        });
        return view;
    }

    private void changePasswordIntoFirebase() {
        Map<String, Object> passwordObject = new HashMap<>();
        passwordObject.put("password", newPassword.getText().toString());
        passwordObject.put("modified_timestamp", Timestamp.now());
        if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (oldPassword.getText().toString().equals(document.getString("password"))) {
                            documentReference.update(passwordObject);
                            Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, new AdminAccountSetting());
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(), "Old Password Incorrect!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            confirmPassword.setError("Not Matching");
        }
    }
}