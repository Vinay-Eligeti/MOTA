package com.example.mota;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.mota.AdminFragments.AdminAccountSetting;
import com.example.mota.AdminFragments.AdminDashboard;
import com.example.mota.AdminFragments.AdminEditAccount;
import com.example.mota.AdminFragments.AdminResult;
import com.example.mota.AdminFragments.AdminTestSchedule;
import com.example.mota.AdminFragments.AdminUserList;
import com.example.mota.UserFragments.TestResult;
import com.google.android.material.navigation.NavigationView;

public class WelcomeAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_admin_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AdminDashboard()).commit();
            navigationView.setCheckedItem(R.id.nav_admin_dashboard);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_admin_dashboard:
                selectedFragment = new AdminDashboard();
                break;
            case R.id.nav_admin_user:
                selectedFragment = new AdminUserList();
                break;
            case R.id.nav_admin_schedule:
                selectedFragment = new AdminTestSchedule();
                break;
            case R.id.nav_admin_result:
                selectedFragment = new AdminResult();
                break;
            case R.id.nav_admin_account_settings:
                selectedFragment = new AdminAccountSetting();
                break;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_logout) {
            // Show the custom dialog when the logout option is selected
            LogoutDialogFragment dialogFragment = new LogoutDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "LogoutDialogFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}