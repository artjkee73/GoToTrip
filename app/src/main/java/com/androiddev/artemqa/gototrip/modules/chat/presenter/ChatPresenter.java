package com.androiddev.artemqa.gototrip.modules.chat.presenter;

import com.androiddev.artemqa.gototrip.modules.chat.ContractChat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 08.04.2018.
 */

public class ChatPresenter implements ContractChat.Presenter{
    private ContractChat.View mView;
    private ChatInteractor mInteractor;
    public ChatPresenter(){
        mInteractor = new ChatInteractor(this);
    }
    @Override
    public void attachView(ContractChat.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady() {
        mInteractor.getQueryForRV();
    }

    @Override
    public void onItemRvClicked() {

    }

    @Override
    public void onGettingQueryForRV(Query keyRef, DatabaseReference dataRef) {
        mView.loadRv(keyRef,dataRef);
    }
}
