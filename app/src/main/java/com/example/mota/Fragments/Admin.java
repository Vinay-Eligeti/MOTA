package com.example.mota.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mota.R;
import com.example.mota.Utility;
import com.example.mota.Welcome;
import com.example.mota.WelcomeAdmin;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Admin extends Fragment {
    private FirebaseFirestore db;
    EditText emailEditText, passwordEditText;
    private SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        emailEditText = view.findViewById(R.id.email_admin_edit_text);
        passwordEditText = view.findViewById(R.id.password_admin_edit_text);
        loginButton = view.findViewById(R.id.login_admin_button);
        progressBar = view.findViewById(R.id.progress_admin_bar);

        sharedPreferences=getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("Email", "") != "") {
            requireActivity().startActivity(new Intent(getActivity(), WelcomeAdmin.class));
            getActivity().finish();
        }
        if (sharedPreferences.getString("RollNo", "") != "" && sharedPreferences.getString("Name", "") != "") {
            requireActivity().startActivity(new Intent(getActivity(), Welcome.class));
            getActivity().finish();
        }
        loginButton.setOnClickListener(v->{
            String username = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            LoginIntoFirebase(username, password, editor);
        });
        return view;
    }
    private void LoginIntoFirebase(String username, String password, SharedPreferences.Editor editor) {
        boolean IsValidated = validateData(username, password);
        if (!IsValidated) {
            return;
        }
        changeInProgress(true);
        db.collection("Admin")
                .document(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String passwordFromDb = document.getString("password");
                            String AdminName = document.getString("name");
                            if (password.equals(passwordFromDb)) {
                                editor.putString("Role", "Admin");
                                editor.putString("Email", username);
                                editor.putString("AdminName", AdminName);
                                editor.apply();
                                Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_LONG).show();
                                requireActivity().startActivity(new Intent(getActivity(), WelcomeAdmin.class));
                                getActivity().finish();
                            } else {
                                changeInProgress(false);
                                Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            changeInProgress(false);
                            Toast.makeText(getActivity(), "User not found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        changeInProgress(false);
                        Toast.makeText(getActivity(), "Login Failed due to some technical issue", Toast.LENGTH_LONG).show();
                    }
                });
    }


    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }

    }

    boolean validateData(String email, String password) {
        //validate the  data that are input by user.
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length() < 6){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password is invalid");
            return false;
        }
        return true;
    }

}