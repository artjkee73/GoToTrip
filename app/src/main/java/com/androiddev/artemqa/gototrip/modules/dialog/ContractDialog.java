package com.androiddev.artemqa.gototrip.modules.dialog;

/**
 * Created by artemqa on 05.04.2018.
 */

public interface ContractDialog {

    interface View {
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void viewIsReady(String idInterlocutor);
    }
}
