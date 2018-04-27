package com.androiddev.artemqa.gototrip.modules.editProfile.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.common.models.User;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.editProfile.EditProfileContract;
import com.androiddev.artemqa.gototrip.modules.editProfile.presenter.EditProfilePresenter;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity implements EditProfileContract.View {

    TextInputEditText mEtName, mEtCountry, mEtCity, mEtAboutMe, mEtCountyList;
    CardView mCvBtnSave;
    EditProfileContract.Presenter mPresenter;
    CircleImageView mIvAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initForm();

    }

    private void initForm() {
        mPresenter = new EditProfilePresenter();

        mPresenter.attachView(this);
        mPresenter.viewIsReady();
        mIvAvatar = findViewById(R.id.iv_avatar_user_edit_profile_a);
        mEtName = findViewById(R.id.et_name_edit_profile_a);
        mEtCountry = findViewById(R.id.et_country_edit_profile_a);
        mEtCity = findViewById(R.id.et_city_edit_profile_a);
        mEtAboutMe = findViewById(R.id.et_about_edit_profile_a);
        mEtCountyList = findViewById(R.id.et_list_country_edit_profile_a);
        mCvBtnSave = findViewById(R.id.cv_btn_save_edit_profile_a);
        mCvBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onBtnSaveClicked(mEtName.getText().toString(), mEtCountry.getText().toString()
                        , mEtCity.getText().toString(), mEtAboutMe.getText().toString(), mEtCountyList.getText().toString());
            }
        });
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissions();
            }
        });
    }

    public void chooseImg() {
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setAction(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Выберите аватар"), Constants.PICK_AVATAR_USER_EDIT_USER);
    }

    private void permissions() {
        Dexter.withActivity(EditProfileActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                chooseImg();
                            } else
                                Toast.makeText(getApplicationContext(), "All permissions are NOT granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_AVATAR_USER_EDIT_USER) {
                Uri selectedImageUri = data.getData();
                CropImage.activity(selectedImageUri)
                        .setAspectRatio(1, 1)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                byte[] compressOriginalPhotoByteArray = Utils.compressPhotoOriginal(resultUri, this);
                byte[] compressThumbnailPhotoByteArray = Utils.compressPhotoThumbnail(resultUri, this);
                mPresenter.saveAvatar(compressOriginalPhotoByteArray, compressThumbnailPhotoByteArray);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void updateUI(User currentUser) {

        if (currentUser.getName() != null) {
            mEtName.setText(currentUser.getName());

        }
        if (currentUser.getCity() != null) {
            mEtCity.setText(currentUser.getCity());

        }
        if (currentUser.getCountry() != null) {
            mEtCountry.setText(currentUser.getCountry());

        }
        if (currentUser.getListVisitedCountries() != null) {
            mEtCountyList.setText(currentUser.getListVisitedCountries());

        }
        if (currentUser.getAboutMe() != null) {
            mEtAboutMe.setText(currentUser.getAboutMe());

        }
        if (currentUser.getUriAvatar() != null) {
            Utils.loadImage(EditProfileActivity.this,currentUser.getUriAvatarThumbnail(),mIvAvatar);
        }

    }

    @Override
    public void showErrorUploadPhoto() {
        Toast.makeText(EditProfileActivity.this, R.string.er_failed_avatar_edit_profile_a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessSetUserInfo() {
        Toast.makeText(EditProfileActivity.this, R.string.toast_success_saving_info_edit_profile_a, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
