package com.epitrack.guardioes.view.game.welcome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.GameMapActivity;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class WelcomeGameActivity extends BaseAppCompatActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.page_indicator)
    CirclePageIndicator pageIndicator;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.welcome_game);

        viewPager.setAdapter(new WelcomeGamePagerAdapter(getSupportFragmentManager(), this, WelcomeGame.values()));

        pageIndicator.setViewPager(viewPager);
    }

    @OnClick(R.id.button_next)
    public void onNext() {
        navigateTo(GameMapActivity.class);
    }
}
