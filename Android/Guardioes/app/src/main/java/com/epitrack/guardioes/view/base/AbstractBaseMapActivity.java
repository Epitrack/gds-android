package com.epitrack.guardioes.view.base;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.LocationListener;
import com.epitrack.guardioes.manager.LocationManager;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.LocationUtility;
import com.epitrack.guardioes.helper.Logger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * @author Igor Morais
 */
public abstract class AbstractBaseMapActivity extends BaseAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    private static final String TAG = AbstractBaseMapActivity.class.getSimpleName();

    private static final long DEFAULT_ZOOM = 12;

    private MarkerOptions markerOption;
    private Marker marker;

    private GoogleMap map;

    private LocationManager locationHandler;

    public LocationManager getLocationHandler() {
        return locationHandler;
    }

    public static long getDefaultZoom() {
        return DEFAULT_ZOOM;
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        locationHandler = new LocationManager(this, this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        hasPermission();

        setMap(map);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (LocationUtility.hasPermission(this)) {

            if (LocationUtility.isEnabled(this)) {

                locationHandler.connect();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationHandler.isConnected()) {
            locationHandler.disconnect();
        }
    }

    @Override
    public void onLastLocation(final Location location) {

        final LatLng latLng = LocationUtility.toLatLng(location);

        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {

                marker = getMap().addMarker(loadMarkerOption().position(latLng));

                onAnimationEnd(latLng);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onLocation(final Location location) {

        if (marker == null) {
            Logger.logDebug(TAG, "The user marker is null.");

        } else {
            marker.setPosition(LocationUtility.toLatLng(location));
        }
    }

    @Override
    public void onConnect(final Bundle bundle) {

    }

    @Override
    public void onSuspend(final int i) {

    }

    @Override
    public void onFail(final ConnectionResult connectionResult) {

    }

    private MarkerOptions loadMarkerOption() {

        if (markerOption == null) {
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
        }

        return markerOption;
    }

    private void setMap(final GoogleMap map) {
        map.setOnMarkerClickListener(this);

        this.map = map;
    }

    protected void onAnimationEnd(final LatLng latLng) {

    }

    public final GoogleMap getMap() {
        return map;
    }

    public final Marker getMarker() {
        return marker;
    }

    private void hasPermission() {

        if (!Dexter.isRequestOngoing()) {

            Dexter.checkPermissions(new MultiplePermissionsListener() {

                @Override
                public void onPermissionsChecked(final MultiplePermissionsReport permissionReport) {

                    if (permissionReport.areAllPermissionsGranted()) {

                        if (LocationUtility.isEnabled(AbstractBaseMapActivity.this)) {
                            locationHandler.connect();

                        } else {

                            new DialogBuilder(AbstractBaseMapActivity.this).load()
                                    .content(R.string.location_disabled)
                                    .cancelable(false)
                                    .negativeText(R.string.not_now)
                                    .positiveText(R.string.setting_upper)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {

                                        @Override
                                        public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }

                                    }).show();
                        }
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> permissionList, final PermissionToken permissionToken) {

                }

            }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
}
