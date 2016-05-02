package com.epitrack.guardioes.helper;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.epitrack.guardioes.manager.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Igor Morais
 */
public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = LocationHelper.class.getSimpleName();

    private static final long INTERVAL = 0;
    private static final long FASTEST_INTERVAL = 0;
    private static final int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private static final LocationRequest LOCATION_REQUEST = new LocationRequest().setInterval(INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
            .setPriority(PRIORITY);

    private GoogleApiClient locationHandler;

    private final Handler handler = new Handler();

    private final Context context;
    private final List<LocationListener> listenerList = new LinkedList<>();

    public LocationHelper(final Context context) {
       this.context = context;
    }

    public LocationHelper(final Context context, final LocationListener... listenerArray) {
        this.context = context;

        Collections.addAll(listenerList, listenerArray);
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {

        for (final LocationListener listener : listenerList) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    try {

                        final Location location = LocationServices.FusedLocationApi.getLastLocation(locationHandler);

                        listener.onLastLocation(location);

                    } catch (final SecurityException e) {
                        Logger.logDebug(TAG, e.getMessage());
                    }
                }
            });
        }

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(locationHandler, LOCATION_REQUEST, LocationHelper.this);

        } catch (final SecurityException e) {
            Logger.logDebug(TAG, e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(final int i) {

        for (final LocationListener listener : listenerList) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    listener.onSuspend(i);
                }
            });
        }
    }

    @Override
    public void onLocationChanged(final Location location) {

        for (final LocationListener listener : listenerList) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    listener.onLocation(location);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

        for (final LocationListener listener : listenerList) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    listener.onFail(connectionResult);
                }
            });
        }
    }

    public final boolean isConnected() {
        return locationHandler.isConnected();
    }

    public final void connect() {

        if (locationHandler == null) {

            locationHandler = new GoogleApiClient.Builder(context)
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

    public final void addListener(final LocationListener listener) {
        listenerList.add(listener);
    }

    public final void clear() {
        listenerList.clear();
    }

    public final boolean isEnabled() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            try {

                return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF;

            } catch (Settings.SettingNotFoundException e) {
                Logger.logDebug(TAG, e.getMessage());
            }

        } else {

            return !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED).isEmpty();
        }

        return false;
    }
}
