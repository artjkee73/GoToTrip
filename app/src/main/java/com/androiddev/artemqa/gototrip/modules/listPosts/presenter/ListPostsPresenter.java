package com.androiddev.artemqa.gototrip.modules.listPosts.presenter;

import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.modules.listPosts.ContractListPosts;

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
        mInteractor.getInitialDataForRV(viewUserId);
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

    @Override
    public void onCommentClicked(String postId) {
        mView.openListComments(postId);
    }

    @Override
    public void onAvatarUserClicked(String authorId) {
        mView.openViewProfile(authorId);
    }

    @Override
    public void onPostPhotoClicked(String photoUrlPost) {
        mView.openViewPhoto(photoUrlPost);
    }

    @Override
    public void onLoadInitialDataForAdapter(Post post) {
        mView.setInitialDataInRv(post);
    }

    @Override
    public void loadNewDataForRV(String viewUserId, String firstItemId) {
        mInteractor.getNewDataForRV(viewUserId,firstItemId);
    }

    @Override
    public void onLoadDataNewPosts(Post post) {
        mView.setNewDataForRv(post);
    }

    @Override
    public void loadOldItemsInRv(String viewUserIdFromIntent, String lastItemId) {
        mInteractor.getOldDataForRv(viewUserIdFromIntent,lastItemId);
    }

    @Override
    public void onLoadOldDataForRv(Post post) {
        mView.setOldDataInRv(post);
    }

    @Override
    public void onEmptyLoadNewDataPosts() {
        mView.showNoNewDataForRv();
    }

    @Override
    public void onEmptyLoadOldDataForRv() {
        mView.showNoOldDataForRv();
    }
}
