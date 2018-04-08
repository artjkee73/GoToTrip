package com.androiddev.artemqa.gototrip.modules.dialog;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 05.04.2018.
 */

public interface ContractDialog {

    interface View {

        void showEmptyRV();

        void hideEmptyRV();

        void setInterlocutorName(String nameInterlocutor);

        void setAvatarInterlocutor(String urlAvatar);

        void updateUI();

        void loadRvData(Query keyRef,DatabaseReference dataRef,String currentUserId);
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void viewIsReady(String idInterlocutor);

        void onGettingInterlocutorAndCurrentUser(User interlocutorUser, User currentUser);

        void onSendMessageClicked(String textMessage);

        void onFirstMessageAdded();

        void onGettingQueryForGetMessages(Query keyRef,DatabaseReference dataRef,String currentUserId);

        void onNewMessageAdded();
    }
}
