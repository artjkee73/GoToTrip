package com.androiddev.artemqa.gototrip.modules.main;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by artemqa on 16.03.2018.
 */

public interface MainContract {
    interface View{
        void showProgress();

        void hideProgress();

        void setUserInformationOnNavDrawer(String name , String email , String urlPhoto);

        void openEditProfile();

        void openSearch();

        void openChat();

        void openNewPost();

        void openViewProfile(String currentUserId);

        void loadRv(Query queryKey, DatabaseReference refData, String uid);

        void openViewPost(String postId);

        void openListComments(String postId);

        void openViewPhoto(String photoUrlPost);
    }

    interface Presenter{

        void attachView(View view);

        void detachView();

        void viewIsReady();

        void onClickProfileItem();

        void onClickSearchItem();

        void onClickChatItem();

        void onClickNewPostItem();

        void onGettingUser(User currentUser);

        void onAvatarCliked();

        void onGettingUserId(String currentUserId);

        void onGettingQueryForRV(Query queryKey, DatabaseReference refData, String uid);

        void onLikeClicked(String postId, boolean finalIsLike);

        void onItemRvClicked(String postId);

        void onCommentClicked(String postId);

        void onAvatarUserClicked(String authorId);

        void onPostPhotoClicked(String photoUrlPost);
    }
}
