package com.androiddev.artemqa.gototrip.modules.viewProfile.presenter;

import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.viewProfile.ContractViewProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by artjk on 29.03.2018.
 */

public class ViewProfileInteractor {
    private ContractViewProfile.Presenter mPresenter;
    private DatabaseReference mRefBaseDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();

    public ViewProfileInteractor(ContractViewProfile.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getViewProfileUser(String viewUserId) {
        DatabaseReference viewUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(viewUserId);
        viewUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User viewUser = dataSnapshot.getValue(User.class);
                    mPresenter.onGettingUser(viewUser, mCurrentUser.getUid());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void followOnUser(String viewUsersId) {
        final DatabaseReference viewUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(viewUsersId);
        final DatabaseReference currentUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());

        viewUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    final User viewUser = dataSnapshot.getValue(User.class);
                    if (viewUser.getFollowers() != null) {
                        viewUser.getFollowers().put(mCurrentUser.getUid(), true);
                        viewUserRef.setValue(viewUser);
                        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    User currentUser = dataSnapshot.getValue(User.class);
                                    if (currentUser.getFollowings() != null) {
                                        currentUser.getFollowings().put(viewUser.getUserId(), true);
                                        currentUserRef.setValue(currentUser);
                                        mPresenter.onFollowOnUser();
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void unFollowUser(String viewUsersId) {
        final DatabaseReference viewUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(viewUsersId);
        final DatabaseReference currentUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        viewUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    final User viewUser = dataSnapshot.getValue(User.class);
                    if (viewUser.getFollowers() != null) {
                        viewUser.getFollowers().remove(mCurrentUser.getUid());
                        viewUserRef.setValue(viewUser);
                        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    User currentUser = dataSnapshot.getValue(User.class);
                                    currentUser.getFollowings().remove(viewUser.getUserId());
                                    currentUserRef.setValue(currentUser);
                                    mPresenter.onUnFollowUser();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
