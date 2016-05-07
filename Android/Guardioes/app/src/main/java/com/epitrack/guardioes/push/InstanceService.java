package com.epitrack.guardioes.push;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * @author Igor Morais
 */
public class InstanceService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegisterService.class));
    }
}
