package com.epitrack.guardioes.helper;

import android.content.Context;
import android.net.ConnectivityManager;

import com.epitrack.guardioes.manager.LocationManager;

/**
 * @author Igor Morais
 */
public final class NetworkUtility {

    private static final String TAG = Utility.class.getSimpleName();

    private NetworkUtility() {

    }

    public static boolean isOnline(Context context) {
        boolean bReturn = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                bReturn = true;
            } else {
                bReturn = false;
            }
        } catch (Exception e) {
            bReturn = false;
        }
        return bReturn;
    }

    public static boolean isLocationServiceOn(Context context) {
        boolean bReturn = false;
        try {
            LocationManager cm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (cm.isEnabled()) {
                bReturn = true;
            } else {
                bReturn = false;
            }
        } catch (Exception e) {
            bReturn = false;
        }
        return bReturn;
    }
}
