package com.androiddev.artemqa.gototrip.modules.listPosts.view;

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
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listPosts.ContractListPosts;
import com.androiddev.artemqa.gototrip.modules.listPosts.presenter.ListPostsPresenter;
import com.androiddev.artemqa.gototrip.modules.search.view.UserViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

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
    public void loadRv(Query queryKey, DatabaseReference refData) {

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setIndexedQuery(queryKey,refData, Post.class)
                        .setLifecycleOwner(this)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
                Glide.with(getApplicationContext()).load(model.getAuthorUriAvatar()).into(holder.mIvAvatarAuthor);
                holder.mTvNameAuthor.setText(model.getAuthorName());
                holder.mTvDatePost.setText(Utils.timestampToDateMessage(model.getDateCreatedLong()));
                holder.mTvTitlePost.setText(model.getTitlePost());
                Glide.with(getApplicationContext()).load(model.getPhotoUrlPost()).into(holder.mIvPostPhoto);
                holder.mTvTextPost.setText(model.getTextPost());
                holder.mBtnLike.setText(String.valueOf(model.getLikeUsers().size()));
                holder.mBtnComment.setText(String.valueOf(model.getComments().size()));
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recucler_view_post_item, parent, false);
                return new PostViewHolder(view);
            }
        };
        mRvPosts.setAdapter(mFirebaseAdapter);
    }
}
