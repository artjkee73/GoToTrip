package com.androiddev.artemqa.gototrip.modules.viewPhoto.presenter;

import com.androiddev.artemqa.gototrip.modules.viewPhoto.ContractViewPhoto;

/**
 * Created by artemqa on 16.04.2018.
 */

public class ViewPhotoPresenter implements ContractViewPhoto.Presenter {
    private ContractViewPhoto.View mView;
    @Override
    public void attachView(ContractViewPhoto.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(String viewPhotoUrl) {
        mView.setViewPhoto(viewPhotoUrl);
    }

}
