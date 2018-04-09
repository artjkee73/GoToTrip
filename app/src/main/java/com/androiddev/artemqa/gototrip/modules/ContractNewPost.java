package com.androiddev.artemqa.gototrip.modules;

/**
 * Created by artjk on 09.04.2018.
 */

public interface ContractNewPost {
    interface View {

    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady();

        void onButtonAddPostClicked();

        void onPhotoPostClicked();

    }
}
