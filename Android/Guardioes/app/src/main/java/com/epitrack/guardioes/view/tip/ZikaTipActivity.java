package com.epitrack.guardioes.view.tip;

import android.os.Bundle;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Miqueias Lopes
 */
public class ZikaTipActivity extends BaseAppCompatActivity {

    @Bind(R.id.zika_content)
    TextView zikaContent;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.zika_info);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Zika Tip Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
