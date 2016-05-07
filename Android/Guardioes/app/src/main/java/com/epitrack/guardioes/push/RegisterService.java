package com.epitrack.guardioes.push;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.epitrack.guardioes.helper.Logger;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * @author Igor Morais
 */
public class RegisterService extends IntentService {

    private static final String TAG = RegisterService.class.getSimpleName();

    private static final String SENDER = "997325640691";

    public RegisterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        try {

            final String hash = InstanceID.getInstance(this).getToken(SENDER, GoogleCloudMessaging.INSTANCE_ID_SCOPE);

            final Intent i = new Intent(HashReceiver.HASH_RECEIVER);

            i.putExtra(HashReceiver.HASH, hash);

            LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }
    }
}
