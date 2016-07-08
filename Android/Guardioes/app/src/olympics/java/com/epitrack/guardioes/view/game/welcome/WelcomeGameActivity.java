package com.epitrack.guardioes.view.game.welcome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
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

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

        } else {

            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @OnClick(R.id.button_next)
    public void onNext() {

        if (new PrefManager(this).getBoolean(Constants.Pref.WELCOME_GAME, true)) {

            if (new PrefManager(this).putBoolean(Constants.Pref.WELCOME_GAME, false)) {
                navigateTo(GameMapActivity.class);
            }

        } else {

            finish();
        }
    }
}
