package com.androiddev.artemqa.gototrip.modules.listPosts.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.ContractListPosts;
import com.androiddev.artemqa.gototrip.modules.listPosts.presenter.ListPostsPresenter;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.ViewPostActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class ListPostsActivity extends AppCompatActivity implements ContractListPosts.View {
    private RecyclerView mRvPosts;
    private ContractListPosts.Presenter mPresenter;
    private FirebaseRecyclerAdapter<Post,PostViewHolder> mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_posts);
        initView();
    }

    private void initView() {
        mPresenter = new ListPostsPresenter();
        mPresenter.attachView(this);
        mRvPosts = findViewById(R.id.rv_posts_posts_list);
        mRvPosts.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.viewIsReady(getViewUserIdFromIntent());
    }

    private String getViewUserIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_LIST_POSTS_USER_ID_VIEW_PROFILE);
    }


    @Override
    public void loadRv(Query queryKey, DatabaseReference refData, final String currentUserId) {

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setIndexedQuery(queryKey,refData, Post.class)
                        .setLifecycleOwner(this)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull final Post model) {
                boolean isLike = false;
                if(model.getLikeUsers().containsKey(currentUserId)){
                    isLike = true;
                    holder.mBtnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0, 0, 0);
                }
                Glide.with(getApplicationContext()).load(model.getAuthorUriAvatar()).into(holder.mIvAvatarAuthor);
                holder.mTvNameAuthor.setText(model.getAuthorName());
                holder.mTvDatePost.setText(Utils.timestampToDateMessage(model.getDateCreatedLong()));
                holder.mTvTitlePost.setText(model.getTitlePost());
                Glide.with(getApplicationContext()).load(model.getPhotoUrlPost()).into(holder.mIvPostPhoto);
                holder.mTvTextPost.setText(model.getTextPost());
                holder.mBtnLike.setText(String.valueOf(model.getLikeUsers().size()));
                final boolean finalIsLike = isLike;
                holder.mBtnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onLikeClicked(model.getPostId(), finalIsLike);
                    }
                });
                holder.mBtnComment.setText(String.valueOf(model.getComments().size()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onItemRvClicked(model.getPostId());
                    }
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_post_item, parent, false);
                return new PostViewHolder(view);
            }
        };
        mRvPosts.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void openViewPost(String postId) {
        Intent intent = new Intent(ListPostsActivity.this, ViewPostActivity.class);
        intent.putExtra(Constants.INTENT_VIEW_POST_POST_ID_LIST_POSTS,postId);
        startActivity(intent);
    }
}
