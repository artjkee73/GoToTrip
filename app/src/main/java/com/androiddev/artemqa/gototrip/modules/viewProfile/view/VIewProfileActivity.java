package com.androiddev.artemqa.gototrip.modules.viewProfile.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.dialog.view.DialogActivity;
import com.androiddev.artemqa.gototrip.modules.viewProfile.ContractViewProfile;
import com.androiddev.artemqa.gototrip.modules.viewProfile.presenter.ViewProfilePresenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ViewProfileActivity extends AppCompatActivity implements ContractViewProfile.View,View.OnClickListener{

    ImageView mIvAvatarUser;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    Toolbar mToolbar;
    Button mBtnChat, mBtnFollow, mBtnUnFollow, mBtnPostsUser, mBtnFollowersUser, mBtnFollowingsUser;
    TextView mTvLocationUser, mTvVisitedCountries, mTvAboutUser;
    ViewProfilePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        initView();

    }

    private void initView() {
        mPresenter = new ViewProfilePresenter();
        mPresenter.attachView(this);
        mIvAvatarUser = findViewById(R.id.iv_avatar_user_view_profile_a);
        mCollapsingToolbarLayout = findViewById(R.id.ctl_view_profile_a);
        mToolbar = findViewById(R.id.toolbar_view_profile_a);
        mBtnChat = findViewById(R.id.btn_chat_user_view_profile_a);
        mBtnChat.setOnClickListener(this);
        mBtnFollow = findViewById(R.id.btn_follow_on_user_view_profile_a);
        mBtnFollow.setOnClickListener(this);
        mBtnUnFollow = findViewById(R.id.btn_un_follow_user_view_profile_a);
        mBtnUnFollow.setOnClickListener(this);
        mBtnPostsUser = findViewById(R.id.btn_posts_user_view_profile_a);
        mBtnPostsUser.setOnClickListener(this);
        mBtnFollowersUser = findViewById(R.id.btn_followers_view_profile_a);
        mBtnFollowersUser.setOnClickListener(this);
        mBtnFollowingsUser = findViewById(R.id.btn_followings_user_view_profile_a);
        mBtnFollowingsUser.setOnClickListener(this);
        mTvLocationUser = findViewById(R.id.tv_location_user_view_profile_a);
        mTvVisitedCountries = findViewById(R.id.tv_visited_country_view_profile_a);
        mTvAboutUser = findViewById(R.id.tv_about_me_view_profile_a);
        mPresenter.viewIsReady(getViewUsersIdFromIntent());
    }

    private String getViewUsersIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_CLICKED_USER_ID_SEARCH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


    @Override
    public void setUserAvatar(String urlAvatar) {

        Picasso.get().load(urlAvatar).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                mIvAvatarUser.getLayoutParams().height = height;
                mIvAvatarUser.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public void setUserName(String userName) {
        mCollapsingToolbarLayout.setTitle(userName);
    }

    @Override
    public void setPostsCount(int postsCount) {
        mBtnPostsUser.setText(getResources().getString(R.string.btn_posts_user_view_profile_a, postsCount));
    }

    @Override
    public void setFollowersCount(int followersCount) {
        mBtnFollowersUser.setText(getResources().getString(R.string.btn_followers_view_profile_a, followersCount));
    }

    @Override
    public void setFollowingsCount(int followingsCount) {
        mBtnFollowingsUser.setText(getResources().getString(R.string.btn_followings_user_view_profile_a, followingsCount));
    }

    @Override
    public void setIsFollowOnUser(boolean isFollow) {
        if(!isFollow){
            mBtnFollow.setVisibility(View.VISIBLE);
            mBtnUnFollow.setVisibility(View.GONE);
        } else {
            mBtnFollow.setVisibility(View.GONE);
            mBtnUnFollow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserLocation(String locationUser) {
        mTvLocationUser.setText(locationUser);
    }

    @Override
    public void setVisitedCountriesUser(String visitedCountriesUser) {
        mTvVisitedCountries.setText(visitedCountriesUser);
    }

    @Override
    public void setAboutUser(String aboutUser) {
        mTvAboutUser.setText(aboutUser);
    }

    @Override
    public void showDialogActivity(String viewUsersId) {
        Intent intent = new Intent(ViewProfileActivity.this, DialogActivity.class);
        intent.putExtra(Constants.INTENT_DIALOG_USER_ID_VIEW_PROFILE,viewUsersId);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_follow_on_user_view_profile_a:
                mPresenter.onButtonFollowClicked(getViewUsersIdFromIntent());
                break;

            case R.id.btn_un_follow_user_view_profile_a:
                mPresenter.onButtonUnFollowClicked(getViewUsersIdFromIntent());
                break;

            case R.id.btn_chat_user_view_profile_a:
                mPresenter.onButtonMessageClicked(getViewUsersIdFromIntent());
                break;
        }
    }
}
