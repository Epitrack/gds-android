package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class PreventionActivity extends BaseAppCompatActivity {

    @Bind(R.id.prevention_content_01)
    TextView preventContent;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.prevention);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Prevention Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
