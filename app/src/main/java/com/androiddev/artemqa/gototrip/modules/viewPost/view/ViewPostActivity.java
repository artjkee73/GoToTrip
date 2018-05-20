package com.androiddev.artemqa.gototrip.modules.viewPost.view;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listComments.view.ListCommentsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.interfaces.OnPostPhotoInRecyclerViewPostsClickListener;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.view.ViewPhotoActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.ContractViewPost;
import com.androiddev.artemqa.gototrip.modules.viewPost.presenter.ViewPostPresenter;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostActivity extends AppCompatActivity implements ContractViewPost.View, View.OnClickListener {
    private ContractViewPost.Presenter mPresenter;
    private CircleImageView mIvAvatarAuthor;
    private TextView mTvNameAuthor, mTvDatePost, mTvTitlePost, mTvTextPost;
    private Button mBtnLike, mBtnComment,mBtnView;
    private MapView mMvPostLocation;
    private MapboxMap mMapboxMap;
    private LoopingViewPager mLvpImagesPost;
    private PostImagesLoopingAdapter mPostImagesLoopingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mPresenter = new ViewPostPresenter();
        mPresenter.attachView(this);
        mMvPostLocation = findViewById(R.id.mv_current_post_map_view_post_a);
        mMvPostLocation.onCreate(savedInstanceState);
        mMvPostLocation.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mPresenter.viewIsReady(getPostIdFromIntent());
            }
        });
        mIvAvatarAuthor = findViewById(R.id.iv_author_avatar_view_post_a);
        mTvNameAuthor = findViewById(R.id.tv_name_author_view_post_a);
        mTvDatePost = findViewById(R.id.tv_date_post_view_post_a);
        mTvTextPost = findViewById(R.id.tv_text_post_view_post_a);
        mTvTitlePost = findViewById(R.id.tv_title_view_post_a);
        mBtnLike = findViewById(R.id.btn_like_view_post_a);
        mBtnLike.setOnClickListener(this);
        mBtnComment = findViewById(R.id.btn_comment_view_post_a);
        mLvpImagesPost = findViewById(R.id.lvp_image_post_view_post_a);
        mBtnComment.setOnClickListener(this);
        mBtnView = findViewById(R.id.btn_view_view_post_a);
    }

    private String getPostIdFromIntent() {
        return getIntent().getStringExtra(Constants.INTENT_POST_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMvPostLocation.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void setAvatarAuthor(String urlAvatarAuthor) {
        Utils.loadImage(ViewPostActivity.this, urlAvatarAuthor, mIvAvatarAuthor);
    }

    @Override
    public void setNameAuthor(String nameAuthor) {
        mTvNameAuthor.setText(nameAuthor);
    }

    @Override
    public void setDateCreatedPost(String dateCreatedPost) {
        mTvDatePost.setText(dateCreatedPost);
    }

    @Override
    public void setTitlePost(String titlePost) {
        mTvTitlePost.setText(titlePost);
    }

    @Override
    public void setUrlPostPhoto(ArrayList<Photo> urlPostPhoto) {
        mPostImagesLoopingAdapter = new PostImagesLoopingAdapter(ViewPostActivity.this, urlPostPhoto, true, new OnPostPhotoInRecyclerViewPostsClickListener() {
            @Override
            public void onPostPhotoClicked(String photoUrlPost) {
                mPresenter.onPhotoPostClicked(photoUrlPost);
            }
        });
        mLvpImagesPost.setAdapter(mPostImagesLoopingAdapter);

    }

    @Override
    public void setTextPost(String textPost) {
        mTvTextPost.setText(textPost);
    }

    @Override
    public void setLikesCount(String likesCount) {
        mBtnLike.setText(likesCount);
    }

    @Override
    public void setCommentsCount(String commentsCount) {
        mBtnComment.setText(commentsCount);
    }

    @Override
    public void setIsLiked(boolean isLiked) {
        if (isLiked) {
            mBtnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0, 0, 0);
        } else {
            mBtnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_off, 0, 0, 0);
        }


    }

    @Override
    public void openListComments(String postIdFromIntent) {
        Intent intent = new Intent(ViewPostActivity.this, ListCommentsActivity.class);
        intent.putExtra(Constants.INTENT_POST_ID, postIdFromIntent);
        startActivity(intent);
    }

    @Override
    public void updateUI() {
        mPresenter.viewIsReady(getPostIdFromIntent());
    }

    @Override
    public void openViewPhoto(String photoUrlPost) {
        Intent intent = new Intent(ViewPostActivity.this, ViewPhotoActivity.class);
        intent.putExtra(Constants.INTENT_PHOTO_URL, photoUrlPost);
        startActivity(intent);
    }

    @Override
    public void setPostLocation(Double latitudeMap, Double longitudeMap, String titlePost) {
        LatLng positionMarker = new LatLng(latitudeMap,longitudeMap);
        Icon iconMaker = Utils.iconFromResource(ViewPostActivity.this,R.drawable.location_circle);
        if (mMapboxMap != null) {
            mMapboxMap.addMarker(new MarkerOptions().position(positionMarker).setTitle(titlePost).setIcon(iconMaker));
            CameraPosition position = new CameraPosition.Builder()
                    .target(positionMarker)
                    .zoom(14)
                    .build();

            mMapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        }
    }

    @Override
    public void setViewPost(int viewCount) {
        mBtnView.setText(String.valueOf(viewCount));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_comment_view_post_a:
                mPresenter.onCommentButtonClicked(getPostIdFromIntent());
                break;
            case R.id.btn_like_view_post_a:
                mPresenter.onLikeButtonClicked(getPostIdFromIntent());
                break;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMvPostLocation.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMvPostLocation.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLvpImagesPost.pauseAutoScroll();
        mMvPostLocation.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMvPostLocation.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLvpImagesPost.resumeAutoScroll();
        mMvPostLocation.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMvPostLocation.onSaveInstanceState(outState);
    }
}
