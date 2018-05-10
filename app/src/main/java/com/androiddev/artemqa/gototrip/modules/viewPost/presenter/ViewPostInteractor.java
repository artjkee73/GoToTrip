package com.androiddev.artemqa.gototrip.modules.viewPost.presenter;

import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.viewPost.ContractViewPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by artemqa on 11.04.2018.
 */

public class ViewPostInteractor {
    private ContractViewPost.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefBaseDatabase = mDatabase.getReference();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mBaseStorageReference = mStorage.getReference();

    public ViewPostInteractor(ContractViewPost.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getPostData(String postId) {
        DatabaseReference refPost = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postId);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Post currentPost = dataSnapshot.getValue(Post.class);
                    addPhotosInPost(currentPost);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addPhotosInPost(Post post) {
        final ArrayList<Photo> listPhotos = new ArrayList<>();
        final ArrayList<String> idPhotos = new ArrayList<>();
        Query queryKeys = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(post.getPostId()).child("listPhoto").orderByKey();
        final DatabaseReference refPhotoBase = mRefBaseDatabase.child(Constants.PHOTOS_LOCATION);
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
                                    if(idPhotos.size() == listPhotos.size()){
                                        post.setPhotos(listPhotos);
                                        mPresenter.onGettingPostData(post, mCurrentUser.getUid());
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


    public void removeLikePost(String postId) {
        final DatabaseReference refPost = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postId);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Post currentPost = dataSnapshot.getValue(Post.class);
                    currentPost.getLikeUsers().remove(mCurrentUser.getUid());
                    refPost.setValue(currentPost);
                    mPresenter.onRemoveLike();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addLikePost(String postId) {
        final DatabaseReference refPost = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postId);
        refPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Post currentPost = dataSnapshot.getValue(Post.class);
                    currentPost.getLikeUsers().put(mCurrentUser.getUid(), true);
                    refPost.setValue(currentPost);
                    mPresenter.onAddLike();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
