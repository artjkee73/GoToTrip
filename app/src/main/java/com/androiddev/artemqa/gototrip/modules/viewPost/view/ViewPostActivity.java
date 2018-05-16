package com.androiddev.artemqa.gototrip.modules.viewPost.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.listComments.view.ListCommentsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.ListPostsActivity;
import com.androiddev.artemqa.gototrip.modules.listPosts.view.PostImagesAsymmetricGridAdapter;
import com.androiddev.artemqa.gototrip.modules.pickLocation.PickLocationActivity;
import com.androiddev.artemqa.gototrip.modules.viewPhoto.view.ViewPhotoActivity;
import com.androiddev.artemqa.gototrip.modules.viewPost.ContractViewPost;
import com.androiddev.artemqa.gototrip.modules.viewPost.presenter.ViewPostPresenter;
import com.bumptech.glide.Glide;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostActivity extends AppCompatActivity implements ContractViewPost.View, View.OnClickListener {
    private ContractViewPost.Presenter mPresenter;
    private CircleImageView mIvAvatarAuthor;
    private TextView mTvNameAuthor, mTvDatePost, mTvTitlePost, mTvTextPost;
    private Button mBtnLike, mBtnComment;
    private AsymmetricGridView mAgvImagesPost;
    private PostImagesAsymmetricGridAdapter mAsymmetricAdapter;
    private MapView mMvPostLocation;
    private MapboxMap mMapboxMap;

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
        mBtnComment.setOnClickListener(this);
        mAgvImagesPost = findViewById(R.id.agv_image_post_view_post_a);

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
    public void setUrlPostPhoto(List<Photo> urlPostPhoto) {

        Photo firstPhoto = urlPostPhoto.get(0);
        firstPhoto.setColumnSpan(2);
        firstPhoto.setRowSpan(2);
        List<Photo> photos = urlPostPhoto;
        photos.set(0, firstPhoto);
        mAsymmetricAdapter = new PostImagesAsymmetricGridAdapter(ViewPostActivity.this, photos);
        mAgvImagesPost.setAdapter(new AsymmetricGridViewAdapter(ViewPostActivity.this, mAgvImagesPost, mAsymmetricAdapter));
        mAgvImagesPost.setRequestedColumnCount(2);
        mAgvImagesPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onPhotoPostClicked(urlPostPhoto.get(position).getPhotoOriginalUrl());
            }
        });
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
        mMvPostLocation.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMvPostLocation.onSaveInstanceState(outState);
    }
}
