package com.epitrack.guardioes.view.welcome;

import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.view.ViewPager;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.view.account.CreateAccountActivity;
import com.epitrack.guardioes.view.account.LoginActivity;
import com.epitrack.guardioes.view.base.BaseFragmentActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class WelcomeActivity extends BaseFragmentActivity {

    @Bind(R.id.page_indicator)
    CirclePageIndicator pageIndicator;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        MultiDex.install(getBaseContext());
        setContentView(R.layout.welcome);

        final SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setContext(this);
        simpleRequester.updateContext();

        viewPager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager(), this, Welcome.values()));

        pageIndicator.setViewPager(viewPager);
    }

    @OnClick(R.id.button_login)
    public void onLogin() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Welcome Enter Button")
                .build());

        navigateTo(LoginActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Welcome Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnClick(R.id.button_create_account)
    public void onCreateAccount() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Welcome Create Account Button")
                .build());

        navigateTo(CreateAccountActivity.class);
    }
}
