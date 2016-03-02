package com.epitrack.guardioes.view.welcome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.account.CreateAccountActivity;
import com.epitrack.guardioes.view.account.SocialLoginActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Miqueias Lopes
 */
public class TermsAction extends BaseAppCompatActivity {

    private Tracker mTracker;

    @Bind(R.id.btn_accept_terms)
    Button buttonAccpetTerms;

    @Bind(R.id.btn_not_accept_terms)
    Button buttonNotAcceptTerms;


    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.terms_of_use_action);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

    }

    @OnClick(R.id.btn_accept_terms)
    public void acceptTerms() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Accept Terms of Use")
                .build());

        String bundle = getIntent().getStringExtra(Constants.Bundle.EMAIL);

        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.Bundle.EMAIL, Constants.Bundle.EMAIL);

        if (bundle == null) {
            navigateTo(SocialLoginActivity.class);
        } else {
            if (bundle.equals("email")) {
                navigateTo(CreateAccountActivity.class, bundle1);
            } else {
                navigateTo(SocialLoginActivity.class);
            }
        }
    }

    @OnClick(R.id.btn_not_accept_terms)
    public void notAcceptTerms() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Not Accept Terms of Use")
                .build());
        super.onBackPressed();
    }


    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Terms of Use Screen Action - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        super.onBackPressed();
        return true;
    }
}

