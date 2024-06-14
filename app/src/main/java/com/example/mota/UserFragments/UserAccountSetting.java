package com.example.mota.UserFragments;

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

import com.example.mota.AdminFragments.AdminDelete;
import com.example.mota.R;


public class UserAccountSetting extends Fragment {

    TextView change_password_text_view;
    Button delete_user_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_account_setting, container, false);
        change_password_text_view=view.findViewById(R.id.change_password_tv);
        change_password_text_view.setOnClickListener(v->{
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new UserChangePassword());
            fragmentTransaction.commit();
        });
        return view;
    }
}