package com.epitrack.guardioes.view.welcome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.NetworkUtility;
import com.epitrack.guardioes.view.base.BaseFragmentActivity;
import com.epitrack.guardioes.view.account.CreateAccountActivity;
import com.epitrack.guardioes.view.account.LoginActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class WelcomeActivity extends BaseFragmentActivity {

    //@Bind(R.id.page_indicator)
    //CirclePageIndicator pageIndicator;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private Tracker mTracker;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.welcome);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setContext(this);
        simpleRequester.updateContext();

        viewPager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager(), this, Welcome.values()));

        //pageIndicator.setViewPager(viewPager);
    }

    @OnClick(R.id.button_login)
    public void onLogin() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Welcome Enter Button")
                .build());

        if (NetworkUtility.isOnline(getApplicationContext())) {

            navigateTo(LoginActivity.class);

        } else {

            new DialogBuilder(WelcomeActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.internet_fail)
                    .positiveText(R.string.ok)
                    .show();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Welcome Screen - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnClick(R.id.button_create_account)
    public void onCreateAccount() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Welcome Create Account Button")
                .build());

        if (NetworkUtility.isOnline(getApplicationContext())) {

            navigateTo(CreateAccountActivity.class);

        } else {

            new DialogBuilder(WelcomeActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.internet_fail)
                    .positiveText(R.string.ok)
                    .show();
        }
    }
}
