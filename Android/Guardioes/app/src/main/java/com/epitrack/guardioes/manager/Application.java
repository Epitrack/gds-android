package com.epitrack.guardioes.manager;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.utility.Constants;
import com.github.mmin18.layoutcast.LayoutCast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * @author Igor Morais
 */
public final class Application extends android.app.Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QiiN9HpeJCN9tTbYvd1PfA1eE";
    private static final String TWITTER_SECRET = "JDNgByadEC8q7DtEZr24gWjZhT1kTiGGqJlPBRB6x6MgtGVZMU";



    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
