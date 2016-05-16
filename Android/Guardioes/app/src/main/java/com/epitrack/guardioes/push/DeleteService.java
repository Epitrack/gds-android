package com.epitrack.guardioes.push;

import android.app.IntentService;
import android.content.Intent;

import com.epitrack.guardioes.helper.Logger;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * @author Igor Morais
 */
public class DeleteService extends IntentService {

    private static final String TAG = DeleteService.class.getSimpleName();

    public DeleteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

        try {

            InstanceID.getInstance(getApplicationContext()).deleteInstanceID();

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }
    }
}
