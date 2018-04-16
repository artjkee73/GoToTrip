package com.androiddev.artemqa.gototrip.modules.viewProfile.presenter;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.viewProfile.ContractViewProfile;

import java.util.HashMap;

/**
 * Created by artjk on 29.03.2018.
 */

public class ViewProfilePresenter implements ContractViewProfile.Presenter {

    private ContractViewProfile.View mView;
    private ViewProfileInteractor mInteractor;

    public ViewProfilePresenter() {
        mInteractor = new ViewProfileInteractor(this);
    }

    @Override
    public void attachView(ContractViewProfile.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(String viewUserId) {
        mInteractor.getViewProfileUser(viewUserId);
    }


    public void onGettingUser(User viewUser,String currentUserId) {
        if (viewUser.getUriAvatar() != null) {
            mView.setUserAvatar(viewUser.getUriAvatar());
        }
        if (viewUser.getName() != null) {
            mView.setUserName(viewUser.getName());
        }
        if (viewUser.getCity() != null && viewUser.getCountry() != null) {
            String city = viewUser.getCity();
            String country = viewUser.getCountry();
            mView.setUserLocation(country.concat(", ").concat(city));
        }
        if (viewUser.getListVisitedCountries() != null) {
            mView.setVisitedCountriesUser(viewUser.getListVisitedCountries());
        }
        if (viewUser.getAboutMe() != null) {
            mView.setAboutUser(viewUser.getAboutMe());
        }

        if (viewUser.getFollowers() != null) {
            mView.setFollowersCount(viewUser.getFollowers().size());
        }

        if (viewUser.getFollowings() != null) {
            mView.setFollowingsCount(viewUser.getFollowings().size());
        }
        if (viewUser.getPosts() != null) {
            mView.setPostsCount(viewUser.getPosts().size());
        }
        if(viewUser.getFollowers() != null){
            mView.setIsFollowOnUser(viewUser.getFollowers().containsKey(currentUserId));
        }
    }

    @Override
    public void onButtonMessageClicked(String viewUsersId) {
        mView.showDialogActivity(viewUsersId);
    }

    @Override
    public void onButtonFollowClicked(String viewUsersId) {
        mInteractor.followOnUser(viewUsersId);
    }

    @Override
    public void onFollowOnUser() {
        mView.setIsFollowOnUser(true);
        mView.updateUI();
    }


    @Override
    public void onButtonUnFollowClicked(String viewUsersId) {
        mInteractor.unFollowUser(viewUsersId);
    }

    @Override
    public void onUnFollowUser() {
        mView.setIsFollowOnUser(false);
        mView.updateUI();
    }

    @Override
    public void onButtonListFollowersClicked(String viewUsersId) {
        mView.showListFollowers(Constants.INTENT_LIST_USERS_TYPE_FOLLOWERS,viewUsersId);
    }

    @Override
    public void onButtonListFollowingsClicked(String viewUsersId) {
        mView.showListFollowings(Constants.INTENT_LIST_USERS_TYPE_FOLLOWINGS,viewUsersId);
    }

    @Override
    public void onButtonListPostsClicked(String viewUsersId) {
        mView.showListPosts(viewUsersId);
    }

    @Override
    public void onPhotoPostClicked(String usersId) {
        mInteractor.getAvatarUrlUser(usersId);
    }

    @Override
    public void onGettingUrlPhoto(String uriAvatar) {
        mView.openViewPhoto(uriAvatar);
    }


}
