package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by artjk on 19.04.2018.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<Post> mPosts;

    public PostsRecyclerAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder,final int position) {
        boolean isLike = false;
        if (mPosts.get(position).getLikeUsers().containsKey(currentUserId)) {
            isLike = true;
            holder.mBtnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0, 0, 0);
        }
        Glide.with(holder.itemView.getContext()).load(mPosts.get(position).getAuthorUriAvatar()).into(holder.mIvAvatarAuthor);
        holder.mIvAvatarAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onAvatarUserClicked(mPosts.get(position).getAuthorId());
            }
        });
        holder.mTvNameAuthor.setText(mPosts.get(position).getAuthorName());
        holder.mTvDatePost.setText(Utils.timestampToDateMessage(mPosts.get(position).getDateCreatedLong()));
        holder.mTvTitlePost.setText(mPosts.get(position).getTitlePost());
        Glide.with(holder.itemView.getContext()).load(mPosts.get(position).getPhotoUrlPost()).into(holder.mIvPostPhoto);
        holder.mIvPostPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onPostPhotoClicked(mPosts.get(position).getPhotoUrlPost());
            }
        });
        holder.mTvTextPost.setText(mPosts.get(position).getTextPost());
        holder.mBtnLike.setText(String.valueOf(mPosts.get(position).getLikeUsers().size()));
        final boolean finalIsLike = isLike;
        holder.mBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onLikeClicked(mPosts.get(position).getPostId(), finalIsLike);
            }
        });
        holder.mBtnComment.setText(String.valueOf(mPosts.get(position).getComments().size()));
        holder.mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onCommentClicked(mPosts.get(position).getPostId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onItemRvClicked(mPosts.get(position).getPostId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
