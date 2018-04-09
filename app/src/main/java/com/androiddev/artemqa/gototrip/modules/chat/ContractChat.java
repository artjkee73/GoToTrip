package com.androiddev.artemqa.gototrip.modules.chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 08.04.2018.
 */

public interface ContractChat {
    interface View {

        void updateUI();

        void loadRv(Query keyRef, DatabaseReference dataRef);

        void openDialog(String chatClickedId);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady();

        void onItemRvClicked(String chatId);

        void onGettingQueryForRV(Query keyRef, DatabaseReference dataRef);
    }
}
