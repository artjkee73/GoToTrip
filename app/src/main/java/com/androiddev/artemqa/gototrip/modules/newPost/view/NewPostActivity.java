package com.androiddev.artemqa.gototrip.modules.newPost.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.newPost.presenter.NewPostPresenter;
import com.androiddev.artemqa.gototrip.modules.newPost.ContractNewPost;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class NewPostActivity extends BaseActivity implements ContractNewPost.View,View.OnClickListener {
    private TextInputEditText mEtTitle;
    private EditText mEtTextPost;
    private ImageView mIvPhotoPost;
    private Button mBtnAddPost;
    private ContractNewPost.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initView();
    }

    private void initView() {
        mPresenter = new NewPostPresenter();
        mPresenter.attachView(this);
        mEtTitle = findViewById(R.id.et_title_new_post_a);
        mEtTextPost = findViewById(R.id.et_description_text_new_post_a);
        mBtnAddPost = findViewById(R.id.btn_add_post_new_post_a);
        mBtnAddPost.setOnClickListener(this);
        mIvPhotoPost = findViewById(R.id.iv_photo_new_post_a);
        mIvPhotoPost.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_photo_new_post_a:
                mPresenter.onPhotoPostClicked();
                break;

            case R.id.btn_add_post_new_post_a:
                mPresenter.onButtonAddPostClicked(mEtTitle.getText().toString(),mEtTextPost.getText().toString());
                break;
        }
    }

    public void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constants.PICK_PHOTO_NEW_POST);
    }

    @Override
    public void showErrorUploadPhoto() {
        Toast.makeText(NewPostActivity.this,R.string.er_failed_upload_photo_new_post_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showErrorNoPhotoPost() {
        Toast.makeText(NewPostActivity.this,R.string.er_no_uploaded_photo_new_post_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessAddPost() {
        Toast.makeText(NewPostActivity.this,R.string.success_add_post_new_post_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPostPhoto(String urlPhoto) {
        Glide.with(getApplicationContext()).load(urlPhoto).into(mIvPhotoPost);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_PHOTO_NEW_POST) {
                Uri selectedImageUri = data.getData();
                byte[] compressPhotoByteArray = Utils.compressPhoto(selectedImageUri, this);
                mPresenter.savePhoto(compressPhotoByteArray);
            }
        }

    }
}
