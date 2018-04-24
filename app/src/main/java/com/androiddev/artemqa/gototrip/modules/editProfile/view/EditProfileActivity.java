package com.androiddev.artemqa.gototrip.modules.editProfile.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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
                mPresenter.onBtnSaveClicked(mEtName.getText().toString(),mEtCountry.getText().toString()
                        ,mEtCity.getText().toString(),mEtAboutMe.getText().toString(),mEtCountyList.getText().toString());
            }
        });
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileActivityPermissionsDispatcher.chooseImgWithPermissionCheck(EditProfileActivity.this);
            }
        });
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void chooseImg() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constants.PICK_AVATAR_USER_EDIT_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_AVATAR_USER_EDIT_USER) {
                Uri selectedImageUri = data.getData();
                byte[] compressOriginalPhotoByteArray = Utils.compressPhotoOriginal(selectedImageUri, this);
                byte[] compressThumbnailPhotoByteArray = Utils.compressPhotoThumbnail(selectedImageUri,this);
                mPresenter.saveAvatar(compressOriginalPhotoByteArray,compressThumbnailPhotoByteArray);
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
            Glide.with(getApplicationContext()).load(currentUser.getUriAvatar()).into(mIvAvatar);
        }

    }

    @Override
    public void showErrorUploadPhoto() {
        Toast.makeText(EditProfileActivity.this, R.string.er_failed_avatar_edit_profile_a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessSetUserInfo() {
        Toast.makeText(EditProfileActivity.this,R.string.toast_success_saving_info_edit_profile_a,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EditProfileActivityPermissionsDispatcher.onRequestPermissionsResult(EditProfileActivity.this,requestCode,grantResults);
    }
}
