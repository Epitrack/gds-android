package com.epitrack.guardioes.view.welcome;

import android.content.Intent;
import android.os.Bundle;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.OnClick;

/**
 * @author Miqueias Lopes
 */
public class TermActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.terms_of_use_action);
    }

    @OnClick(R.id.button_no)
    public void onNoAccept() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Not Accept Terms of Use")
                .build());

        finish();
    }

    @OnClick(R.id.button_accept)
    public void onAccept() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Accept Terms of Use")
                .build());

        final Intent intent = new Intent();

        intent.putExtra(Constants.Bundle.TYPE, getIntent().getIntExtra(Constants.Bundle.TYPE, Integer.MIN_VALUE));

        setResult(RESULT_OK, intent);

        finish();
    }
}
