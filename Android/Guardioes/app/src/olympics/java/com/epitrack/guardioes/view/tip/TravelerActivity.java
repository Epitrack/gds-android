package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class TravelerActivity extends BaseAppCompatActivity {

    private static final String URL = "https://github.com";

    @Bind(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.traveler);

        webView.setWebViewClient(new WebClient());
        webView.loadUrl(URL);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Traveler Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            return false;
        }
    }
}
