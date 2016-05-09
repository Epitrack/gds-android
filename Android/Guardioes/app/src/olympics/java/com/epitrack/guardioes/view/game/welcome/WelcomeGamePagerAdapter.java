package com.epitrack.guardioes.view.game.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.epitrack.guardioes.helper.Constants;

/**
 * @author Igor Morais
 */
public class WelcomeGamePagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final WelcomeGame[] welcomeGameArray;

    public WelcomeGamePagerAdapter(final FragmentManager fragmentManager, final Context context, final WelcomeGame[] welcomeGameArray) {
        super(fragmentManager);

        this.context = context;
        this.welcomeGameArray = welcomeGameArray;
    }

    @Override
    public int getCount() {
        return welcomeGameArray.length;
    }

    @Override
    public Fragment getItem(final int position) {

        final Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.Bundle.WELCOME_GAME, WelcomeGame.getBy(position + 1));

        return Fragment.instantiate(context, WelcomeGamePageFragment.class.getName(), bundle);
    }
}
