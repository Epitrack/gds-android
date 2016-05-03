package com.epitrack.guardioes.view.welcome;

import android.os.Bundle;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.view.account.CreateAccountActivity;
import com.epitrack.guardioes.view.account.SocialLoginActivity;
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

    @OnClick(R.id.btn_not_accept_terms)
    public void notAcceptTerms() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Not Accept Terms of Use")
                .build());

        finish();
    }

    @OnClick(R.id.btn_accept_terms)
    public void acceptTerms() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Accept Terms of Use")
                .build());

        final String mail = getIntent().getStringExtra(Constants.Bundle.EMAIL);

        if (mail == null) {
            navigateTo(SocialLoginActivity.class);

        } else {

            if (mail.equals(Constants.Bundle.EMAIL)) {

                final Bundle bundle = new Bundle();
                bundle.putString(Constants.Bundle.EMAIL, Constants.Bundle.EMAIL);

                navigateTo(CreateAccountActivity.class, bundle);

            } else {
                navigateTo(SocialLoginActivity.class);
            }
        }
    }
}
