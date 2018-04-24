package com.androiddev.artemqa.gototrip.modules.editProfile;

import com.androiddev.artemqa.gototrip.common.models.User;

/**
 * Created by artemqa on 16.03.2018.
 */

public interface EditProfileContract {
    interface View {
        void showProgress();

        void hideProgress();

        void updateUI(User currentUser);

        void showErrorUploadPhoto();

        void showSuccessSetUserInfo();
    }

    interface Presenter {
        void onBtnSaveClicked(String name,String city,String country,String aboutMe,String visitedCountries);

        void attachView(View view);

        void detachView();

        void viewIsReady();

        void onLoadUser(User currentUser);

        void saveAvatar(byte[] compressPhotoByteArray, byte[] compressThumbnailPhotoByteArray);

        void onSuccessUploadPhoto(String urlUserPhoto, String s);

        void onFailedUploadPhoto();

        void onSuccessSetUserInfo();
    }
}
