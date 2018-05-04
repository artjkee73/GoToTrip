package com.androiddev.artemqa.gototrip.modules.newPost.presenter;

import com.androiddev.artemqa.gototrip.modules.newPost.ContractNewPost;

/**
 * Created by artjk on 09.04.2018.
 */

public class NewPostPresenter implements ContractNewPost.Presenter {
    private ContractNewPost.View mView;
    private NewPostInteractor mInteractor;
    private String mUrlUploadPostPhoto;
    private String mPostKey;

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
    public void onButtonAddPostClicked(String titlePost, String textPost) {
        if (mUrlUploadPostPhoto != null && mPostKey != null) {
            mInteractor.addPost(titlePost, textPost, mUrlUploadPostPhoto, mPostKey);
        } else {
            mView.showErrorNoPhotoPost();
        }


    }

    @Override
    public void onPhotoPostClicked(int maxImagePicked) {
        mView.choosePhoto(maxImagePicked);
    }

    @Override
    public void savePhoto(byte[] compressorPhotoByteArray) {
        mInteractor.setPhotoPost(compressorPhotoByteArray);
        mView.showProgress();
    }

    @Override
    public void onSuccessUploadPhoto(String photoUrl, String postKey) {
        mPostKey = postKey;
        mUrlUploadPostPhoto = photoUrl;
        mView.hideProgress();
        mView.setPostPhoto(photoUrl);

    }

    @Override
    public void onFailedUploadPhoto() {
        mView.hideProgress();
        mView.showErrorUploadPhoto();
    }

    @Override
    public void onSuccessAddPost() {
        mView.showSuccessAddPost();
        mPostKey = null;
        mUrlUploadPostPhoto = null;
    }
}
