package com.androiddev.artemqa.gototrip.modules.register;

/**
 * Created by artemqa on 16.03.2018.
 */

public interface RegisterContract {
    interface View {
        void showProgress();

        void hideProgress();

        void showErrorEmptyFields();

        void showErrorUnvalidFields();

        void showErrorRegister(String errorMessage);

        void showSuccessRegister();

        void openMainActivity();

        void openLoginActivity();
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void onButtonRegisterClicked(String email,String name,String password);

        void onButtonLoginClicked();

        void onSuccessRegister(String email,String name,String password);

        void onFailedRegister(String errorMessage);

    }
}
