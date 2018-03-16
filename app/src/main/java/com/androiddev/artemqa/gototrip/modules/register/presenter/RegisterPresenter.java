package com.androiddev.artemqa.gototrip.modules.register.presenter;

import com.androiddev.artemqa.gototrip.modules.register.RegisterContract;

import java.util.regex.Pattern;

/**
 * Created by artemqa on 16.03.2018.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mView;
    private RegisterInteractor mInteractor;

    public RegisterPresenter() {
        mInteractor = new RegisterInteractor(this);
    }

    @Override
    public void attachView(RegisterContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onButtonRegisterClicked(String email,String name,String password) {
        mView.showProgress();
        if(validateDataForRegister(email,name,password)){
            mInteractor.registerUser(email,name,password);
        }
    }

    @Override
    public void onButtonLoginClicked() {
        mView.openLoginActivity();
    }

    @Override
    public void onSuccessRegister(String email, String name, String password) {

        mView.hideProgress();
        mView.showSuccessRegister();
        mView.openMainActivity();
        mInteractor.addUserInDB(email, name);
    }

    @Override
    public void onFailedRegister(String errorMessage) {
        mView.hideProgress();
        mView.showErrorRegister(errorMessage);
    }

    private boolean validateDataForRegister(String email,String name,String password) {
        boolean isValidate = false;

        if (email.isEmpty() && password.isEmpty() && name.isEmpty()) {
            mView.showErrorEmptyFields();
            isValidate = false;
        } else if (Pattern.matches("^.{3,}@.{3,}$", email) &&
                Pattern.matches("[A-Z_a-z_а-я_А-Я_0-9\\u002E\\u005F]{6,15}", password)) {
            isValidate = true;
        } else {
            mView.showErrorUnvalidFields();
            isValidate = false;
        }

        return isValidate;
    }
}
