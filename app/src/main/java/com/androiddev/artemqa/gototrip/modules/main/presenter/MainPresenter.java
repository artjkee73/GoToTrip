package com.androiddev.artemqa.gototrip.modules.main.presenter;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

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
        mInteractor.getQueryForRv();
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
    public void onClickChatItem() {
        mView.openChat();
    }

    @Override
    public void onClickNewPostItem() {
        mView.openNewPost();
    }

    @Override
    public void onGettingUser(User currentUser) {
        mView.setUserInformationOnNavDrawer(currentUser.getName(),currentUser.getEmail(),currentUser.getUriAvatar());
    }

    @Override
    public void onAvatarCliked() {
        mInteractor.getUserId();
    }

    @Override
    public void onGettingUserId(String currentUserId) {
        mView.openViewProfile(currentUserId);
    }

    @Override
    public void onGettingQueryForRV(Query queryKey, DatabaseReference refData, String uid) {
        mView.loadRv(queryKey,refData,uid);
    }

    @Override
    public void onLikeClicked(String postId, boolean isLike) {
        if(isLike){
            mInteractor.removeLikeFromPost(postId);
        }else {
            mInteractor.addLikeToPost(postId);
        }
    }

    @Override
    public void onItemRvClicked(String postId) {
        mView.openViewPost(postId);
    }

    @Override
    public void onCommentClicked(String postId) {
        mView.openListComments(postId);
    }
}
