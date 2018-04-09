package com.androiddev.artemqa.gototrip.modules;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.androiddev.artemqa.gototrip.R;

public class NewPostActivity extends AppCompatActivity implements ContractNewPost.View,View.OnClickListener {
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
        mIvPhotoPost = findViewById(R.id.iv_photo_new_post_a);
        mPresenter.viewIsReady();
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
                mPresenter.onButtonAddPostClicked();
                break;
        }
    }
}
