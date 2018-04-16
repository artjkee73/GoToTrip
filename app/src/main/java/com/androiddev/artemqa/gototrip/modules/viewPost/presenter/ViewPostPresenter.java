package com.androiddev.artemqa.gototrip.modules.viewPost.presenter;

import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.viewPost.ContractViewPost;
import com.androiddev.artemqa.gototrip.modules.viewPost.presenter.ViewPostInteractor;

/**
 * Created by artemqa on 11.04.2018.
 */

public class ViewPostPresenter implements ContractViewPost.Presenter {
    private ContractViewPost.View mView;
    private ViewPostInteractor mInteractor;
    private boolean isLikedPost;

    public ViewPostPresenter() {
        mInteractor = new ViewPostInteractor(this);
    }

    @Override
    public void attachView(ContractViewPost.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(String postId) {
        mInteractor.getPostData(postId);
    }

    @Override
    public void onGettingPostData(Post currentPost, String currentUserId) {
        if (currentPost.getAuthorName() != null) {
            mView.setNameAuthor(currentPost.getAuthorName());
        }
        if (currentPost.getAuthorUriAvatar() != null) {
            mView.setAvatarAuthor(currentPost.getAuthorUriAvatar());
        }
        if (currentPost.getDateCreated() != null) {
            mView.setDateCreatedPost(Utils.timestampToDateMessage(currentPost.getDateCreatedLong()));
        }
        if (currentPost.getTitlePost() != null) {
            mView.setTitlePost(currentPost.getTitlePost());
        }
        if (currentPost.getPhotoUrlPost() != null) {
            mView.setUrlPostPhoto(currentPost.getPhotoUrlPost());
        }
        if (currentPost.getTextPost() != null) {
            mView.setTextPost(currentPost.getTextPost());
        }
        if (currentPost.getLikeUsers() != null) {
            mView.setLikesCount(String.valueOf(currentPost.getLikeUsers().size()));
        }
        if (currentPost.getComments() != null) {
            mView.setCommentsCount(String.valueOf(currentPost.getComments().size()));
        }
        if (currentPost.getLikeUsers() != null) {
            isLikedPost = currentPost.getLikeUsers().containsKey(currentUserId);

            mView.setIsLiked(isLikedPost);
        }
    }

    @Override
    public void onCommentButtonClicked(String postIdFromIntent) {
        mView.openListComments(postIdFromIntent);
    }

    @Override
    public void onLikeButtonClicked(String postIdFromIntent) {
        if (isLikedPost) {
            mInteractor.removeLikePost(postIdFromIntent);
        } else {
            mInteractor.addLikePost(postIdFromIntent);
        }
    }

    @Override
    public void onRemoveLike() {
        mView.updateUI();
    }

    @Override
    public void onAddLike() {
        mView.updateUI();
    }

    @Override
    public void onPhotoPostClicked(String postIdFromIntent) {
        mInteractor.getUrlPhotoPost(postIdFromIntent);
    }

    @Override
    public void onGettingUrlPhotoPost(String photoUrlPost) {
        mView.openViewPhoto(photoUrlPost);
    }


}
