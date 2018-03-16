package com.androiddev.artemqa.gototrip.modules.login.presenter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.androiddev.artemqa.gototrip.helper.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by artemqa on 16.03.2018.
 */

public class LoginInteractor {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private LoginPresenter mPresenter;
    private SharedPreferences mSharedPref;

    public LoginInteractor(LoginPresenter presenter, SharedPreferences sharedPreferences) {
        mPresenter = presenter;
        mSharedPref = sharedPreferences;
    }

    public void loginUser(final String email, final String password, final boolean isChecked) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mPresenter.onSuccessLogin();
                    mPresenter.saveToSharedPref(email, password, isChecked);
                } else {
                    mPresenter.onFailedLogin(task.getException().toString());
                }
            }
        });
    }

    public void saveToSharedPreferences(String email, String password, boolean isChecked) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(Constants.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL, email);
        editor.putString(Constants.SHARED_PREFERENCES_SAVE_LOGIN_PASSWORD, password);
        editor.putBoolean(Constants.SHARED_PREFERENCES_SAVE_LOGIN_SW_IS_CHECKED, isChecked);
        editor.apply();
    }

    public void loadFromSharedPref() {
        if (mSharedPref.contains(Constants.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL)) {
            String email = mSharedPref.getString(Constants.SHARED_PREFERENCES_SAVE_LOGIN_EMAIL, "");
            String password = mSharedPref.getString(Constants.SHARED_PREFERENCES_SAVE_LOGIN_PASSWORD, "");
            Boolean isChecked = mSharedPref.getBoolean(Constants.SHARED_PREFERENCES_SAVE_LOGIN_SW_IS_CHECKED, false);

            mPresenter.setFromSharedPref(email, password, isChecked);
        }
    }
}
