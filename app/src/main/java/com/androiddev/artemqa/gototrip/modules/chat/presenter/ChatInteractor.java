package com.androiddev.artemqa.gototrip.modules.chat.presenter;

import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.chat.ContractChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 08.04.2018.
 */

public class ChatInteractor {
    private ContractChat.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefDatabaseBase = mDatabase.getReference();

    public ChatInteractor(ContractChat.Presenter presenter){
        mPresenter = presenter;
    }

    public void getQueryForRV() {
        Query queryKey = mRefDatabaseBase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid()).child("chats").orderByValue();
        DatabaseReference refData = mRefDatabaseBase.child(Constants.CHATS_LOCATION);
        mPresenter.onGettingQueryForRV(queryKey,refData);
    }
}
