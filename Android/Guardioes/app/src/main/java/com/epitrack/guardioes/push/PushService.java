package com.epitrack.guardioes.push;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;

/**
 * @author Igor Morais
 */
public class PushService extends GcmListenerService {

    private static final String TAG = PushService.class.getSimpleName();

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String NOTIFICATION = "notification";

    @Override
    public void onMessageReceived(final String sender, final Bundle bundle) {

        try {

            final JsonNode json = new ObjectMapper().readTree(bundle.getString(NOTIFICATION));

            notify(new Message(json.get(TITLE).asText(), json.get(MESSAGE).asText()));

        } catch (final IOException e) {
            Logger.logDebug(TAG, e.getMessage());
        }
    }

    private void notify(final Message message) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(message.getTitle())
                .setContentText(message.getMessage());

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, builder.build());
    }

    private class Message {

        private String title;
        private String message;

        public Message(final String title, final String message) {
            this.title = title;
            this.message = message;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }
    }
}
