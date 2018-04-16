package com.androiddev.artemqa.gototrip.modules.dialog.presenter;

import android.content.Intent;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by artemqa on 05.04.2018.
 */

public class DialogPresenter implements ContractDialog.Presenter {
    private ContractDialog.View mView;
    private DialogInteractor mInteractor;
    private String currentChatId;

    public DialogPresenter() {
        mInteractor = new DialogInteractor(this);
    }

    @Override
    public void attachView(ContractDialog.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(Intent intent) {
        if (intent.getStringExtra(Constants.INTENT_CHAT_ID) != null &&
                intent.getStringExtra(Constants.INTENT_USER_ID) != null) {
            currentChatId = intent.getStringExtra(Constants.INTENT_CHAT_ID);
            mInteractor.getQueryForGetMessages(currentChatId);
            mInteractor.getInterlocutorUser(intent.getStringExtra(Constants.INTENT_USER_ID));
        } else {
            mInteractor.getInterlocutorAndCurrentUserProfile(intent.getStringExtra(Constants.INTENT_USER_ID));
        }
    }

    @Override
    public void onGettingInterlocutorAndCurrentUser(User interlocutorUser, User currentUser) {

        getChatId(interlocutorUser, currentUser);

        if (interlocutorUser.getName() != null) {
            mView.setInterlocutorName(interlocutorUser.getName());
        }

        if (interlocutorUser.getUriAvatar() != null) {
            mView.setAvatarInterlocutor(interlocutorUser.getUriAvatar());
        }

        if (currentChatId != null) {
            mInteractor.getQueryForGetMessages(currentChatId);
        } else {
            mView.showEmptyRV();
        }
    }

    @Override
    public void onSendMessageClicked(String textMessage) {
        if (currentChatId != null) {
            mInteractor.addMessageInChat(currentChatId, textMessage);
        } else {
            mInteractor.createChatAndAddMessage(textMessage);
        }
    }

    @Override
    public void onFirstMessageAdded() {
        mView.hideEmptyRV();
        mView.updateUI();

    }

    @Override
    public void onGettingQueryForGetMessages(Query keyRef, DatabaseReference dataRef, String currentUserId) {
        mView.loadRvData(keyRef, dataRef, currentUserId);
    }

    @Override
    public void onNewMessageAdded() {
        mView.updateUI();
        mView.clearEtMessage();
    }

    @Override
    public void onGettingInterlocutorUser(User interlocutor) {
        if (interlocutor.getName() != null) {
            mView.setInterlocutorName(interlocutor.getName());
        }
        if (interlocutor.getUriAvatar() != null) {
            mView.setAvatarInterlocutor(interlocutor.getUriAvatar());
        }
    }

    @Override
    public void onAvatarInterlocutorClicked(String interlocutorId) {
        mView.openViewProfile(interlocutorId);
    }

    private void getChatId(User interlocutorUser, User currentUser) {
        for (String currentUId : currentUser.getChats().keySet()) {
            for (String interlocutorUid : interlocutorUser.getChats().keySet()) {
                if (currentUId.equals(interlocutorUid))
                    currentChatId = currentUId;
            }
        }
    }
}


