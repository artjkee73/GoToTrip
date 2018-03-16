package com.androiddev.artemqa.gototrip.modules.login.presenter;

import android.content.SharedPreferences;
import com.androiddev.artemqa.gototrip.modules.login.LoginContract;
import java.util.regex.Pattern;

/**
 * Created by artemqa on 16.03.2018.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private LoginInteractor mInteractor;

    public LoginPresenter(SharedPreferences sharedPreferences) {
        mInteractor = new LoginInteractor(this, sharedPreferences);
    }


    @Override
    public void attachView(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void OnClickRegisterButton() {
        mView.openRegisterActivity();
    }

    @Override
    public void loginUser(String email, String password, boolean isCheked) {

        if (validateUser(email, password)) {
            mInteractor.loginUser(email, password, isCheked);
        }

    }

    private boolean validateUser(String email, String password) {

        boolean isValidate = false;

        if (email.isEmpty() && password.isEmpty()) {
            mView.showErrorEmptyField();
            isValidate = false;

        } else if (Pattern.matches("^.{3,}@.{3,}$", email) &&
                Pattern.matches("[A-Z_a-z_а-я_А-Я_0-9\\u002E\\u005F]{6,15}", password)) {
            isValidate = true;
        } else{
            isValidate = false;
            mView.showErrorUnvalidField();
        }


        return isValidate;
    }


    @Override
    public void onClickLoginButton(String email, String password, boolean isChecked) {
        mView.showProgress();
        loginUser(email, password, isChecked);
    }


    @Override
    public void saveToSharedPref(String email, String password, boolean isChecked) {
        if (isChecked) {
            mInteractor.saveToSharedPreferences(email, password, isChecked);
        }
    }

    @Override
    public void viewIsReady() {
        mInteractor.loadFromSharedPref();
    }

    @Override
    public void onSuccessLogin() {

        mView.showSuccessAuth();
        mView.openMainActivity();
        mView.hideProgress();

    }

    @Override
    public void onFailedLogin(String errorMessage) {

        mView.showErrorAuth(errorMessage);
        mView.hideProgress();
    }

    @Override
    public void setFromSharedPref(String email, String password, boolean isChecked) {
        mView.setFromSharedPref(email, password, isChecked);
    }

}
