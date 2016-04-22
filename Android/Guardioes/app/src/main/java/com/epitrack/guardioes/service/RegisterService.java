package com.epitrack.guardioes.service;

import android.app.IntentService;
import android.content.Intent;

import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.Logger;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * @author Igor Morais
 */
public class RegisterService extends IntentService {

    private static final String TAG = RegisterService.class.getSimpleName();

    private static final String NAME = RegisterService.class.getSimpleName();

    public RegisterService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        try {

            final String token = InstanceID.getInstance(this)
                                           .getToken(Constants.Push.SENDER,
                                                     GoogleCloudMessaging.INSTANCE_ID_SCOPE);

            // TODO: Send to server
            // TODO: Save the token

            // Notify with broadcast
            //LocalBroadcastManager.getInstance(this).sendBroadcast();

        } catch (IOException e) {
            Logger.logError(TAG, e.getMessage());
        }
    }
}
