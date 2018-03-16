package com.androiddev.artemqa.gototrip.common;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by artemqa on 15.03.2018.
 */

public class AppGoToTrip extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

