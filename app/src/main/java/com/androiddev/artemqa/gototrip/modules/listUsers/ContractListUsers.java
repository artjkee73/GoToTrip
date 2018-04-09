package com.androiddev.artemqa.gototrip.modules.listUsers;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 09.04.2018.
 */

public interface ContractListUsers {
    interface View {
        void loadRV(Query queryKey, DatabaseReference refData);

        void openViewProfile(String idUserClicked);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady(Intent intent);

        void onGettingQueryForRv(Query queryKey, DatabaseReference refData);

        void onItemRvClicked(String idUserClicked);

    }
}
