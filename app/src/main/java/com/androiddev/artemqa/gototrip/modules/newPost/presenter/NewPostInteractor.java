package com.androiddev.artemqa.gototrip.modules.newPost.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;

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

    public NewPostInteractor(ContractNewPost.Presenter presenter) {
        mPresenter = presenter;

    }

    public void setPhotoPost(byte[] compressPhotoByteArray) {
        final String postKey = mRefBaseDatabase.child(Constants.POSTS_LOCATION).push().getKey();
        StorageReference refPhoto = mBaseStorageReference.child(Constants.POSTS_LOCATION).child(postKey).child("images").child("photo_" + postKey + ".jpeg");
        UploadTask uploadTask = refPhoto.putBytes(compressPhotoByteArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mPresenter.onFailedUploadPhoto();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mPresenter.onSuccessUploadPhoto(downloadUrl.toString(), postKey);
            }
        });
    }

    public void addPost(final String titlePost, final String textPost, final String urlUploadPostPhoto, final String postKey) {
        final DatabaseReference refCurrentUser = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        refCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    currentUser.getPosts().put(postKey, true);
                    Post post = new Post(postKey, titlePost, currentUser.getUserId(), currentUser.getName(), currentUser.getUriAvatar(), textPost, urlUploadPostPhoto);
                    mRefBaseDatabase.child(Constants.POSTS_LOCATION).child(postKey).setValue(post);
                    refCurrentUser.setValue(currentUser);
                    mPresenter.onSuccessAddPost();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
