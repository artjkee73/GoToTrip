package com.androiddev.artemqa.gototrip.modules.viewProfile;

import com.androiddev.artemqa.gototrip.common.models.User;

/**
 * Created by artjk on 29.03.2018.
 */

public interface ContractViewProfile {
    interface View {

        void setIsFollowOnUser(boolean isFollow);

        void setUserAvatar(String urlAvatar);

        void setUserName(String userName);

        void setPostsCount(int postsCount);

        void setFollowersCount(int followersCount);

        void setFollowingsCount(int followingsCount);

        void setUserLocation(String locationUser);

        void setVisitedCountriesUser(String visitedCountriesUser);

        void setAboutUser(String aboutUser);

        void showDialogActivity(String viewUsersId);

        void updateUI();
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void viewIsReady(String viewUserId);

        void onGettingUser(User viewUser,String currentUserId);

        void onButtonMessageClicked(String viewUsersId);

        void onButtonFollowClicked(String viewUsersId);

        void onFollowOnUser();

        void onButtonUnFollowClicked(String viewUsersId);

        void onUnFollowUser();

        void onButtonListFollowersClicked(String viewUsersId);

        void onButtonListFollowingsClicked(String viewUsersId);
    }
}
