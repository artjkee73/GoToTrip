package com.androiddev.artemqa.gototrip.modules.listPosts.presenter;

import com.androiddev.artemqa.gototrip.modules.listPosts.ContractListPosts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 10.04.2018.
 */

public class ListPostsPresenter implements ContractListPosts.Presenter {
    private ContractListPosts.View mView;
    private ListPostsInteractor mInteractor;

    public ListPostsPresenter() {
        mInteractor = new ListPostsInteractor(this);
    }

    @Override
    public void attachView(ContractListPosts.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(String viewUserId) {
        mInteractor.getQueryForRV(viewUserId);
    }

    @Override
    public void onGettingWueryForRv(Query queryKey, DatabaseReference refData,String currentUserId) {
        mView.loadRv(queryKey,refData,currentUserId);
    }

    @Override
    public void onItemRvClicked(String postId) {
        mView.openViewPost(postId);
    }

    @Override
    public void onLikeClicked(String postId, boolean isLike) {
        if(isLike){
            mInteractor.removeLikeFromPost(postId);
        }else {
            mInteractor.addLikeToPost(postId);
        }
    }
}
