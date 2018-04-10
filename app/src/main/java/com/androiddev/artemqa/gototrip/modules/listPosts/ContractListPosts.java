package com.androiddev.artemqa.gototrip.modules.listPosts;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 10.04.2018.
 */

public interface ContractListPosts {
    interface View {
        void loadRv(Query queryKey, DatabaseReference refData);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady(String viewUserId);

        void onGettingWueryForRv(Query queryKey, DatabaseReference refData);
    }
}
