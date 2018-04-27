package com.androiddev.artemqa.gototrip.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;


import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by artemqa on 15.03.2018.
 */

public class AppGoToTrip extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

