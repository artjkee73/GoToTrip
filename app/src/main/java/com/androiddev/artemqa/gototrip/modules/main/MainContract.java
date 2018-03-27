package com.androiddev.artemqa.gototrip.modules.main;

import com.androiddev.artemqa.gototrip.common.models.User;

/**
 * Created by artemqa on 16.03.2018.
 */

public interface MainContract {
    interface View{
        void showProgress();

        void hideProgress();

        void setUserInformationOnNavDrawer(String name , String email , String urlPhoto);

        void openEditProfile();

        void openSearch();
    }

    interface Presenter{

        void attachView(View view);

        void detachView();

        void viewIsReady();

        void onClickProfileItem();

        void onClickSearchItem();

        void onGettingUser(User currentUser);
    }
}
