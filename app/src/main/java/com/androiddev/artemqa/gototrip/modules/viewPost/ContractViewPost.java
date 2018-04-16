package com.androiddev.artemqa.gototrip.modules.viewPost;

import com.androiddev.artemqa.gototrip.common.models.Post;

/**
 * Created by artemqa on 11.04.2018.
 */

public interface ContractViewPost {
    interface View {
        void setAvatarAuthor(String urlAvatarAuthor);

        void setNameAuthor(String nameAuthor);

        void setDateCreatedPost(String dateCreatedPost);

        void setTitlePost(String titlePost);

        void setUrlPostPhoto(String urlPostPhoto);

        void setTextPost(String textPost);

        void setLikesCount(String likesCount);

        void setCommentsCount(String commentsCount);

        void setIsLiked(boolean isLicked);

        void openListComments(String postId);

        void updateUI();

        void openViewPhoto(String photoUrlPost);
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady(String postIdFromIntent);

        void onGettingPostData(Post currentPost, String currentUserId);

        void onCommentButtonClicked(String postId);

        void onLikeButtonClicked(String postId);

        void onRemoveLike();

        void onAddLike();

        void onPhotoPostClicked(String postIdFromIntent);

        void onGettingUrlPhotoPost(String photoUrlPost);
    }
}
