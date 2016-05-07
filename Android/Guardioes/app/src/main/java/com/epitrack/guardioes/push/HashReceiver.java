package com.epitrack.guardioes.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Igor Morais
 */
public abstract class HashReceiver extends BroadcastReceiver {

    public static final String HASH = "hash";

    public static final String HASH_RECEIVER = HashReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        onHash(intent.getStringExtra(HASH));
    }

    public abstract void onHash(String hash);
}
