package com.androiddev.artemqa.gototrip.modules.pickLocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.androiddev.artemqa.gototrip.R;
import com.androiddev.artemqa.gototrip.helper.Constants;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickLocationActivity extends AppCompatActivity implements LocationEngineListener {
    private LocationEngine mLocationEngine;
    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private LocationLayerPlugin mLocationPlugin;
    private ImageView hoveringMarker;
    private Toolbar mToolbar;
    private Button mBtnSelectLocation;
    private Marker droppedMarker;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String TAG = "LocationPickerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        mToolbar = findViewById(R.id.toolbar_pick_location_a);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mLocationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        mLocationEngine.activate();
        mMapView = findViewById(R.id.mv_pick_location_pick_loc_a);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                mMapboxMap = mapboxMap;
                enableLocationPlugin();
                initSearchFab();
                setUpSource();
            }
        });

        hoveringMarker = new ImageView(this);
        hoveringMarker.setImageResource(R.drawable.ic_location);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                100,
                100, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);
        mMapView.addView(hoveringMarker);

        mBtnSelectLocation = findViewById(R.id.select_location_button);
        mBtnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMapboxMap != null) {
                    if (droppedMarker == null) {
                        float coordinateX = hoveringMarker.getLeft() + (hoveringMarker.getWidth() / 2);
                        float coordinateY = hoveringMarker.getBottom();
                        float[] coords = new float[]{coordinateX, coordinateY};
                        final Point latLng = Point.fromLngLat(mMapboxMap.getProjection().fromScreenLocation(
                                new PointF(coords[0], coords[1])).getLongitude(), mMapboxMap.getProjection().fromScreenLocation(
                                new PointF(coords[0], coords[1])).getLatitude());
                        hoveringMarker.setVisibility(View.GONE);

                        mBtnSelectLocation.setBackgroundColor(
                                ContextCompat.getColor(PickLocationActivity.this, R.color.colorAccent));
                        mBtnSelectLocation.setText(getResources().getString(R.string.btn_choosed_location_pick_location_a));

                        Icon icon = IconFactory.getInstance(PickLocationActivity.this).fromResource(R.drawable.red_marker);

                        droppedMarker = mMapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latLng.latitude(), latLng.longitude())));
                        Toast.makeText(PickLocationActivity.this, "выбрана точка" + String.valueOf(latLng.latitude()) + String.valueOf(latLng.longitude()), Toast.LENGTH_LONG).show();
                        reverseGeocode(latLng);
                    } else {
                        mMapboxMap.removeMarker(droppedMarker);
                        mBtnSelectLocation.setBackgroundColor(
                                ContextCompat.getColor(PickLocationActivity.this, R.color.colorPrimary));
                        mBtnSelectLocation.setText(getResources().getString(R.string.btn_choose_location_pick_location_a));
                        hoveringMarker.setVisibility(View.VISIBLE);
                        droppedMarker = null;
                    }
                }
            }
        });
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
            if (droppedMarker != null) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.INTENT_PICKED_LOCATION_LATLNG, droppedMarker.getPosition());
                intent.putExtra(Constants.INTENT_PICKED_LOCATION_BUNDLE,bundle);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(PickLocationActivity.this, R.string.toast_choose_location_pick_location_a, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearchFab() {
        FloatingActionButton searchFab = findViewById(R.id.fab_location_search);
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(PickLocationActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                    new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())});

            GeoJsonSource source = mMapboxMap.getSourceAs(geojsonSourceLayerId);
            if (source != null) {
                source.setGeoJson(featureCollection);
            }

            CameraPosition newCameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                    .zoom(14)
                    .build();
            mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition), 5000);
        }
    }

    private void setUpSource() {
        GeoJsonSource geoJsonSource = new GeoJsonSource(geojsonSourceLayerId);
        mMapboxMap.addSource(geoJsonSource);
    }

    @SuppressLint("MissingPermission")
    private void enableLocationPlugin() {

        initializeLocationEngine();
        mLocationPlugin = new LocationLayerPlugin(mMapView, mMapboxMap, mLocationEngine);
        mLocationPlugin.setLocationLayerEnabled(true);

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
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (mLocationPlugin != null) {
            mLocationPlugin.onStart();
        }
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationEngine != null) {
            mLocationEngine.removeLocationUpdates();
        }
        if (mLocationPlugin != null) {
            mLocationPlugin.onStop();
        }
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationEngine != null) {
            mLocationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        mLocationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationEngine.removeLocationEngineListener(this);
    }

    private void reverseGeocode(final Point point) {
        try {

            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(Constants.MAPBOX_ACCESS_TOKEN)
                    .query(Point.fromLngLat(point.longitude(), point.latitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                    List<CarmenFeature> results = response.body().features();
                    if (results.size() > 0) {
                        CarmenFeature feature = results.get(0);

                        if (droppedMarker != null) {
                            droppedMarker.setSnippet(feature.placeName());
                            mMapboxMap.selectMarker(droppedMarker);
                        }

                    } else {
                        if (droppedMarker != null) {
                            droppedMarker.setSnippet("NO RESULTS");
                            mMapboxMap.selectMarker(droppedMarker);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Log.e(TAG, "Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            Log.e(TAG, "Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }
}
