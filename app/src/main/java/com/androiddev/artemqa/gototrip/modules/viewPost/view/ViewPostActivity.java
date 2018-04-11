package com.androiddev.artemqa.gototrip.modules.viewPost.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.listComments.view.ListCommentsActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.ContractViewPost;
import com.androiddev.artemqa.gototrip.modules.viewPost.presenter.ViewPostPresenter;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostActivity extends AppCompatActivity implements ContractViewPost.View ,View.OnClickListener{
    private ContractViewPost.Presenter mPresenter;
    public CircleImageView mIvAvatarAuthor;
    public ImageView mIvPostPhoto;
    public TextView mTvNameAuthor, mTvDatePost, mTvTitlePost, mTvTextPost;
    public Button mBtnLike, mBtnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        initView();
    }

    private void initView() {
        mPresenter = new ViewPostPresenter();
        mPresenter.attachView(this);
        mIvAvatarAuthor = findViewById(R.id.iv_author_avatar_view_post_a);
        mIvPostPhoto = findViewById(R.id.iv_image_post_view_post_a);
        mTvNameAuthor = findViewById(R.id.tv_name_author_view_post_a);
        mTvDatePost = findViewById(R.id.tv_date_post_view_post_a);
        mTvTextPost = findViewById(R.id.tv_text_post_view_post_a);
        mTvTitlePost = findViewById(R.id.tv_title_view_post_a);
        mBtnLike = findViewById(R.id.btn_like_view_post_a);
        mBtnComment = findViewById(R.id.btn_comment_view_post_a);
        mBtnComment.setOnClickListener(this);
        mPresenter.viewIsReady(getPostIdFromIntent());
    }

    private String getPostIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_VIEW_POST_POST_ID_LIST_POSTS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void setAvatarAuthor(String urlAvatarAuthor) {
        Glide.with(getApplicationContext()).load(urlAvatarAuthor).into(mIvAvatarAuthor);
    }

    @Override
    public void setNameAuthor(String nameAuthor) {
        mTvNameAuthor.setText(nameAuthor);
    }

    @Override
    public void setDateCreatedPost(String dateCreatedPost) {
        mTvDatePost.setText(dateCreatedPost);
    }

    @Override
    public void setTitlePost(String titlePost) {
        mTvTitlePost.setText(titlePost);
    }

    @Override
    public void setUrlPostPhoto(String urlPostPhoto) {
        Glide.with(getApplicationContext()).load(urlPostPhoto).into(mIvPostPhoto);
    }

    @Override
    public void setTextPost(String textPost) {
        mTvTextPost.setText(textPost);
    }

    @Override
    public void setLikesCount(String likesCount) {
        mBtnLike.setText(likesCount);
    }

    @Override
    public void setCommentsCount(String commentsCount) {
        mBtnComment.setText(commentsCount);
    }

    @Override
    public void setIsLicked(boolean isLicked) {

    }

    @Override
    public void openListComments(String postIdFromIntent) {
        Intent intent = new Intent(ViewPostActivity.this, ListCommentsActivity.class);
        intent.putExtra(Constants.INTENT_LIST_COMMENTS_POST_ID_VIEW_POST,postIdFromIntent);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comment_view_post_a:
                mPresenter.onCommentButtonClicked(getPostIdFromIntent());
        }
    }
}
