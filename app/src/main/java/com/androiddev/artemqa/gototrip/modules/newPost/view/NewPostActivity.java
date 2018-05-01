package com.androiddev.artemqa.gototrip.modules.newPost.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.editProfile.view.EditProfileActivity;
import com.androiddev.artemqa.gototrip.modules.newPost.presenter.NewPostPresenter;
import com.androiddev.artemqa.gototrip.modules.newPost.ContractNewPost;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

public class NewPostActivity extends BaseActivity implements ContractNewPost.View, View.OnClickListener, LocationEngineListener {
    private TextInputEditText mEtTitle;
    private EditText mEtTextPost;
    private Button mBtnAddPost;
    private ContractNewPost.Presenter mPresenter;
    private MapView mMvPostLocation;
    private RecyclerView mRvListPhotos;
    private MapboxMap mMapboxMap;
    private LocationLayerPlugin mLocationLayerPlugin;
    private LocationEngine mLocationEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mPresenter = new NewPostPresenter();
        mPresenter.attachView(this);
        mMvPostLocation = findViewById(R.id.mv_new_place_location_new_post_a);
        mMvPostLocation.onCreate(savedInstanceState);
        mMvPostLocation.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                NewPostActivity.this.mMapboxMap = mapboxMap;
                checkPermissionsMap();
            }
        });
        mRvListPhotos = findViewById(R.id.rv_images_place_new_post_a);
        mEtTitle = findViewById(R.id.et_title_new_post_a);
        mEtTextPost = findViewById(R.id.et_description_text_new_post_a);
        mBtnAddPost = findViewById(R.id.btn_add_post_new_post_a);
        mBtnAddPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_post_new_post_a:
                checkPermissionsPhoto();
                break;
        }
    }

    private void checkPermissionsPhoto() {
        Dexter.withActivity(NewPostActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                mPresenter.onPhotoPostClicked();
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

    public void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Выберите фото"), Constants.PICK_PHOTO_NEW_POST);
    }

    public void checkPermissionsMap() {

        Dexter.withActivity(NewPostActivity.this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                initializeLocationEngine();
                                mLocationLayerPlugin = new LocationLayerPlugin(mMvPostLocation, mMapboxMap, mLocationEngine);
                                mLocationLayerPlugin.setLocationLayerEnabled(true);
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

    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {
        mLocationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        mLocationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        mLocationEngine.activate();

        Location lastLocation = mLocationEngine.getLastLocation();
        if (lastLocation == null) {
            mLocationEngine.addLocationEngineListener(this);
        } else {
            setCameraPosition(lastLocation);
        }
    }

    private void setCameraPosition(Location location) {
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    public void showErrorUploadPhoto() {
        Toast.makeText(NewPostActivity.this, R.string.er_failed_upload_photo_new_post_a, Toast.LENGTH_SHORT).show();
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
    public void showErrorNoPhotoPost() {
        Toast.makeText(NewPostActivity.this, R.string.er_no_uploaded_photo_new_post_a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessAddPost() {
        Toast.makeText(NewPostActivity.this, R.string.success_add_post_new_post_a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPostPhoto(String urlPhoto) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_PHOTO_NEW_POST) {
                Uri selectedImageUri = data.getData();
                CropImage.activity(selectedImageUri)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                byte[] compressOriginalPhotoByteArray = Utils.compressPhotoOriginal(resultUri, this);
                mPresenter.savePhoto(compressOriginalPhotoByteArray);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (mLocationLayerPlugin != null) {
            mLocationLayerPlugin.onStart();
        }
        mMvPostLocation.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMvPostLocation.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMvPostLocation.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationEngine != null) {
            mLocationEngine.removeLocationUpdates();
        }
        if (mLocationLayerPlugin != null) {
            mLocationLayerPlugin.onStop();
        }
        mMvPostLocation.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMvPostLocation.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMvPostLocation.onLowMemory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationEngine != null) {
            mLocationEngine.deactivate();
        }
        mMvPostLocation.onDestroy();
        mPresenter.detachView();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        mLocationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            setCameraPosition(location);
            mLocationEngine.removeLocationEngineListener(this);
        }
    }
}
