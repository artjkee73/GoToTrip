package com.androiddev.artemqa.gototrip.common.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artemqa on 05.03.2018.
 */

public class User {

    private String mUserId;
    private String mName;
    private String mEmail;
    private String mAboutMe;
    private String mCity;
    private String mCountry;
    private String mUriAvatar;
    private String mListVisitedCountries;
    private Map <String,Boolean> mFollowers = new HashMap<>();
    private Map <String,Boolean> mFollowings = new HashMap<>();
    private Map <String,Boolean> mPosts = new HashMap<>();
    private Map <String,Boolean> mChats = new HashMap<>();
    
    public User() {
    }

    public User(String userId, String email, String name,String uriAvatar) {
        this.mUserId = userId;
        this.mEmail = email;
        this.mName = name;
        this.mUriAvatar = uriAvatar;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getListVisitedCountries() {
        return mListVisitedCountries;
    }

    public void setListVisitedCountries(String listVisitedCountries) {
        this.mListVisitedCountries = listVisitedCountries;
    }

    public String getUriAvatar() {
        return mUriAvatar;
    }

    public void setUriAvatar(String uriAvatar) {
        this.mUriAvatar = uriAvatar;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getAboutMe() {
        return mAboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.mAboutMe = aboutMe;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public Map<String, Boolean> getFollowers() {
        return mFollowers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.mFollowers = followers;
    }

    public Map<String, Boolean> getFollowings() {
        return mFollowings;
    }

    public void setFollowings(Map<String, Boolean> followings) {
        this.mFollowings = followings;
    }

    public Map<String, Boolean> getPosts() {
        return mPosts;
    }

    public void setPosts(HashMap<String, Boolean> posts) {
        this.mPosts = mPosts;
    }

    public Map<String, Boolean> getChats() {
        return mChats;
    }
    public void setChats(Map<String, Boolean> chats) {
        mChats = chats;
    }

}
