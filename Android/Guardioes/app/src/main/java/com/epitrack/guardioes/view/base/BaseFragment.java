/*
 * Copyright 2015 Igor Morais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epitrack.guardioes.view.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.Logger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public abstract class BaseFragment extends Fragment implements INavigate {

    private static final String TAG = BaseFragment.class.getSimpleName();

    private Tracker tracker;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        ButterKnife.unbind(this);
    }
    
    protected final void bind(final View view) {
        ButterKnife.bind(this, view);
    }

    protected final ActionBar getSupportActionBar() {

        if (getActivity() instanceof AppCompatActivity) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        
        return null;
    }

    @Override
    public final void navigateTo(final Class<? extends Activity> activityClass) {
        startActivity(new Intent(getActivity(), activityClass));
    }

    @Override
    public final void navigateTo(final Class<? extends Activity> activityClass, final int flags) {

        final Intent intent = new Intent(getActivity(), activityClass);
        
        intent.setFlags(flags);

        startActivity(intent);
    }

    @Override
    public final void navigateTo(final Class<? extends Activity> activityClass, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public final void navigateTo(final Class<? extends Activity> activityClass, final int flags, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public final void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode) {
        startActivityForResult(new Intent(getActivity(), activityClass), requestCode);
    }

    @Override
    public final void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode, final int flags) {

        final Intent intent = new Intent(getActivity(), activityClass);
        
        intent.setFlags(flags);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public final void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public final void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode, final int flags, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    protected final void setShowTitle(final boolean display) {

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {

            Logger.logError(TAG, "The actionBar is null.");

        } else {

            actionBar.setDisplayShowTitleEnabled(display);
        }
    }

    protected final void setShowLogo(final boolean display) {

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {

            Logger.logError(TAG, "The actionBar is null.");

        } else {

            actionBar.setDisplayUseLogoEnabled(display);
        }
    }

    protected Tracker getTracker() {

        if (tracker == null) {
            tracker = GoogleAnalytics.getInstance(getActivity()).newTracker(R.xml.analytics);
        }

        return tracker;
    }
}
