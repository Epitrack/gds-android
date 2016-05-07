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
public class WelcomePagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final Welcome[] welcomeArray;

    public WelcomePagerAdapter(final FragmentManager fragmentManager, final Context context, final Welcome[] welcomeArray) {
        super(fragmentManager);

        this.context = context;
        this.welcomeArray = welcomeArray;
    }

    @Override
    public int getCount() {
        return welcomeArray.length;
    }

    @Override
    public Fragment getItem(final int position) {

        final Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.Bundle.WELCOME, Welcome.getBy(position + 1));

        return Fragment.instantiate(context, WelcomePageFragment.class.getName(), bundle);
    }
}
