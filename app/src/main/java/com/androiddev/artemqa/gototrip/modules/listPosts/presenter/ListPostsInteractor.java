package com.androiddev.artemqa.gototrip.modules.listPosts.presenter;

import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.listPosts.ContractListPosts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by artemqa on 10.04.2018.
 */

public class ListPostsInteractor {
    private ContractListPosts.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefBaseDatabase = mDatabase.getReference();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mBaseStorageReference = mStorage.getReference();

    public ListPostsInteractor(ContractListPosts.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getQueryForRV(String viewUserId) {
        Query queryKey = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(viewUserId).child("posts").orderByValue();
        DatabaseReference refData = mRefBaseDatabase.child(Constants.POSTS_LOCATION);
        mPresenter.onGettingWueryForRv(queryKey,refData);
    }
}
