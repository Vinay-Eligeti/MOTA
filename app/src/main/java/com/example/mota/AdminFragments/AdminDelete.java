package com.example.mota.AdminFragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.mota.MainActivity;
import com.example.mota.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDelete extends Fragment {

    Button Close,Continue;
    FirebaseFirestore db;
    DocumentReference documentReference;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String emailAdmin = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        emailAdmin = sharedPreferences.getString("Email", "");
        documentReference = db.collection("Admin").document(emailAdmin);
        editor = sharedPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_admin_delete, container, false);
        Close=view.findViewById(R.id.admin_account_delete_close_btn);
        Continue=view.findViewById(R.id.admin_account_delete_continue_btn);
        Continue.setOnClickListener(v->{
            deleteAdminAccountIntoFirebase();
        });
        Close.setOnClickListener(v->{
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminAccountSetting());
            fragmentTransaction.commit();
        });
        return view;
    }

    private void deleteAdminAccountIntoFirebase() {
        documentReference.delete().addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                    editor.putString("Email", "");
                    editor.putString("AdminName", "");
                    editor.apply();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Deleting Failed!!", Toast.LENGTH_LONG).show();
                });
    }
}