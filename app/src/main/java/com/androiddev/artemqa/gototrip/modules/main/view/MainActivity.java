package com.androiddev.artemqa.gototrip.modules.main.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.androiddev.artemqa.gototrip.modules.editProfile.view.EditProfileActivity;
import com.androiddev.artemqa.gototrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initForm();
        setUserInformationOnNavHeader();
    }

    private void setUserInformationOnNavHeader() {
    }

    private void initForm() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout_main_a);
        navigationView = findViewById(R.id.nav_view_main_a);
        setToggleButtonInNavDrower();

    }

    private void setToggleButtonInNavDrower() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.toggle_open_main_a, R.string.toggle_close_main_a);
        drawerLayout.addDrawerListener(toggle);
        navigationView = findViewById(R.id.nav_view_main_a);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.profile_nav_main :
                intent = new Intent(MainActivity.this,EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.search_nav_main :

                break;
            case R.id.chat_nav_main :

                break;
            case R.id.new_post_nav_main :

                break;
            case R.id.settings_nav_main :

                break;
            case R.id.log_out_nav_main :

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
