package com.epitrack.guardioes.view.menu.help;

import android.os.Bundle;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

/**
 * @author Miquéias Lopes 30/09/15.
 */
public class TermActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.term);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Terms of Use Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
