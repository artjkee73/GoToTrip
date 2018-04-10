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
import android.view.View;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.modules.chat.view.ChatActivity;
import com.androiddev.artemqa.gototrip.modules.editProfile.view.EditProfileActivity;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;
import com.androiddev.artemqa.gototrip.modules.main.presenter.MainPresenter;
import com.androiddev.artemqa.gototrip.modules.newPost.view.NewPostActivity;
import com.androiddev.artemqa.gototrip.modules.search.view.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;
    MainContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initForm();
    }


    private void initForm() {
        mDrawerLayout = findViewById(R.id.drawer_layout_main_a);
        mNavigationView = findViewById(R.id.nav_view_main_a);
        setToggleButtonInNavDrower();
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mPresenter.viewIsReady();


    }

    private void setToggleButtonInNavDrower() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.toggle_open_main_a, R.string.toggle_close_main_a);
        mDrawerLayout.addDrawerListener(mToggle);
        mNavigationView = findViewById(R.id.nav_view_main_a);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.profile_nav_main :
                mPresenter.onClickProfileItem();
                break;
            case R.id.search_nav_main :
                mPresenter.onClickSearchItem();
                break;
            case R.id.chat_nav_main :
                mPresenter.onClickChatItem();
                break;
            case R.id.new_post_nav_main :
                mPresenter.onClickNewPostItem();
                break;
            case R.id.settings_nav_main :

                break;
            case R.id.log_out_nav_main :

                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUserInformationOnNavDrawer(String name, String email, String urlPhoto) {
        View headerNavDrawer =  mNavigationView.getHeaderView(0);
        TextView tvName = headerNavDrawer.findViewById(R.id.tv_name_header_nav_drawer);
        TextView tvEmail = headerNavDrawer.findViewById(R.id.tv_email_header_nav_drawer);
        CircleImageView ivAvatar = headerNavDrawer.findViewById(R.id.iv_header_nav_drawer);
        if( urlPhoto != null){
            Picasso.get().load(urlPhoto).into(ivAvatar);
        }
        tvName.setText(name);
        tvEmail.setText(email);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void openEditProfile() {
        Intent intent = new Intent(MainActivity.this,EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void openSearch() {
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void openChat() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void openNewPost() {
        Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
        startActivity(intent);
    }
}
