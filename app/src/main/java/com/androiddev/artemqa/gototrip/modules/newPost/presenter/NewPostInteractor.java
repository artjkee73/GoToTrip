package com.androiddev.artemqa.gototrip.modules.newPost.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.androiddev.artemqa.gototrip.common.models.Photo;
import com.androiddev.artemqa.gototrip.common.models.Post;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.newPost.ContractNewPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by artjk on 09.04.2018.
 */

public class NewPostInteractor {
    private ContractNewPost.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefBaseDatabase = mDatabase.getReference();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mBaseStorageReference = mStorage.getReference();
    private String mPostKey;

    public NewPostInteractor(ContractNewPost.Presenter presenter) {
        mPresenter = presenter;

    }

    public void addiPost(final String titlePost, final String textPost, final String urlUploadPostPhoto, final String postKey) {
        final DatabaseReference refCurrentUser = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        final DatabaseReference mRefBaseUsers = mRefBaseDatabase.child(Constants.USERS_LOCATION);
        refCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    currentUser.getPosts().put(postKey, true);
//                    Post post = new Post(postKey, titlePost, currentUser.getUserId(), currentUser.getName(), currentUser.getUriAvatarThumbnail(), textPost, urlUploadPostPhoto);
//                    mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postKey).setValue(post);
                    refCurrentUser.setValue(currentUser);
                    for (final String currentUId : currentUser.getFollowers().keySet()) {
                        mRefBaseUsers.child(currentUId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    User currentFollower = dataSnapshot.getValue(User.class);
                                    currentFollower.getFeedPosts().put(postKey, true);
                                    mRefBaseUsers.child(currentUId).setValue(currentFollower);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    mPresenter.onSuccessAddPost();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addPhoto(byte[] compressPhotoByteArrayOriginal, byte[] compressPhotoByteArrayThumbnail) {
        if (mPostKey == null) {
            mPostKey = mRefBaseDatabase.child(Constants.POSTS_LOCATION).push().getKey();
        }
        String photoKey = mRefBaseDatabase.child(Constants.PHOTOS_LOCATION).push().getKey();
        DatabaseReference refPhotoGeneral = mRefBaseDatabase.child(Constants.PHOTOS_LOCATION).child(photoKey);
        DatabaseReference refPhotoPost = mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(mPostKey).child(Constants.PHOTOS_LOCATION).child(photoKey);
        StorageReference refPhotoOriginal = mBaseStorageReference.child(Constants.POSTS_LOCATION).child(mPostKey).child("images").child("photo_orig_" + photoKey + ".webp");
        StorageReference refPhotoThumbnail = mBaseStorageReference.child(Constants.POSTS_LOCATION).child(mPostKey).child("images").child("photo_thumb_" + photoKey + ".webp");
        UploadTask uploadTaskOriginal = refPhotoOriginal.putBytes(compressPhotoByteArrayOriginal);
        uploadTaskOriginal.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrlOriginal = taskSnapshot.getDownloadUrl();
                UploadTask uploadTaskThumbnail = refPhotoThumbnail.putBytes(compressPhotoByteArrayThumbnail);
                uploadTaskThumbnail.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrlThumbnail = taskSnapshot.getDownloadUrl();
                        Photo newPhoto = new Photo(mPostKey, photoKey, downloadUrlOriginal.toString(), downloadUrlThumbnail.toString());
                        refPhotoGeneral.setValue(newPhoto);
                        refPhotoPost.setValue(newPhoto);
                        mPresenter.onAddPhoto(photoKey);
                    }
                });
            }
        });

    }

    public void addPost(String titlePost, String textPost, List<String> photoUploadedId, Double latitudeLoc, Double longitudeLoc, Long postDate) {
        final DatabaseReference refCurrentUser = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        final DatabaseReference mRefBaseUsers = mRefBaseDatabase.child(Constants.USERS_LOCATION);
        refCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    currentUser.getPosts().put(mPostKey, true);
                    Map<String, Boolean> listPhotos = new HashMap<>();
                    for (String photoId : photoUploadedId) {
                        listPhotos.put(photoId, true);
                    }
                    Post post = new Post(mPostKey, titlePost, currentUser.getUserId(),currentUser.getName(), currentUser.getUriAvatarThumbnail(), textPost, postDate,listPhotos,latitudeLoc,longitudeLoc );
                    mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(mPostKey).setValue(post);
                    refCurrentUser.setValue(currentUser);
                    for (final String currentUId : currentUser.getFollowers().keySet()) {
                        mRefBaseUsers.child(currentUId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    User currentFollower = dataSnapshot.getValue(User.class);
                                    currentFollower.getFeedPosts().put(mPostKey, true);
                                    mRefBaseUsers.child(currentUId).setValue(currentFollower);
                                    mPresenter.onSuccessAddPost();
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
