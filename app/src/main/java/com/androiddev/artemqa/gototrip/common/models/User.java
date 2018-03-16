package com.androiddev.artemqa.gototrip.common.models;

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


    public User() {
    }

    public User(String userId, String email, String name) {
        this.mUserId = userId;
        this.mEmail = email;
        this.mName = name;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getmListVisitedCountries() {
        return mListVisitedCountries;
    }

    public void setmListVisitedCountries(String listVisitedCountries) {
        this.mListVisitedCountries = listVisitedCountries;
    }

    public String getUriAvatar() {
        return mUriAvatar;
    }

    public void setUriAvatar(String mUriAvatar) {
        this.mUriAvatar = mUriAvatar;
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


}
