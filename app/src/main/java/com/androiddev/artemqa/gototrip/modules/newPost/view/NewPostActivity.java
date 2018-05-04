package com.androiddev.artemqa.gototrip.modules.newPost.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.modules.PickLocationActivity;
import com.androiddev.artemqa.gototrip.modules.main.view.MainActivity;
import com.androiddev.artemqa.gototrip.modules.newPost.interfaces.OnAddImageClickListener;
import com.androiddev.artemqa.gototrip.modules.newPost.presenter.NewPostPresenter;
import com.androiddev.artemqa.gototrip.modules.newPost.ContractNewPost;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class NewPostActivity extends BaseActivity implements ContractNewPost.View, View.OnClickListener, LocationEngineListener {
    private TextInputEditText mEtTitle;
    private EditText mEtTextPost;
    private Button mBtnAddPost;
    private ContractNewPost.Presenter mPresenter;
    private MapView mMvPostLocation;
    private MapboxMap mMapboxMap;
    private LocationLayerPlugin mLocationLayerPlugin;
    private LocationEngine mLocationEngine;
    private Uri imageUri;
    private List<String> uriSelectImages;
    private GridView mGrAddImagesPost;
    private AddPostImagesGridAdapter mImagesGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initView(savedInstanceState);
//        initImagePicker();
        initAddImagesGridView();
    }

    private void initAddImagesGridView() {
        mGrAddImagesPost = findViewById(R.id.gv_add_images_new_post_a);
        mImagesGridAdapter = new AddPostImagesGridAdapter(NewPostActivity.this);
        mImagesGridAdapter.setOnAddImageClickListener(new OnAddImageClickListener() {
            @Override
            public void onAddImageClicked(int maxImagePicked) {
                checkPermissionsPhoto(maxImagePicked);
            }
        });
        mGrAddImagesPost.setAdapter(mImagesGridAdapter);
    }

//    private void initImagePicker() {
//        final View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
//        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
//        mBottomSheetDialog.setContentView(bottomSheet);
//        mBottomSheetDialog.setCancelable(true);
//        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
//
//        CustomImagePicker imagePicker = new CustomImagePicker();
//        imagePicker.setHeight(100);
//        imagePicker.setWidth(100);
//        ImageAdapter adapter = imagePicker.getAdapter(NewPostActivity.this);
//
//        TwoWayGridView gridview = bottomSheet.findViewById(R.id.gridview);
//        gridview.getLayoutParams().height = Utils.dpToPx(NewPostActivity.this, 200);
//        gridview.setNumRows(2);
//        gridview.setAdapter(adapter);
//        gridview.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
//            public void onItemClick(TwoWayAdapterView parent, View v, int position, long id) {
//                imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//                //Do what you want with the returned imageUri here.
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        mBottomSheetDialog.show();
//    }

    private void initView(Bundle savedInstanceState) {
        mPresenter = new NewPostPresenter();
        mPresenter.attachView(this);
        mMvPostLocation = findViewById(R.id.mv_new_place_location_new_post_a);
        mMvPostLocation.onCreate(savedInstanceState);
        mMvPostLocation.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                checkPermissionsMap();
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        Intent intent = new Intent(NewPostActivity.this, PickLocationActivity.class);
                        startActivityForResult(intent, Constants.INTENT_PICK_LOCATION_FROM_NEW_POST);
                    }
                });
            }
        });
        mEtTitle = findViewById(R.id.et_title_new_post_a);
        mEtTextPost = findViewById(R.id.et_description_text_new_post_a);
        mBtnAddPost = findViewById(R.id.btn_add_post_new_post_a);
        mBtnAddPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_post_new_post_a:
                //
                break;
        }
    }

    private void checkPermissionsPhoto(int maxImagePicked) {
        Dexter.withActivity(NewPostActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (ContextCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                mPresenter.onPhotoPostClicked(maxImagePicked);
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

    public void choosePhoto(int maxImagePicked) {
        Matisse.from(NewPostActivity.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(maxImagePicked)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(Constants.PICK_PHOTO_NEW_POST);
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
                                mLocationLayerPlugin.setRenderMode(RenderMode.NORMAL);
                                mLocationLayerPlugin.setCameraMode(CameraMode.TRACKING);
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
        if (lastLocation != null) {
            setCameraPosition(lastLocation);
        } else {
            mLocationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 20));
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
            switch (requestCode) {
//                case Constants.PICK_PHOTO_NEW_POST:
//                    Uri selectedImageUri = data.getData();
//                    CropImage.activity(selectedImageUri)
//                            .start(this);
//                    break;
//
//                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
//                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                    imageUri = result.getUri();
////                    byte[] compressOriginalPhotoByteArray = Utils.compressPhotoOriginal(resultUri, this);
////                    mPresenter.savePhoto(compressOriginalPhotoByteArray);
//                    break;
//
//                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
//                    Exception error = CropImage.getActivityResult(data).getError();
//                    break;

                case Constants.INTENT_PICK_LOCATION_FROM_NEW_POST:
                    LatLng pickedLocation = data.getExtras()
                            .getBundle(Constants.INTENT_PICKED_LOCATION_BUNDLE)
                            .getParcelable(Constants.INTENT_PICKED_LOCATION_LATLNG);
                    setNewPositionOnMap(pickedLocation);
                    break;

                case Constants.PICK_PHOTO_NEW_POST:
                    List<String> strUriSelectedImage = new ArrayList<>();
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    for (Uri uriImg : mSelected) {
                        strUriSelectedImage.add(uriImg.toString());
                    }
                    mImagesGridAdapter.addItems(strUriSelectedImage);
                    break;
            }
        }
    }

    private void setNewPositionOnMap(LatLng pickedLocation) {
        if (mMapboxMap != null) {
            mMapboxMap.removeAnnotations();
            mMapboxMap.addMarker(new MarkerOptions().position(pickedLocation));
            mLocationLayerPlugin.setCameraMode(CameraMode.NONE);
            CameraPosition position = new CameraPosition.Builder()
                    .target(pickedLocation)
                    .zoom(16)
                    .build();

            mMapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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
