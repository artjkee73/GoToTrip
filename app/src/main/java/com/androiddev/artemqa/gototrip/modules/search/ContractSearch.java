package com.androiddev.artemqa.gototrip.modules.search;

import android.app.DownloadManager;

import com.google.firebase.database.Query;

/**
 * Created by artemqa on 19.03.2018.
 */

public interface ContractSearch {
    interface View {
        void showProgress();

        void hideProgress();

        void searchUser(Query userNameQuery);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void onButtonSearchClicked(String userName);

        void onGettingSearchQueryUser(Query userNameQuery);
    }
}
