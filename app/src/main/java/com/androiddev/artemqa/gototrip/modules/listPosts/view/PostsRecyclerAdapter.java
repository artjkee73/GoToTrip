package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnAvatarUserInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnCommentInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnItemInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnLikeInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artjk on 19.04.2018.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<Post> mPosts = new ArrayList<>();
    private OnAvatarUserInRecyclerViewPostsClickListener mOnAvatarUserInRecyclerViewPostsClickListener;
    private OnCommentInRecyclerViewPostsClickListener mOnCommentInRecyclerViewPostsClickListener;
    private OnItemInRecyclerViewPostsClickListener mOnItemInRecyclerViewPostsClickListener;
    private OnLikeInRecyclerViewPostsClickListener mOnLikeInRecyclerViewPostsClickListener;
    private OnPostPhotoInRecyclerViewPostsClickListener mOnPostPhotoInRecyclerViewPostsClickListener;

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(mPosts.get(position),
                mOnAvatarUserInRecyclerViewPostsClickListener,
                mOnCommentInRecyclerViewPostsClickListener,
                mOnItemInRecyclerViewPostsClickListener,
                mOnLikeInRecyclerViewPostsClickListener,
                mOnPostPhotoInRecyclerViewPostsClickListener);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void setOnAvatarUserInRecyclerViewPostsClickListener(OnAvatarUserInRecyclerViewPostsClickListener onAvatarUserInRecyclerViewPostsClickListener) {
        mOnAvatarUserInRecyclerViewPostsClickListener = onAvatarUserInRecyclerViewPostsClickListener;
    }

    public void setOnCommentInRecyclerViewPostsClickListener(OnCommentInRecyclerViewPostsClickListener onCommentInRecyclerViewPostsClickListener) {
        mOnCommentInRecyclerViewPostsClickListener = onCommentInRecyclerViewPostsClickListener;
    }

    public void setOnItemInRecyclerViewPostsClickListener(OnItemInRecyclerViewPostsClickListener onItemInRecyclerViewPostsClickListener) {
        mOnItemInRecyclerViewPostsClickListener = onItemInRecyclerViewPostsClickListener;
    }

    public void setOnLikeInRecyclerViewPostsClickListener(OnLikeInRecyclerViewPostsClickListener onLikeInRecyclerViewPostsClickListener) {
        mOnLikeInRecyclerViewPostsClickListener = onLikeInRecyclerViewPostsClickListener;
    }

    public void setOnPostPhotoInRecyclerViewPostsClickListener(OnPostPhotoInRecyclerViewPostsClickListener onPostPhotoInRecyclerViewPostsClickListener) {
        mOnPostPhotoInRecyclerViewPostsClickListener = onPostPhotoInRecyclerViewPostsClickListener;
    }

    public void addOrUpdateItem(Post item) {
        Post containsPost = null;

        if (!mPosts.isEmpty()) {
            for (Post post : mPosts) {
                if (post.getPostId().equals(item.getPostId())) {
                    containsPost = post;
                }
            }
            if (containsPost != null) {
                mPosts.set(mPosts.indexOf(containsPost), item);
                notifyDataSetChanged();
            } else {
                mPosts.add(item);
                notifyItemInserted(mPosts.size() - 1);
            }
        } else {
            mPosts.add(item);
            notifyItemInserted(mPosts.size() - 1);
        }
    }

    public String getLastItemId() {
        return mPosts.get(mPosts.size() - 1).getPostId();
    }

    public String getFirstItemId() {
        return mPosts.get(0).getPostId();
    }

    public void addOrUpdateNewItem(Post newPost) {
        Post containsPost = null;
        for (Post post : mPosts) {
            if (post.getPostId().equals(newPost.getPostId())) {
                containsPost = post;
            }
        }
        if (containsPost != null) {
            mPosts.set(mPosts.indexOf(containsPost), newPost);
            notifyDataSetChanged();
        } else {
            mPosts.add(0,newPost);
            notifyItemInserted(0);
        }
    }
}
