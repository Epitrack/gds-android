package com.epitrack.guardioes.manager;

import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.helper.Logger;
import com.karumi.dexter.Dexter;
import com.koushikdutta.ion.Ion;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import io.fabric.sdk.android.Fabric;

/**
 * @author Igor Morais
 */
public final class Application extends android.app.Application {

    private static final String TAG = Application.class.getSimpleName();

    private static final String NAME = "47b83fbefd5092a.crt";

    private static final String TWITTER_KEY = "l4t5P03ZR3mbBON7HHLWhgSrS";
    private static final String TWITTER_SECRET = "8Vi40vWK3s4kqViKMKPJJFO5bLsrFbvzRqhDbsy6mZQH7pkVbe";

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getBaseContext());

        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build(),
                          new Twitter(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET)));

        Dexter.initialize(this);

        loadAuth();
    }

    public void loadAuth() {

        try {

            final Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(getAssets().open(NAME));

            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            keyStore.load(null, null);
            keyStore.setCertificateEntry("certificate", certificate);

            final TrustManagerFactory trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManager.init(keyStore);

            final SSLContext context = SSLContext.getInstance("TLS");

            context.init(null, trustManager.getTrustManagers(), null);

            Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setSSLContext(context);
            Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustManager.getTrustManagers());

        } catch (final Exception e) {
            Logger.logError(TAG, e.getMessage());
        }
    }
}
