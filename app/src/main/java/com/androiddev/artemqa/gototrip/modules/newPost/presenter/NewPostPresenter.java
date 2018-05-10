package com.androiddev.artemqa.gototrip.modules.newPost.presenter;

import com.androiddev.artemqa.gototrip.modules.newPost.ContractNewPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artjk on 09.04.2018.
 */

public class NewPostPresenter implements ContractNewPost.Presenter {
    private ContractNewPost.View mView;
    private NewPostInteractor mInteractor;
    private String mTitlePost;
    private String mTextPost;
    private List<String> mImagePickedUri;
    private Double mLatitudeLoc;
    private Double mLongitudeLoc;
    private Long mPostDate;
    private List<String> mPhotoUploadedId = new ArrayList<>();

    public NewPostPresenter() {
        mInteractor = new NewPostInteractor(this);
    }

    @Override
    public void attachView(ContractNewPost.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onButtonAddPostClicked(String titlePost, String textPost, List<String> imagePickedUri, Double latitude, Double longitude, Long postDate) {
        mTitlePost = titlePost;
        mTextPost = textPost;
        mImagePickedUri = imagePickedUri;
        mLatitudeLoc = latitude;
        mLongitudeLoc = longitude;
        mPostDate = postDate;
        mView.showProgressAddingPost(mImagePickedUri.size());
        uploadImagePost(imagePickedUri);
    }

    private void uploadImagePost(List<String> imagePickedUri) {
        for (String imageUriUpload : imagePickedUri) {
            mView.compressImage(imageUriUpload);
        }
    }


    @Override
    public void onPhotoPostClicked(int maxImagePicked) {
        mView.choosePhoto(maxImagePicked);
    }

    @Override
    public void addPhoto(byte[] compressPhotoByteArrayOriginal, byte[] compressPhotoByteArrayThumbnail) {
        mInteractor.addPhoto(compressPhotoByteArrayOriginal, compressPhotoByteArrayThumbnail);
    }


    @Override
    public void onFailedUploadPhoto() {
        mView.hideProgress();
        mView.showErrorUploadPhoto();
    }

    @Override
    public void onSuccessAddPost() {
        mView.showSuccessUploadPost();
    }

    @Override
    public void onAddPhoto(String photoKey) {
        mPhotoUploadedId.add(photoKey);
        if(mPhotoUploadedId.size() == mImagePickedUri.size()){
            mInteractor.addPost(mTitlePost,mTextPost,mPhotoUploadedId ,mLatitudeLoc , mLongitudeLoc , mPostDate);
        }else {
            mView.incrementDialog();
        }
    }
}
