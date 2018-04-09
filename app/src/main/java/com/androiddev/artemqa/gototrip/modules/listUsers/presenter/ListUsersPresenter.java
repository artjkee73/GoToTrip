package com.androiddev.artemqa.gototrip.modules.listUsers.presenter;

import android.content.Intent;

import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.listUsers.ContractListUsers;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 09.04.2018.
 */

public class ListUsersPresenter implements ContractListUsers.Presenter {
    private ContractListUsers.View mView;
    private ListUsersInteractor mInteractor;

    public ListUsersPresenter(){
        mInteractor = new ListUsersInteractor(this);
    }
    @Override
    public void attachView(ContractListUsers.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(Intent intent) {
        if(intent.getStringExtra(Constants.INTENT_LIST_USERS_TYPE_LIST_USERS_VIEW_PROFILE)
                .equals(Constants.INTENT_LIST_USERS_FOLLOWERS_TYPE_PROFILE)){
            mInteractor.getFollowersQueryForRv(intent.getStringExtra(Constants.INTENT_LIST_USERS_USER_ID_VIEW_PROFILE));
        }else if(intent.getStringExtra(Constants.INTENT_LIST_USERS_TYPE_LIST_USERS_VIEW_PROFILE)
                .equals(Constants.INTENT_LIST_USERS_FOLLOWINGS_TYPE_PROFILE)){
            mInteractor.getFollowingsQueryForRv(intent.getStringExtra(Constants.INTENT_LIST_USERS_USER_ID_VIEW_PROFILE));
        }
    }

    @Override
    public void onGettingQueryForRv(Query queryKey, DatabaseReference refData) {
        mView.loadRV(queryKey,refData);
    }

    @Override
    public void onItemRvClicked(String idUserClicked) {
        mView.openViewProfile(idUserClicked);
    }
}
