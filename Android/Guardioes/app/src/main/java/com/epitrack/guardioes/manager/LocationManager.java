package com.epitrack.guardioes.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.epitrack.guardioes.helper.Logger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * @author Igor Morais
 */
public class LocationManager extends BaseManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = LocationManager.class.getSimpleName();

    private static final long INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 5000;
    private static final int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private static final LocationRequest LOCATION_REQUEST = new LocationRequest().setInterval(INTERVAL)
                                                                    .setFastestInterval(FASTEST_INTERVAL)
            .setPriority(PRIORITY);

    private final Handler handler = new Handler();

    private GoogleApiClient locationHandler;

    private final LocationListener listener;

    public LocationManager(final Context context, final LocationListener listener) {
        super(context);

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
    }

    private boolean hasPermission() {

        return ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onConnected(final Bundle bundle) {

        handler.post(new Runnable() {

            @Override
            public void run() {

                listener.onConnect(bundle);
            }
        });

        if (hasPermission()) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    try {

                        final Location location = LocationServices.FusedLocationApi.getLastLocation(locationHandler);

                        listener.onLastLocation(location);

                        if (locationHandler.isConnected()) {

                            LocationServices.FusedLocationApi.requestLocationUpdates(locationHandler, LOCATION_REQUEST, LocationManager.this);
                        }

                    } catch (final SecurityException e) {
                        Logger.logDebug(TAG, "The listener is null.");
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(final int i) {
        locationHandler.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {

        handler.post(new Runnable() {

            @Override
            public void run() {

                listener.onLocation(location);
            }
        });
    }

    public final GoogleApiClient getLocationHandler() {
        return locationHandler;
    }

    public final boolean isEnabled() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            try {

                return Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF;

            } catch (Settings.SettingNotFoundException e) {
                Logger.logDebug(TAG, e.getMessage());
            }

        } else {

            return !Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED).isEmpty();
        }

        return false;
    }

    public final boolean isConnected() {
        return locationHandler == null ? false : locationHandler.isConnected();
    }

    public final void connect() {

        if (locationHandler == null) {

            locationHandler = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        locationHandler.connect();
    }

    public final void disconnect() {
        locationHandler.disconnect();
    }
}
