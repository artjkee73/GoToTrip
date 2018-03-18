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
    }

    interface Presenter{

        void attachView(View view);

        void detachView();

        void viewIsReady();

        void onClickProfileItem();

        void onGettingUser(User currentUser);
    }
}
