package com.example.mota;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.mota.UserFragments.TestCompleted;
import com.example.mota.UserFragments.TestResult;
import com.example.mota.UserFragments.TestSchedule;
import com.example.mota.UserFragments.UserAccountSetting;
import com.example.mota.UserFragments.UserHome;
import com.google.android.material.navigation.NavigationView;

public class Welcome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Toolbar toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_user_layout);
        NavigationView navigationView = findViewById(R.id.nav_user_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new UserHome()).commit();
            navigationView.setCheckedItem(R.id.nav_user_home);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_user_home:
                selectedFragment = new UserHome();
                break;
            case R.id.nav_user_exam_schedule:
                selectedFragment = new TestSchedule();
                break;
            case R.id.nav_user_account_settings:
                selectedFragment=new UserAccountSetting();
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
    private void enableImmersiveMode() {
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }
}