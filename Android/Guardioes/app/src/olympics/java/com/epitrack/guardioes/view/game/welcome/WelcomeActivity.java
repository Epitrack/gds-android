package com.epitrack.guardioes.view.game.welcome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.account.LoginActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class WelcomeActivity extends BaseAppCompatActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.page_indicator)
    CirclePageIndicator pageIndicator;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.welcome_game);

        viewPager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager(), this, Welcome.values()));

        pageIndicator.setViewPager(viewPager);
    }

    @OnClick(R.id.button_login)
    public void onNext() {
        navigateTo(LoginActivity.class);
    }
}
