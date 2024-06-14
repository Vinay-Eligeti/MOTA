package com.example.mota.AdminFragments;

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

import com.example.mota.R;

public class AdminAccountSetting extends Fragment {

    TextView change_password_text_view,edit_account_text_view;
    Button admin_delete_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_admin_account_setting, container, false);
        admin_delete_btn=view.findViewById(R.id.admin_delete_btn);
        change_password_text_view=view.findViewById(R.id.change_password_tv);
        edit_account_text_view=view.findViewById(R.id.edit_account_details_tv);
        admin_delete_btn.setOnClickListener(v->{
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminDelete());
            fragmentTransaction.commit();
        });
        change_password_text_view.setOnClickListener(v->{
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminChangePassword());
            fragmentTransaction.commit();
        });
        edit_account_text_view.setOnClickListener(v->{
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminEditAccount());
            fragmentTransaction.commit();
        });
        return view;
    }
}