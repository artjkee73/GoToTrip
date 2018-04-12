package com.androiddev.artemqa.gototrip.modules.main.presenter;

import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.main.MainContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private MainContract.Presenter mPresenter;

    public MainInteractor(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getUserFromDB() {
        DatabaseReference userRef = mBaseRefDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
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

    public void getUserId() {
        mPresenter.onGettingUserId(mCurrentUser.getUid());
    }

    public void getQueryForRv() {
        Query queryKey = mBaseRefDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid()).child("feedPosts").orderByValue();
        DatabaseReference refData = mBaseRefDatabase.child(Constants.POSTS_LOCATION);
        mPresenter.onGettingQueryForRV(queryKey, refData, mCurrentUser.getUid());
    }

    public void removeLikeFromPost(String postId) {
        final DatabaseReference refPost = mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(postId);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Post currentPost = dataSnapshot.getValue(Post.class);
                    currentPost.getLikeUsers().remove(mCurrentUser.getUid());
                    refPost.setValue(currentPost);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addLikeToPost(String postId) {
        final DatabaseReference refPost = mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(postId);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Post currentPost = dataSnapshot.getValue(Post.class);
                    currentPost.getLikeUsers().put(mCurrentUser.getUid(), true);
                    refPost.setValue(currentPost);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
