package com.epitrack.guardioes.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Miqueias Lopes
 */
public final class LocationUtility {

    private static final String TAG = LocationHelper.class.getSimpleName();

    private LocationUtility() {

    }

    public static LatLng toLatLng(final double latitude, final double longitude) {
        return new LatLng(latitude, longitude);
    }

    public static LatLng toLatLng(final Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static boolean isEnabled(final Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            try {

                return !(Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE) == Settings.Secure.LOCATION_MODE_OFF);

            } catch (final Settings.SettingNotFoundException e) {
                Logger.logError(TAG, e.getMessage());
            }

        } else {

            return !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED).isEmpty();
        }

        return true;
    }

    public static boolean hasPermission(final Context context) {

        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
