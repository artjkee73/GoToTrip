package com.androiddev.artemqa.gototrip.modules.dialog.presenter;

import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;

/**
 * Created by artemqa on 05.04.2018.
 */

public class DialogPresenter implements ContractDialog.Presenter {
    private ContractDialog.View mView;
    private DialogInteractor mInteractor;

    public DialogPresenter() {
        mInteractor = new DialogInteractor(this);
    }

    @Override
    public void attachView(ContractDialog.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void viewIsReady(String idInterlocutor) {
        mInteractor.getInterlocutorUserProfile(idInterlocutor);
    }
}


