package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnAvatarUserInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnCommentInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnItemInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnLikeInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.PostImagesLoopingAdapter;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.ViewPostActivity;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.bumptech.glide.Glide;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by artemqa on 10.04.2018.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView mIvAvatarAuthor;
    private AsymmetricGridView mAgvImagesPost;
    private TextView mTvNameAuthor, mTvDatePost, mTvTitlePost, mTvTextPost;
    private Button mBtnLike, mBtnComment;
    private String mCurrentUser;
    private OnAvatarUserInRecyclerViewPostsClickListener mOnAvatarUserInRecyclerViewPostsClickListener;
    private OnCommentInRecyclerViewPostsClickListener mOnCommentInRecyclerViewPostsClickListener;
    private OnItemInRecyclerViewPostsClickListener mOnItemInRecyclerViewPostsClickListener;
    private OnLikeInRecyclerViewPostsClickListener mOnLikeInRecyclerViewPostsClickListener;
    private OnPostPhotoInRecyclerViewPostsClickListener mOnPostPhotoInRecyclerViewPostsClickListener;
    private LoopingViewPager mLvpImagesPost;


    public PostViewHolder(View itemView) {
        super(itemView);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mIvAvatarAuthor = itemView.findViewById(R.id.iv_author_avatar_rv_post_item);
        mTvNameAuthor = itemView.findViewById(R.id.tv_name_author_rv_post_item);
        mTvDatePost = itemView.findViewById(R.id.tv_date_post_rv_post_item);
        mTvTextPost = itemView.findViewById(R.id.tv_text_post_rv_post_item);
        mTvTitlePost = itemView.findViewById(R.id.tv_title_rv_post_item);
        mBtnLike = itemView.findViewById(R.id.btn_like_rv_post_item);
        mBtnComment = itemView.findViewById(R.id.btn_comment_rv_post_item);
        mLvpImagesPost = itemView.findViewById(R.id.lvp_image_post_rv_post_item);
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
        Utils.loadImage(itemView.getContext(),item.getAuthorUriAvatar(),mIvAvatarAuthor);
        mIvAvatarAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnAvatarUserInRecyclerViewPostsClickListener.onAvatarUserClicked(item.getAuthorId());
            }
        });
        mTvNameAuthor.setText(item.getAuthorName());
        mTvDatePost.setText(Utils.timestampToDateMessage(item.getDateCreatedLong()));
        mTvTitlePost.setText(item.getTitlePost());

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

        PostImagesLoopingAdapter mPostImagesLoopingAdapter = new PostImagesLoopingAdapter(itemView.getContext(),item.getPhotos(),true,mOnPostPhotoInRecyclerViewPostsClickListener);
        mLvpImagesPost.setAdapter(mPostImagesLoopingAdapter);
//        Photo firstPhoto =  item.getPhotos().get(0);
//        firstPhoto.setColumnSpan(2);
//        firstPhoto.setRowSpan(2);
//        List<Photo> photos = item.getPhotos();
//        photos.set(0,firstPhoto);
//        mAsymmetricAdapter = new PostImagesAsymmetricGridAdapter(itemView.getContext(),photos);
////        mAgvImagesPost.setDebugging(true);
//        mAgvImagesPost.setAdapter(new AsymmetricGridViewAdapter(itemView.getContext(),mAgvImagesPost , mAsymmetricAdapter));
//        mAgvImagesPost.setRequestedColumnCount(2);
//        mAgvImagesPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mOnPostPhotoInRecyclerViewPostsClickListener.onPostPhotoClicked(item.getPhotos().get(position).getPhotoOriginalUrl());
//            }
//        });

    }
}
