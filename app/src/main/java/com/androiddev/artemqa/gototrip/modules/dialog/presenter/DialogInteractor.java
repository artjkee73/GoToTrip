package com.androiddev.artemqa.gototrip.modules.dialog.presenter;

import com.androiddev.artemqa.gototrip.modules.dialog.ContractDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by artemqa on 05.04.2018.
 */

public class DialogInteractor {
    private ContractDialog.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefDatabaseBase = mDatabase.getReference();

    public DialogInteractor(ContractDialog.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getInterlocutorUserProfile(String idInterlocutor) {
        DatabaseReference refChat = mRefDatabaseBase.child("chats");

    }
}
