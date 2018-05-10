package com.androiddev.artemqa.gototrip.modules.newPost;

import java.util.List;

/**
 * Created by artjk on 09.04.2018.
 */

public interface ContractNewPost {
    interface View {
        void choosePhoto(int maxImagePicked);

        void showErrorUploadPhoto();

        void showProgress();

        void hideProgress();

        void showErrorNoPhotoPost();

        void showSuccessAddPost();

        void compressImage(String uriImage);

        void incrementDialog();

        void showSuccessUploadPost();

        void showProgressAddingPost(int size);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void onButtonAddPostClicked(String titlePost, String textPost, List<String> imagePickedUri ,Double latitude , Double longitude , Long postDate );

        void onPhotoPostClicked(int maxImagePicked);

        void addPhoto(byte[] compressPhotoByteArrayOriginal,byte[] compressPhotoByteArrayThumbnail);


        void onFailedUploadPhoto();

        void onSuccessAddPost();

        void onAddPhoto(String photoKey);
    }
}
