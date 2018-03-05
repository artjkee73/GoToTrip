package com.androiddev.artemqa.gototrip.models;

/**
 * Created by artemqa on 05.03.2018.
 */

public class User {


   private String name;
   private String email;
   private String aboutMe;
   private String city;
   private String country;

    public User() {
    }

    public User(String name, String email, String aboutMe, String city, String country) {
        this.name = name;
        this.email = email;
        this.aboutMe = aboutMe;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
