package com.androiddev.artemqa.gototrip.modules.main.presenter;

import com.androiddev.artemqa.gototrip.common.models.Photo;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void getInitialDataForRV() {
        final ArrayList<String> idPosts = new ArrayList<>();
        Query queryKeys = mBaseRefDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid()).child("feedPosts").orderByKey().limitToLast(3);
        final DatabaseReference refPostBase = mBaseRefDatabase.child(Constants.POSTS_LOCATION);
        queryKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        idPosts.add(id.getKey());
                    }
                    Collections.reverse(idPosts);
                    for (String idPost : idPosts) {
                        refPostBase.child(idPost).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    addPhotosInPostForLoadInitialData(post);
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

    private void addPhotosInPostForLoadInitialData(Post post) {
        final ArrayList<Photo> listPhotos = new ArrayList<>();
        final ArrayList<String> idPhotos = new ArrayList<>();
        Query queryKeys = mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(post.getPostId()).child("listPhoto").orderByKey();
        final DatabaseReference refPhotoBase = mBaseRefDatabase.child(Constants.PHOTOS_LOCATION);
        queryKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        idPhotos.add(id.getKey());
                    }
                    for (String idPhoto : idPhotos) {
                        refPhotoBase.child(idPhoto).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Photo photo = dataSnapshot.getValue(Photo.class);
                                    listPhotos.add(photo);
                                    if (idPhotos.size() == listPhotos.size()) {
                                        post.setPhotos(listPhotos);
                                        mPresenter.onLoadInitialDataForAdapter(post);
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

    public void getOldDataForRv(final String lastItemId) {
        final List<String> idPosts = new ArrayList<>();
        mBaseRefDatabase.child(Constants.USERS_LOCATION)
                .child(lastItemId).child("feedPosts").orderByKey()
                .endAt(lastItemId).limitToLast(3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                if (!id.getKey().equals(lastItemId)) {
                                    idPosts.add(id.getKey());
                                }
                            }
                            if (!idPosts.isEmpty()) {
                                Collections.reverse(idPosts);
                                for (String postId : idPosts) {
                                    mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(postId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {
                                                Post post = dataSnapshot.getValue(Post.class);
                                                addPhotosInPostForLoadOldData(post);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            } else {
                                mPresenter.onEmptyLoadOldDataForRv();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addPhotosInPostForLoadOldData(Post post) {
        final ArrayList<Photo> listPhotos = new ArrayList<>();
        final ArrayList<String> idPhotos = new ArrayList<>();
        Query queryKeys = mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(post.getPostId()).child("listPhoto").orderByKey();
        final DatabaseReference refPhotoBase = mBaseRefDatabase.child(Constants.PHOTOS_LOCATION);
        queryKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        idPhotos.add(id.getKey());
                    }
                    for (String idPhoto : idPhotos) {
                        refPhotoBase.child(idPhoto).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Photo photo = dataSnapshot.getValue(Photo.class);
                                    listPhotos.add(photo);
                                    if (idPhotos.size() == listPhotos.size()) {
                                        post.setPhotos(listPhotos);
                                        mPresenter.onLoadOldDataForRv(post);
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

    public void getNewDataForRV(final String firstItemId) {
        final List<String> idPosts = new ArrayList<>();
        mBaseRefDatabase.child(Constants.USERS_LOCATION).
                child(mCurrentUser.getUid()).
                child("feedPosts").
                orderByKey().
                startAt(firstItemId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                if (!id.getKey().equals(firstItemId)) {
                                    idPosts.add(id.getKey());
                                }
                            }
                            if (!idPosts.isEmpty()) {
                                for (String idPost : idPosts) {
                                    mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(idPost).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {
                                                Post post = dataSnapshot.getValue(Post.class);
                                                addPhotosInPostForLoadNewData(post);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            } else {
                                mPresenter.onEmptyLoadNewDataPosts();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void addPhotosInPostForLoadNewData(Post post) {
        final ArrayList<Photo> listPhotos = new ArrayList<>();
        final ArrayList<String> idPhotos = new ArrayList<>();
        Query queryKeys = mBaseRefDatabase.child(Constants.POSTS_LOCATION).child(post.getPostId()).child("listPhoto").orderByKey();
        final DatabaseReference refPhotoBase = mBaseRefDatabase.child(Constants.PHOTOS_LOCATION);
        queryKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        idPhotos.add(id.getKey());
                    }
                    for (String idPhoto : idPhotos) {
                        refPhotoBase.child(idPhoto).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Photo photo = dataSnapshot.getValue(Photo.class);
                                    listPhotos.add(photo);
                                    if (idPhotos.size() == listPhotos.size()) {
                                        post.setPhotos(listPhotos);
                                        mPresenter.onLoadDataNewPosts(post);
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
}
