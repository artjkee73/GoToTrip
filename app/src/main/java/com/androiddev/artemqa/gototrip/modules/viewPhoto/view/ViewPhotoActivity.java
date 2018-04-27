package com.androiddev.artemqa.gototrip.modules.viewPhoto.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.ContractViewPhoto;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.presenter.ViewPhotoPresenter;

public class ViewPhotoActivity extends AppCompatActivity implements ContractViewPhoto.View {
    private PhotoView mIvViewPhoto;
    private ContractViewPhoto.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        initView();
    }

    private void initView() {
        mPresenter = new ViewPhotoPresenter();
        mPresenter.attachView(this);
        mIvViewPhoto = findViewById(R.id.iv_view_photo_view_photo_a);
        mPresenter.viewIsReady(getViewPhotoUrl());
    }

    private String getViewPhotoUrl() {
        return getIntent().getStringExtra(Constants.INTENT_PHOTO_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void setViewPhoto(String viewPhotoUrl) {
        Utils.loadImage(ViewPhotoActivity.this,viewPhotoUrl,mIvViewPhoto);
    }
}
