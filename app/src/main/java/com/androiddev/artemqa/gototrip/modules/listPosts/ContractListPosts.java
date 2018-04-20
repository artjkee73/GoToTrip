package com.androiddev.artemqa.gototrip.modules.listPosts;

import com.androiddev.artemqa.gototrip.common.models.Post;

import java.util.ArrayList;

/**
 * Created by artemqa on 10.04.2018.
 */

public interface ContractListPosts {
    interface View {
        void openViewPost(String postId);

        void openListComments(String postId);

        void openViewProfile(String authorId);

        void openViewPhoto(String photoUrlPost);

        void setInitialDataInRv(Post post);

        void setNewDataForRv(Post post);

        void setOldDataInRv(Post post);

        void showNoNewDataForRv();

        void showNoOldDataForRv();
    }

    interface Presenter {
        void attachView(View view);

        void detachView();

        void viewIsReady(String viewUserId);
        
        void onItemRvClicked(String postId);

        void onLikeClicked(String postId, boolean finalIsLike);

        void onCommentClicked(String postId);

        void onAvatarUserClicked(String authorId);

        void onPostPhotoClicked(String photoUrlPost);

        void onLoadInitialDataForAdapter(Post posts);

        void loadNewDataForRV(String viewUserIdFromIntent, String firstItemId);

        void onLoadDataNewPosts(Post post);

        void loadOldItemsInRv(String viewUserIdFromIntent, String lastItemId);

        void onLoadOldDataForRv(Post post);

        void onEmptyLoadNewDataPosts();

        void onEmptyLoadOldDataForRv();
    }
}
