package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnAvatarUserInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnCommentInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnItemInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnLikeInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artemqa on 10.04.2018.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView mIvAvatarAuthor;
    public ImageView mIvPostPhoto;
    public TextView mTvNameAuthor, mTvDatePost, mTvTitlePost, mTvTextPost;
    public Button mBtnLike, mBtnComment;
    private String mCurrentUser;
    private OnAvatarUserInRecyclerViewPostsClickListener mOnAvatarUserInRecyclerViewPostsClickListener;
    private OnCommentInRecyclerViewPostsClickListener mOnCommentInRecyclerViewPostsClickListener;
    private OnItemInRecyclerViewPostsClickListener mOnItemInRecyclerViewPostsClickListener;
    private OnLikeInRecyclerViewPostsClickListener mOnLikeInRecyclerViewPostsClickListener;
    private OnPostPhotoInRecyclerViewPostsClickListener mOnPostPhotoInRecyclerViewPostsClickListener;

    public PostViewHolder(View itemView) {
        super(itemView);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mIvAvatarAuthor = itemView.findViewById(R.id.iv_author_avatar_rv_post_item);
        mIvPostPhoto = itemView.findViewById(R.id.iv_image_post_rv_post_item);
        mTvNameAuthor = itemView.findViewById(R.id.tv_name_author_rv_post_item);
        mTvDatePost = itemView.findViewById(R.id.tv_date_post_rv_post_item);
        mTvTextPost = itemView.findViewById(R.id.tv_text_post_rv_post_item);
        mTvTitlePost = itemView.findViewById(R.id.tv_title_rv_post_item);
        mBtnLike = itemView.findViewById(R.id.btn_like_rv_post_item);
        mBtnComment = itemView.findViewById(R.id.btn_comment_rv_post_item);
    }

    public void bind(final Post item,
                     OnAvatarUserInRecyclerViewPostsClickListener onAvatarUserInRecyclerViewPostsClickListener,
                     OnCommentInRecyclerViewPostsClickListener onCommentInRecyclerViewPostsClickListener,
                     OnItemInRecyclerViewPostsClickListener onItemInRecyclerViewPostsClickListener,
                     OnLikeInRecyclerViewPostsClickListener onLikeInRecyclerViewPostsClickListener,
                     OnPostPhotoInRecyclerViewPostsClickListener onPostPhotoInRecyclerViewPostsClickListener) {

        mOnAvatarUserInRecyclerViewPostsClickListener = onAvatarUserInRecyclerViewPostsClickListener;
        mOnCommentInRecyclerViewPostsClickListener = onCommentInRecyclerViewPostsClickListener;
        mOnItemInRecyclerViewPostsClickListener = onItemInRecyclerViewPostsClickListener;
        mOnLikeInRecyclerViewPostsClickListener = onLikeInRecyclerViewPostsClickListener;
        mOnPostPhotoInRecyclerViewPostsClickListener = onPostPhotoInRecyclerViewPostsClickListener;

        boolean isLike = false;
        if (item.getLikeUsers().containsKey(mCurrentUser)) {
            isLike = true;
        }
        if(isLike){
            mBtnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0, 0, 0);
        }else mBtnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_off, 0, 0, 0);
        Glide.with(itemView.getContext()).load(item.getAuthorUriAvatar()).into(mIvAvatarAuthor);
        mIvAvatarAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnAvatarUserInRecyclerViewPostsClickListener.onAvatarUserClicked(item.getAuthorId());
            }
        });
        mTvNameAuthor.setText(item.getAuthorName());
        mTvDatePost.setText(Utils.timestampToDateMessage(item.getDateCreatedLong()));
        mTvTitlePost.setText(item.getTitlePost());
        Glide.with(itemView.getContext()).load(item.getPhotoUrlPost()).into(mIvPostPhoto);
        mIvPostPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnPostPhotoInRecyclerViewPostsClickListener.onPostPhotoClicked(item.getPhotoUrlPost());
            }
        });
        mTvTextPost.setText(item.getTextPost());
        mBtnLike.setText(String.valueOf(item.getLikeUsers().size()));
        final boolean finalIsLike = isLike;
        mBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnLikeInRecyclerViewPostsClickListener.onLikeClicked(item.getPostId(), finalIsLike);
            }
        });
        mBtnComment.setText(String.valueOf(item.getComments().size()));
        mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCommentInRecyclerViewPostsClickListener.onCommentClicked(item.getPostId());
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemInRecyclerViewPostsClickListener.onItemClicked (item.getPostId());
            }
        });
    }

}
