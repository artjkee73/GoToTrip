package com.androiddev.artemqa.gototrip.modules.listComments.presenter;

import com.androiddev.artemqa.gototrip.modules.listComments.ContractListComments;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 11.04.2018.
 */

public class ListCommentsPresenter implements ContractListComments.Presenter {
    private ContractListComments.View mView;
    private ListCommentsInteractor mInteractor;

    public ListCommentsPresenter() {
        mInteractor = new ListCommentsInteractor(this);
    }

    @Override
    public void attachView(ContractListComments.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(String postId) {
        mInteractor.getQueryForRv(postId);

    }

    @Override
    public void onGettingQueryForRV(Query queryKey, DatabaseReference refData) {
        mView.loadRV(queryKey,refData);
    }

    @Override
    public void onButtonSendCommentClicked(String textComment, String postId) {
        mInteractor.addCommentInPost(textComment,postId);
    }

    @Override
    public void onAddedComment() {
        mView.clearEtComment();
    }

    @Override
    public void onAvatarUserClicked(String authorId) {
        mView.openViewProfile(authorId);
    }
}
