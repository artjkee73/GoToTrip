package com.androiddev.artemqa.gototrip.modules.dialog;

import android.content.Intent;

import com.androiddev.artemqa.gototrip.common.models.Message;
import com.androiddev.artemqa.gototrip.common.models.User;

import java.util.ArrayList;

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

        void clearEtMessage();

        void openViewProfile(String interlocutorId);

        void setInitialDataForAdapter(ArrayList<Message> message);

        void setOldDataForRecyclerView(ArrayList<Message> messages);

        void setNewMessageInRV(Message newMessage);

        void showEndOldDataForRv();
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void viewIsReady(Intent intent);

        void onGettingInterlocutorAndCurrentUser(User interlocutorUser, User currentUser);

        void onSendMessageClicked(String textMessage);

        void onFirstMessageAdded();

        void onNewMessageAdded();

        void onGettingInterlocutorUser(User interlocutor);

        void onAvatarInterlocutorClicked(String interlocutorIdFromIntent);

        void onLoadInitialDataForAdapter(ArrayList<Message> message);

        void onSwipeToRefresh(String lastItemId);

        void onLoadOldDataForRV(ArrayList<Message> messages);

        void onGettingNewMessageForRV(Message newMessage);

        void onLoadRV(String lastItemId);

        void onEndOldDataForLoadInRV();
    }
}
