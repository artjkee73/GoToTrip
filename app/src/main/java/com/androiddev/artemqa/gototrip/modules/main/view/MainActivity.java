package com.androiddev.artemqa.gototrip.modules.main.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.chat.view.ChatActivity;
import com.androiddev.artemqa.gototrip.modules.editProfile.view.EditProfileActivity;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.modules.listComments.view.ListCommentsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.ListPostsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.PostViewHolder;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;
import com.androiddev.artemqa.gototrip.modules.main.presenter.MainPresenter;
import com.androiddev.artemqa.gototrip.modules.newPost.view.NewPostActivity;
import com.androiddev.artemqa.gototrip.modules.search.view.SearchActivity;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.view.ViewPhotoActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.view.ViewPostActivity;
import com.androiddev.artemqa.gototrip.modules.viewProfile.view.ViewProfileActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;
    MainContract.Presenter mPresenter;
    RecyclerView mRvFeedPosts;
    FirebaseRecyclerAdapter<Post,PostViewHolder> mFirebaseAdapter;


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
        mRvFeedPosts.setLayoutManager(new LinearLayoutManager(this));
        setToggleButtonInNavDrawer();
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
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onAvatarCliked();
            }
        });
        if( urlPhoto != null){
            Glide.with(getApplicationContext()).load(urlPhoto).into(ivAvatar);
        }
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
                holder.mIvAvatarAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onAvatarUserClicked(model.getAuthorId());
                    }
                });
                holder.mTvNameAuthor.setText(model.getAuthorName());
                holder.mTvDatePost.setText(Utils.timestampToDateMessage(model.getDateCreatedLong()));
                holder.mTvTitlePost.setText(model.getTitlePost());
                Glide.with(getApplicationContext()).load(model.getPhotoUrlPost()).into(holder.mIvPostPhoto);
                holder.mIvPostPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onPostPhotoClicked(model.getPhotoUrlPost());
                    }
                });
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
                holder.mBtnComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.onCommentClicked(model.getPostId());
                    }
                });
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
        mRvFeedPosts.setAdapter(mFirebaseAdapter);
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
}
