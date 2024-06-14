package com.example.mota;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mota.Fragments.Admin;
import com.example.mota.Fragments.User;

public class ViewPageAdapter extends FragmentStateAdapter {
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new User();
            case 1:
                return new Admin();
            default:
                return new User();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
