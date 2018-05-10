package com.androiddev.artemqa.gototrip.modules.newPost.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.common.BaseActivity;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.androiddev.artemqa.gototrip.helper.GlideV4ImageEngine;
import com.androiddev.artemqa.gototrip.helper.GridViewScrollable;
import com.androiddev.artemqa.gototrip.helper.Utils;
import com.androiddev.artemqa.gototrip.modules.newPost.interfaces.OnImageClickListener;
import com.androiddev.artemqa.gototrip.modules.pickLocation.PickLocationActivity;

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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewPostActivity extends BaseActivity implements ContractNewPost.View, View.OnClickListener, LocationEngineListener {
    private EditText mEtTitle;
    private EditText mEtTextPost;
    private ContractNewPost.Presenter mPresenter;
    private MapView mMvPostLocation;
    private MapboxMap mMapboxMap;
    private LocationLayerPlugin mLocationLayerPlugin;
    private LocationEngine mLocationEngine;
    private Toolbar mToolbar;
    private ConstraintLayout mBtnDatePicker;
    private GridViewScrollable mGrAddImagesPost;
    private AddPostImagesGridAdapter mImagesGridAdapter;
    private TextView mTvCurrentDate;
    private Calendar mCurrentDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    MaterialDialog dialogAddingPost;
    LatLng pickedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        initView(savedInstanceState);
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
        mImagesGridAdapter.setOnImageClickListener(new OnImageClickListener() {
            @Override
            public void OnItemClicked(int itemPosition) {
                imagesDialog(itemPosition);
            }
        });
        mGrAddImagesPost.setAdapter(mImagesGridAdapter);
    }

    private void imagesDialog(int itemPosition) {
        MaterialDialog imageDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.images_dialog_new_post_a, false)
                .show();

        View view = imageDialog.getCustomView();
        Button delete = view.findViewById(R.id.btn_delete_images_dialog_new_post_a);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagesGridAdapter.removeImage(itemPosition);
                imageDialog.dismiss();
            }
        });

    }

    private void initView(Bundle savedInstanceState) {
        mPresenter = new NewPostPresenter();
        mPresenter.attachView(this);
        mTvCurrentDate = findViewById(R.id.tv_date_value_new_post_a);
        mCurrentDate = Calendar.getInstance();
        mToolbar = findViewById(R.id.toolbar_new_post_a);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mBtnDatePicker = findViewById(R.id.btn_set_date_new_post_a);
        mBtnDatePicker.setOnClickListener(this);
        setDatePickerListener();
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
    }

    private void setDatePickerListener() {
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCurrentDate.set(Calendar.YEAR, year);
                mCurrentDate.set(Calendar.MONTH, monthOfYear);
                mCurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setInitialDateTime();
            }
        };
    }

    private void setInitialDateTime() {

        mTvCurrentDate.setText(Utils.millisTimeToReadbleString(mCurrentDate.getTimeInMillis()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_date_new_post_a:
                new DatePickerDialog(NewPostActivity.this, mDateSetListener,
                        mCurrentDate.get(Calendar.YEAR),
                        mCurrentDate.get(Calendar.MONTH),
                        mCurrentDate.get(Calendar.DAY_OF_MONTH))
                        .show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pick_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_confirm_menu_pick_location) {
            setDataForAddPost();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void setDataForAddPost() {
        String title = mEtTitle.getText().toString();
        String textPost = mEtTextPost.getText().toString();
        Double latitude;
        Double longitude;
        if (pickedLocation != null) {
        latitude = pickedLocation.getLatitude();
        longitude = pickedLocation.getLongitude();
        } else {
            latitude = mLocationEngine.getLastLocation().getLatitude();
            longitude = mLocationEngine.getLastLocation().getLongitude();
        }
        Long datePost = mCurrentDate.getTimeInMillis();
        List<String> uriImagesPicked = mImagesGridAdapter.getItemsFromAdapter();
        mPresenter.onButtonAddPostClicked(title,textPost,uriImagesPicked,latitude,longitude,datePost);
    }

    public void choosePhoto(int maxImagePicked) {
        Matisse.from(NewPostActivity.this)
                .choose(MimeType.of(MimeType.JPEG,MimeType.PNG))
                .countable(true)
                .maxSelectable(maxImagePicked)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.35f)
                .imageEngine(new GlideV4ImageEngine())
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
    public void compressImage(String uriImage) {
        byte[] compressPhotoOriginal = Utils.compressPhotoOriginal(Uri.parse(uriImage),NewPostActivity.this);
        byte [] compressPhotoThumbnail  = Utils.compressPhotoThumbnail(Uri.parse(uriImage),NewPostActivity.this );
        mPresenter.addPhoto(compressPhotoOriginal,compressPhotoThumbnail);
    }

    @Override
    public void incrementDialog() {
                dialogAddingPost.incrementProgress(1);
    }

    @Override
    public void showSuccessUploadPost() {
        dialogAddingPost.setContent(R.string.dialog_add_post_success_upload_new_post_a);
        dialogAddingPost.getActionButton(DialogAction.POSITIVE).setEnabled(true);
        dialogAddingPost.getActionButton(DialogAction.NEGATIVE).setEnabled(false);
    }


    public void showProgressAddingPost(int imageCount) {
        dialogAddingPost = new MaterialDialog.Builder(this)
                .title(R.string.dialog_add_post_title_new_post_a)
                .content(R.string.dialog_add_post_content_new_post_a)
                .progress(false, imageCount, true)
                .positiveText(R.string.dialog_add_post_btn_positive_new_post_a)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(NewPostActivity.this, "Сохраняем пост", Toast.LENGTH_SHORT).show();
                    }
                })
                .positiveColor(Color.GRAY)
                .negativeText(R.string.dialog_add_post_btn_negative_new_post_a)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(NewPostActivity.this, "Отмена кнопкой", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

                dialogAddingPost.getActionButton(DialogAction.POSITIVE).setEnabled(false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case Constants.INTENT_PICK_LOCATION_FROM_NEW_POST:
                    pickedLocation = data.getExtras()
                            .getBundle(Constants.INTENT_PICKED_LOCATION_BUNDLE)
                            .getParcelable(Constants.INTENT_PICKED_LOCATION_LATLNG);
                    setNewPositionOnMap(pickedLocation);
                    break;

                case Constants.PICK_PHOTO_NEW_POST:
                    List<Uri> uriPickedImages = Matisse.obtainResult(data);

                    mImagesGridAdapter.addItems(uriPickedImages);
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
