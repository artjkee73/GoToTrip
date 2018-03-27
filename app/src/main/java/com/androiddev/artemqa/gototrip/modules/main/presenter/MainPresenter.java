package com.androiddev.artemqa.gototrip.modules.main.presenter;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;

/**
 * Created by artemqa on 16.03.2018.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private MainInteractor mInteractor;

    public MainPresenter() {
        mInteractor = new MainInteractor(this);
    }

    @Override
    public void attachView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady() {
        mInteractor.getUserFromDB();
    }

    @Override
    public void onClickProfileItem() {
        mView.openEditProfile();
    }

    @Override
    public void onClickSearchItem() {
        mView.openSearch();
    }

    @Override
    public void onGettingUser(User currentUser) {
        mView.setUserInformationOnNavDrawer(currentUser.getName(),currentUser.getEmail(),currentUser.getUriAvatar());
    }
}
