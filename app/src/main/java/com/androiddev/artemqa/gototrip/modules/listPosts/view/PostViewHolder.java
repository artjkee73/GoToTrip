package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artemqa on 10.04.2018.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView mIvAvatarAuthor;
    public ImageView mIvPostPhoto;
    public TextView mTvNameAuthor, mTvDatePost, mTvTitlePost, mTvTextPost;
    public Button mBtnLike, mBtnComment;

    public PostViewHolder(View itemView) {
        super(itemView);
        mIvAvatarAuthor = itemView.findViewById(R.id.iv_author_avatar_rv_post_item);
        mIvPostPhoto = itemView.findViewById(R.id.iv_image_post_rv_post_item);
        mTvNameAuthor = itemView.findViewById(R.id.tv_name_author_rv_post_item);
        mTvDatePost = itemView.findViewById(R.id.tv_date_post_rv_post_item);
        mTvTextPost = itemView.findViewById(R.id.tv_text_post_rv_post_item);
        mTvTitlePost = itemView.findViewById(R.id.tv_title_rv_post_item);
        mBtnLike = itemView.findViewById(R.id.btn_like_rv_post_item);
        mBtnComment = itemView.findViewById(R.id.btn_comment_rv_post_item);
    }
}
