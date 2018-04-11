package com.androiddev.artemqa.gototrip.modules.listComments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 11.04.2018.
 */

public interface ContractListComments {
    interface View {
        void loadRV(Query queryKey, DatabaseReference refData);

        void clearEtComment();
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady(String postId);

        void onGettingQueryForRV(Query queryKey, DatabaseReference refData);

        void onButtonSendCommentClicked(String textComment, String postId);

        void onAddedComment();
    }
}
