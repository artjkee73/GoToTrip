package com.androiddev.artemqa.gototrip.modules.newPost;

/**
 * Created by artjk on 09.04.2018.
 */

public interface ContractNewPost {
    interface View {
        void choosePhoto();

        void showErrorUploadPhoto();

        void showProgress();

        void hideProgress();

        void showErrorNoPhotoPost();

        void showSuccessAddPost();

        void setPostPhoto(String urlPhoto);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void onButtonAddPostClicked(String titlePost,String textPost);

        void onPhotoPostClicked();

        void savePhoto(byte[] compressPhotoByteArray);

        void onSuccessUploadPhoto(String s, String postKey);

        void onFailedUploadPhoto();

        void onSuccessAddPost();
    }
}
