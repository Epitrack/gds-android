package com.epitrack.guardioes.view.tip;

import android.os.Bundle;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

public class SafeSexActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.safe_sex);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Prevention Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
