package com.androiddev.artemqa.gototrip.modules.listUsers.presenter;

import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.androiddev.artemqa.gototrip.modules.listUsers.ContractListUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 09.04.2018.
 */

public class ListUsersInteractor {
    private ContractListUsers.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefDatabaseBase = mDatabase.getReference();
    private DatabaseReference refChatsBase = mRefDatabaseBase.child(Constants.CHATS_LOCATION);
    private DatabaseReference refMessagesBase = mRefDatabaseBase.child(Constants.MESSAGES_LOCATION);

    public ListUsersInteractor(ContractListUsers.Presenter presenter){
        mPresenter = presenter;
    }

    public void getFollowersQueryForRv(String viewUserId) {
        DatabaseReference refData = mRefDatabaseBase.child(Constants.USERS_LOCATION);
        Query queryKey = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(viewUserId).child("followers").orderByValue();
        mPresenter.onGettingQueryForRv(queryKey,refData);
    }

    public void getFollowingsQueryForRv(String viewUserId) {
        DatabaseReference refData = mRefDatabaseBase.child(Constants.USERS_LOCATION);
        Query queryKey = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(viewUserId).child("followings").orderByValue();
        mPresenter.onGettingQueryForRv(queryKey,refData);
    }
}
