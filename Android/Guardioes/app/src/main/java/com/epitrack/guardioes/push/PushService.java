package com.epitrack.guardioes.push;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;

/**
 * @author Igor Morais
 */
public class PushService extends GcmListenerService {

    private static final String TAG = PushService.class.getSimpleName();

    private static final int ID = 0;
    private static final String MESSAGE = "message";

    @Override
    public void onMessageReceived(final String sender, final Bundle bundle) {

        final String message = bundle.getString(MESSAGE);

        try {

            notify(new ObjectMapper().readValue(message, Message.class));

        } catch (final IOException e) {
            Logger.logDebug(TAG, e.getMessage());
        }
    }

    private void notify(final Message message) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody());

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(ID, builder.build());
    }

    private class Message {

        private String title;
        private String body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
}
