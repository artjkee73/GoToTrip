package com.androiddev.artemqa.gototrip.modules.editProfile.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.editProfile.EditProfileContract;
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

import java.io.File;

/**
 * Created by artemqa on 17.03.2018.
 */

public class EditProfileInteractor {

    private EditProfileContract.Presenter mPresenter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mCurrentUser = mAuth.getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefBaseDatabase = mDatabase.getReference();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mBaseStorageReference = mStorage.getReference();

    public EditProfileInteractor(EditProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void getUserInformation(){
        DatabaseReference currentUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                mPresenter.onLoadUser(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUserAvatar(byte[] compressPhotoByteArray){
    StorageReference refPhoto = mBaseStorageReference.child(mCurrentUser.getUid()).child("images").child("avatar_"+ mCurrentUser.getUid() + ".jpeg");
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
                mPresenter.onSuccessUploadPhoto(downloadUrl.toString());
            }
        });
    }

    public void setUserUrlPhoto(final String urlPhoto){
        final DatabaseReference currentUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                currentUser.setUriAvatar(urlPhoto);
                currentUserRef.setValue(currentUser);
                mPresenter.onLoadUser(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUserInfo(final String name, final String country, final String city, final String aboutMe, final String visitedCountries) {

        final DatabaseReference currentUserRef = mRefBaseDatabase.child(Constants.USERS_LOCATION).child(mCurrentUser.getUid());
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                currentUser.setName(name);
                currentUser.setCountry(country);
                currentUser.setCity(city);
                currentUser.setAboutMe(aboutMe);
                currentUser.setListVisitedCountries(visitedCountries);
                currentUserRef.setValue(currentUser);
                mPresenter.onSuccessSetUserInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
