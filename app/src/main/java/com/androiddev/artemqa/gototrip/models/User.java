package com.androiddev.artemqa.gototrip.models;

/**
 * Created by artemqa on 05.03.2018.
 */

public class User {
   private String mName;
   private String mEmail;
   private String mAboutMe;
   private String mCity;
   private String mCountry;
   private String mUriAvatar;
   private String mListVisitedCountries;


    public User() {

    }

    public User(String email){
        this.mEmail = email;
    }

    public User(String name, String email, String aboutMe, String city, String country,String liistVisitedCountries) {
        this.mName = name;
        this.mEmail = email;
        this.mAboutMe = aboutMe;
        this.mCity = city;
        this.mCountry = country;
        this.mListVisitedCountries = liistVisitedCountries;
    }

    public String getmListVisitedCountries() {
        return mListVisitedCountries;
    }

    public void setmListVisitedCountries(String mListVisitedCountries) {
        this.mListVisitedCountries = mListVisitedCountries;
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
