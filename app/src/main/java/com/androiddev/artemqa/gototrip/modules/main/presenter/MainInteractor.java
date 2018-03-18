package com.androiddev.artemqa.gototrip.modules.main.presenter;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by artemqa on 17.03.2018.
 */

public class MainInteractor {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mBaseRefDatabase = mDatabase.getReference();
    private FirebaseStorage mStorage;

    private MainContract.Presenter mPresenter;

    public MainInteractor(MainContract.Presenter presenter){
        mPresenter = presenter;
    }

    public void getUserFromDB(){
        DatabaseReference userRef = mBaseRefDatabase.child("users").child(mCurrentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                mPresenter.onGettingUser(currentUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
