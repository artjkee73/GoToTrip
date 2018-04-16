package com.androiddev.artemqa.gototrip.modules.viewPhoto;

/**
 * Created by artemqa on 16.04.2018.
 */

public interface ContractViewPhoto {
    interface View {
        void setViewPhoto(String viewPhotoUrl);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady(String viewPhotoUrl);
    }
}
