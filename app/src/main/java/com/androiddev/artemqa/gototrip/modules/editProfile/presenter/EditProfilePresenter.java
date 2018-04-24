package com.androiddev.artemqa.gototrip.modules.editProfile.presenter;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.modules.editProfile.EditProfileContract;
/**
 * Created by artemqa on 16.03.2018.
 */

public class EditProfilePresenter implements EditProfileContract.Presenter {
    private EditProfileContract.View mView;
    private EditProfileInteractor mInteractor;

    public EditProfilePresenter() {
        mInteractor = new EditProfileInteractor(this);
    }

    @Override
    public void onBtnSaveClicked(String name, String country,String city,String aboutMe,String visitedCountries) {
        mInteractor.setUserInfo(name,country,city,aboutMe,visitedCountries);

    }


    @Override
    public void attachView(EditProfileContract.View view) {
        mView = view;

    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady() {
        mInteractor.getUserInformation();
    }

    @Override
    public void onLoadUser(User currentUser) {
        mView.updateUI(currentUser);

    }

    @Override
    public void saveAvatar(byte[] compressOriginalPhotoByteArray, byte[] compressThumbnailPhotoByteArray)  {
        mInteractor.setUserAvatar(compressOriginalPhotoByteArray,compressThumbnailPhotoByteArray);
        mView.showProgress();
    }

    @Override
    public void onSuccessUploadPhoto(String urlUserPhotoOriginal, String urlUserPhotoThumbnail) {
        mInteractor.setUserUrlPhoto(urlUserPhotoOriginal , urlUserPhotoThumbnail);
        mView.hideProgress();
    }

    @Override
    public void onFailedUploadPhoto() {
        mView.hideProgress();
        mView.showErrorUploadPhoto();

    }

    @Override
    public void onSuccessSetUserInfo() {
        mView.showSuccessSetUserInfo();
    }

}