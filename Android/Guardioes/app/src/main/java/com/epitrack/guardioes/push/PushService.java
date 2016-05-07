package com.epitrack.guardioes.push;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * @author Igor Morais
 */
public class PushService extends GcmListenerService {

    private static final String MESSAGE = "message";

    @Override
    public void onMessageReceived(final String sender, final Bundle bundle) {

        final String message = bundle.getString(MESSAGE);
    }
}
