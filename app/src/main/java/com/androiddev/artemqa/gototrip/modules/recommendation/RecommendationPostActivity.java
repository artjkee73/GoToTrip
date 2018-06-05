package com.androiddev.artemqa.gototrip.modules.recommendation;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.listComments.view.ListCommentsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.ContractListPosts;
import com.androiddev.artemqa.gototrip.modules.listPosts.presenter.ListPostsPresenter;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.ListPostsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.PostsRecyclerAdapter;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnAvatarUserInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnCommentInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnItemInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnLikeInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.view.ViewPhotoActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.ViewPostActivity;
import com.androiddev.artemqa.gototrip.modules.viewProfile.view.ViewProfileActivity;

public class RecommendationPostActivity extends AppCompatActivity implements RecommendationContract.View {
    private RecyclerView mRvPosts;
    private RecommendationContract.Presenter mPresenter;
    private PostsRecyclerAdapter mPostsRecyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mPbLoafItems;
    private boolean isLoadingOldItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_posts);
        initView();
    }

    private void initView() {
        mPresenter = new RecommendationPresenter();
        mPresenter.attachView(this);
        mPbLoafItems = findViewById(R.id.pb_load_items_list_post_a);
        mSwipeRefreshLayout = findViewById(R.id.srl_list_posts_a);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.loadNewDataForRV(getViewUserIdFromIntent(), mPostsRecyclerAdapter.getFirstItemId());
            }
        });
        mRvPosts = findViewById(R.id.rv_posts_posts_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvPosts.setLayoutManager(mLinearLayoutManager);
        mPresenter.viewIsReady(getViewUserIdFromIntent());
        mPostsRecyclerAdapter = new PostsRecyclerAdapter();
        mPostsRecyclerAdapter.setOnAvatarUserInRecyclerViewPostsClickListener(new OnAvatarUserInRecyclerViewPostsClickListener() {
            @Override
            public void onAvatarUserClicked(String authorId) {
                mPresenter.onAvatarUserClicked(authorId);
            }
        });
        mPostsRecyclerAdapter.setOnCommentInRecyclerViewPostsClickListener(new OnCommentInRecyclerViewPostsClickListener() {
            @Override
            public void onCommentClicked(String postId) {
                mPresenter.onCommentClicked(postId);
            }
        });
        mPostsRecyclerAdapter.setOnLikeInRecyclerViewPostsClickListener(new OnLikeInRecyclerViewPostsClickListener() {
            @Override
            public void onLikeClicked(String postId, boolean isLike) {
                mPresenter.onLikeClicked(postId, isLike);
            }
        });
        mPostsRecyclerAdapter.setOnPostPhotoInRecyclerViewPostsClickListener(new OnPostPhotoInRecyclerViewPostsClickListener() {
            @Override
            public void onPostPhotoClicked(String photoUrlPost) {
                mPresenter.onPostPhotoClicked(photoUrlPost);
            }
        });
        mPostsRecyclerAdapter.setOnItemInRecyclerViewPostsClickListener(new OnItemInRecyclerViewPostsClickListener() {
            @Override
            public void onItemClicked(String postId) {
                mPresenter.onItemRvClicked(postId);
            }
        });
        mRvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLinearLayoutManager.getItemCount();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition() + 1;
                if (!isLoadingOldItem && totalItemCount > 2 && totalItemCount <= lastVisibleItemPosition) {
                    isLoadingOldItem = true;
                    mPbLoafItems.setVisibility(View.VISIBLE);
                    mPresenter.loadOldItemsInRv(getViewUserIdFromIntent(), mPostsRecyclerAdapter.getLastItemId());
                }
            }
        });

        mRvPosts.setAdapter(mPostsRecyclerAdapter);

    }

    private String getViewUserIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_USER_ID);
    }

    @Override
    public void openViewPost(String postId) {
        Intent intent = new Intent(RecommendationPostActivity.this, ViewPostActivity.class);
        intent.putExtra(Constants.INTENT_POST_ID, postId);
        startActivity(intent);
    }

    @Override
    public void openListComments(String postId) {
        Intent intent = new Intent(RecommendationPostActivity.this, ListCommentsActivity.class);
        intent.putExtra(Constants.INTENT_POST_ID, postId);
        startActivity(intent);
    }

    @Override
    public void openViewProfile(String authorId) {
        Intent intent = new Intent(RecommendationPostActivity.this, ViewProfileActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, authorId);
        startActivity(intent);
    }

    @Override
    public void openViewPhoto(String photoUrlPost) {
        Intent intent = new Intent(RecommendationPostActivity.this, ViewPhotoActivity.class);
        intent.putExtra(Constants.INTENT_PHOTO_URL, photoUrlPost);
        startActivity(intent);
    }

    @Override
    public void setInitialDataInRv(Post post) {
        mPostsRecyclerAdapter.addOrUpdateItem(post);
    }

    @Override
    public void setNewDataForRv(Post post) {
        mSwipeRefreshLayout.setRefreshing(false);
        mPostsRecyclerAdapter.addOrUpdateNewItem(post);
    }

    @Override
    public void setOldDataInRv(Post post) {
        mPostsRecyclerAdapter.addOrUpdateItem(post);
        mPbLoafItems.setVisibility(View.GONE);
        isLoadingOldItem = false;
    }

    @Override
    public void showNoNewDataForRv() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoOldDataForRv() {
        mRvPosts.clearOnScrollListeners();
        isLoadingOldItem = false;
        mPbLoafItems.setVisibility(View.GONE);
        Toast.makeText(RecommendationPostActivity.this,R.string.toast_end_old_messages_dialog_a,Toast.LENGTH_SHORT).show();
    }
}
