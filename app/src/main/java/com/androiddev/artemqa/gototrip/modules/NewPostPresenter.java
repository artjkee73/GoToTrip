package com.androiddev.artemqa.gototrip.modules;

/**
 * Created by artjk on 09.04.2018.
 */

public class NewPostPresenter implements ContractNewPost.Presenter {
    private ContractNewPost.View mView;
    private NewPostInteractor mInteractor;

    public NewPostPresenter() {
        mInteractor = new NewPostInteractor(this);
    }

    @Override
    public void attachView(ContractNewPost.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void onButtonAddPostClicked() {

    }

    @Override
    public void onPhotoPostClicked() {

    }
}
