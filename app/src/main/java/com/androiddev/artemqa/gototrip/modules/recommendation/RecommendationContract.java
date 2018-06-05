package com.androiddev.artemqa.gototrip.modules.recommendation;

import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.common.models.User;

public interface RecommendationContract {

        interface View{
//            void showProgress();
//
//            void hideProgress();

//            void setUserInformationOnNavDrawer(String name , String email , String urlPhoto);

//            void openEditProfile();

//            void openSearch();
//
//            void openChat();
//
//            void openNewPost();

            void openViewProfile(String currentUserId);

            void openViewPost(String postId);

            void openListComments(String postId);

            void openViewPhoto(String photoUrlPost);

            void setInitialDataInRv(Post post);

            void setOldDataInRv(Post post);

            void showNoOldDataForRv();

            void setNewDataForRv(Post post);

            void showNoNewDataForRv();
        }

        interface Presenter{

            void attachView(RecommendationContract.View view);

            void detachView();

            void viewIsReady();

            void onClickProfileItem();

            void onClickSearchItem();

            void onClickChatItem();

            void onClickNewPostItem();

            void onGettingUser(User currentUser);

            void onAvatarClicked();

            void onGettingUserId(String currentUserId);


            void onLikeClicked(String postId, boolean finalIsLike);

            void onItemRvClicked(String postId);

            void onCommentClicked(String postId);

            void onAvatarUserClicked(String authorId);

            void onPostPhotoClicked(String photoUrlPost);

            void loadNewDataForRV(String firstItemId);

            void loadOldItemsInRv(String lastItemId);

            void onLoadInitialDataForAdapter(Post post);

            void onLoadOldDataForRv(Post post);

            void onEmptyLoadOldDataForRv();

            void onLoadDataNewPosts(Post post);

            void onEmptyLoadNewDataPosts();
        }

}
