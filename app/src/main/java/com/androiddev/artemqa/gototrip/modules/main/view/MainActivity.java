package com.androiddev.artemqa.gototrip.modules.main.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.chat.view.ChatActivity;
import com.androiddev.artemqa.gototrip.modules.editProfile.view.EditProfileActivity;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.modules.listComments.view.ListCommentsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.ListPostsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.PostsRecyclerAdapter;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnAvatarUserInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnCommentInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnItemInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnLikeInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;
import com.androiddev.artemqa.gototrip.modules.main.presenter.MainPresenter;
import com.androiddev.artemqa.gototrip.modules.newPost.view.NewPostActivity;
import com.androiddev.artemqa.gototrip.modules.search.view.SearchActivity;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.view.ViewPhotoActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.ViewPostActivity;
import com.androiddev.artemqa.gototrip.modules.viewProfile.view.ViewProfileActivity;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;
    MainContract.Presenter mPresenter;
    RecyclerView mRvFeedPosts;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mPbLoafItems;
    LinearLayoutManager mLinearLayoutManager;
    PostsRecyclerAdapter mPostsRecyclerAdapter;
    boolean isLoadingOldItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initForm();
    }


    private void initForm() {
        mDrawerLayout = findViewById(R.id.drawer_layout_main_a);
        mNavigationView = findViewById(R.id.nav_view_main_a);
        mRvFeedPosts = findViewById(R.id.rv_feed_posts_main_a);
        setToggleButtonInNavDrawer();
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mPbLoafItems = findViewById(R.id.pb_load_items_main_a);
        mSwipeRefreshLayout = findViewById(R.id.srl_list_posts_main_a);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.loadNewDataForRV(mPostsRecyclerAdapter.getFirstItemId());
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvFeedPosts.setLayoutManager(mLinearLayoutManager);
        mPresenter.viewIsReady();
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
        mRvFeedPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLinearLayoutManager.getItemCount();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition() + 1;
                if (!isLoadingOldItem && totalItemCount > 2 && totalItemCount <= lastVisibleItemPosition) {
                    isLoadingOldItem = true;
                    mPbLoafItems.setVisibility(View.VISIBLE);
                    mPresenter.loadOldItemsInRv(mPostsRecyclerAdapter.getLastItemId());
                }
            }
        });

        mRvFeedPosts.setAdapter(mPostsRecyclerAdapter);



        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        mPresenter.viewIsReady();


    }

    private void setToggleButtonInNavDrawer() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.toggle_open_main_a, R.string.toggle_close_main_a);
        mDrawerLayout.addDrawerListener(mToggle);
        mNavigationView = findViewById(R.id.nav_view_main_a);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.profile_nav_main :
                mPresenter.onClickProfileItem();
                break;
            case R.id.search_nav_main :
                mPresenter.onClickSearchItem();
                break;
            case R.id.chat_nav_main :
                mPresenter.onClickChatItem();
                break;
            case R.id.new_post_nav_main :
                mPresenter.onClickNewPostItem();
                break;
            case R.id.settings_nav_main :

                break;
            case R.id.log_out_nav_main :

                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUserInformationOnNavDrawer(String name, String email, String urlPhoto) {
        View headerNavDrawer =  mNavigationView.getHeaderView(0);
        TextView tvName = headerNavDrawer.findViewById(R.id.tv_name_header_nav_drawer);
        TextView tvEmail = headerNavDrawer.findViewById(R.id.tv_email_header_nav_drawer);
        CircleImageView ivAvatar = headerNavDrawer.findViewById(R.id.iv_header_nav_drawer);
        Utils.loadImage(MainActivity.this,urlPhoto,ivAvatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onAvatarClicked();
            }
        });
        tvName.setText(name);
        tvEmail.setText(email);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void openEditProfile() {
        Intent intent = new Intent(MainActivity.this,EditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void openSearch() {
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void openChat() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void openNewPost() {
        Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void openViewProfile(String currentUserId) {
        Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID,currentUserId);
        startActivity(intent);
    }

    @Override
    public void openViewPost(String postId) {
        Intent intent = new Intent(MainActivity.this, ViewPostActivity.class);
        intent.putExtra(Constants.INTENT_POST_ID,postId);
        startActivity(intent);
    }

    @Override
    public void openListComments(String postId) {
        Intent intent = new Intent(MainActivity.this, ListCommentsActivity.class);
        intent.putExtra(Constants.INTENT_POST_ID,postId);
        startActivity(intent);
    }

    @Override
    public void openViewPhoto(String photoUrlPost) {
        Intent intent = new Intent(MainActivity.this,ViewPhotoActivity.class);
        intent.putExtra(Constants.INTENT_PHOTO_URL,photoUrlPost);
        startActivity(intent);
    }

    @Override
    public void setInitialDataInRv(Post post) {
        mPostsRecyclerAdapter.addOrUpdateItem(post);
    }

    @Override
    public void setOldDataInRv(Post post) {
        mPostsRecyclerAdapter.addOrUpdateItem(post);
        mPbLoafItems.setVisibility(View.GONE);
        isLoadingOldItem = false;
    }

    @Override
    public void showNoOldDataForRv() {
        mRvFeedPosts.clearOnScrollListeners();
        isLoadingOldItem = false;
        mPbLoafItems.setVisibility(View.GONE);
        Toast.makeText(MainActivity.this,R.string.toast_end_old_messages_dialog_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNewDataForRv(Post post) {
        mSwipeRefreshLayout.setRefreshing(false);
        mPostsRecyclerAdapter.addOrUpdateNewItem(post);
    }

    @Override
    public void showNoNewDataForRv() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
