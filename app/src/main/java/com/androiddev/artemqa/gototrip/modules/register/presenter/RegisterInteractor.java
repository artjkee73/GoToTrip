package com.androiddev.artemqa.gototrip.modules.register.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.modules.register.RegisterContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by artemqa on 16.03.2018.
 */

public class RegisterInteractor {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRefFirebase = FirebaseDatabase.getInstance().getReference();
    private RegisterContract.Presenter mPresenter;
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    public RegisterInteractor(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void registerUser(final String email, final String name, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mPresenter.onSuccessRegister(email, name, password);
                        } else {
                            mPresenter.onFailedRegister(task.getException().toString());
                        }
                    }
                });
    }

    public void addUserInDB(String email, String name) {
        String userId = currentUser.getUid();
        User user = new User(userId,email,name);
        mRefFirebase.child("users").child(userId).setValue(user);
    }
}