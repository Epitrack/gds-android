package com.epitrack.guardioes.service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegisterService.class));
    }
}