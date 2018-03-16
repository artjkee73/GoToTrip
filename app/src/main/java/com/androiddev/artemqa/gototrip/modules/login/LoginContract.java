package com.androiddev.artemqa.gototrip.modules.login;

/**
 * Created by artemqa on 16.03.2018.
 */

public interface LoginContract {

    interface View {

        void showProgress();

        void hideProgress();

        void showErrorAuth(String errorMassage);

        void showSuccessAuth();

        void setFromSharedPref(String email,String password,boolean isChecked);

        void showErrorEmptyField();

        void openMainActivity();

        void openRegisterActivity();

    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void OnClickRegisterButton();

        void loginUser(String email,String password, boolean isCheked);

        void onClickLoginButton(String email,String password,boolean isChecked);

        void saveToSharedPref(String email,String password,boolean isChecked);

        void viewIsReady();

        void onSuccessLogin();

        void onFailedLogin(String errorMessage);

        void setFromSharedPref(String email,String password,boolean isChecked);
    }
}
