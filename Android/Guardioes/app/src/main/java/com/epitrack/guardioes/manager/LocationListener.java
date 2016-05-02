package com.epitrack.guardioes.manager;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;

/**
 * @author Igor Morais
 */
public interface LocationListener {

    void onConnect(Bundle bundle);

    void onSuspend(int i);

    void onFail(ConnectionResult connectionResult);

    void onLastLocation(Location location);

    void onLocation(Location location);
}
