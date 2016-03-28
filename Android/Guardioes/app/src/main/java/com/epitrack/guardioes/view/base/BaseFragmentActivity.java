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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.epitrack.guardioes.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.apache.commons.collections4.map.ReferenceMap;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements IFragment, INavigate, ActivityListener {

    private Tracker tracker;

    private Map<String, Fragment> fragmentMap;

    @Override
    public void setContentView(final int layout) {
        
        onTheme();
        
        super.setContentView(layout);

        onContentView();
    }

    @Override
    public void setContentView(final View view) {
        
        onTheme();
        
        super.setContentView(view);

        onContentView();
    }

    @Override
    public void setContentView(final View view, final ViewGroup.LayoutParams layoutParam) {
        
        onTheme();
        
        super.setContentView(view, layoutParam);

        onContentView();
    }
    
    @Override
    public void onTheme() {

    }

    @Override
    public void onContentView() {
        ButterKnife.bind(this);
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(getLayout());
    }

    @Override
    public int add(final Class<? extends Fragment> fragmentClass) {
        return add(fragmentClass, fragmentClass.getSimpleName(), null, false);
    }

    @Override
    public int add(final Class<? extends Fragment> fragmentClass, final String tag) {
        return add(fragmentClass, tag, null, false);
    }

    @Override
    public int add(final Class<? extends Fragment> fragmentClass, final String tag, final Bundle bundle) {
        return add(fragmentClass, tag, bundle, false);
    }

    @Override
    public int add(final Class<? extends Fragment> fragmentClass, final String tag, final boolean addToBackStack) {
        return add(fragmentClass, tag, null, addToBackStack);
    }

    @Override
    public int add(final Class<? extends Fragment> fragmentClass, final Bundle bundle, final boolean addToBackStack) {
        return add(fragmentClass, fragmentClass.getSimpleName(), bundle, addToBackStack);
    }

    @Override
    public int add(final Class<? extends Fragment> fragmentClass, final String tag, final Bundle bundle, final boolean addToBackStack) {

        if (fragmentClass == null) {
            throw new IllegalArgumentException("The fragmentClass cannot be null.");
        }

        if (tag == null) {
            throw new IllegalArgumentException("The tag cannot be null.");
        }

        if (fragmentMap == null) {
            fragmentMap = new ReferenceMap<>();
        }

        Fragment fragment = fragmentMap.get(tag);

        if (fragment == null) {

            fragment = Fragment.instantiate(this, fragmentClass.getName(), bundle);

            fragmentMap.put(tag, fragment);
        }

        if (addToBackStack) {

            return getFragmentManager().beginTransaction()
                                       .add(getLayout(), fragment, tag)
                                       .addToBackStack(tag)
                                       .commit();
        }

        return getFragmentManager().beginTransaction()
                                   .add(getLayout(), fragment, tag)
                                   .commit();
    }

    @Override
    public int replace(final Class<? extends Fragment> fragmentClass) {
        return replace(fragmentClass, fragmentClass.getSimpleName(), null, false);
    }

    @Override
    public int replace(final Class<? extends Fragment> fragmentClass, final String tag) {
        return replace(fragmentClass, tag, null, false);
    }

    @Override
    public int replace(final Class<? extends Fragment> fragmentClass, final String tag, final Bundle bundle) {
        return replace(fragmentClass, tag, bundle, false);
    }

    @Override
    public int replace(final Class<? extends Fragment> fragmentClass, final String tag, final boolean addToBackStack) {
        return replace(fragmentClass, tag, null, addToBackStack);
    }

    @Override
    public int replace(final Class<? extends Fragment> fragmentClass, final Bundle bundle, final boolean addToBackStack) {
        return replace(fragmentClass, fragmentClass.getSimpleName(), bundle, addToBackStack);
    }

    @Override
    public int replace(final Class<? extends Fragment> fragmentClass, final String tag, final Bundle bundle, final boolean addToBackStack) {

        if (fragmentClass == null) {
            throw new IllegalArgumentException("The fragmentClass cannot be null.");
        }

        if (tag == null) {
            throw new IllegalArgumentException("The tag cannot be null.");
        }

        if (fragmentMap == null) {
            fragmentMap = new ReferenceMap<>();
        }

        Fragment fragment = fragmentMap.get(tag);

        if (fragment == null) {

            fragment = Fragment.instantiate(this, fragmentClass.getName(), bundle);

            fragmentMap.put(tag, fragment);
        }

        if (addToBackStack) {

            return getFragmentManager().beginTransaction()
                                       .replace(getLayout(), fragment, tag)
                                       .addToBackStack(tag)
                                       .commit();
        }

        return getFragmentManager().beginTransaction()
                                   .replace(getLayout(), fragment, tag)
                                   .commit();
    }

    @Override
    public int remove(final Fragment fragment) {

        return getFragmentManager().beginTransaction()
                                   .remove(fragment)
                                   .commit();
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final int flags) {

        final Intent intent = new Intent(this, activityClass);
        
        intent.setFlags(flags);

        startActivity(intent);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final int flags, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode) {
        startActivityForResult(new Intent(this, activityClass), requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode, final int flags) {

        final Intent intent = new Intent(this, activityClass);
        
        intent.setFlags(flags);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode, final int flags, final Bundle bundle) {

        final Intent intent = new Intent(this, activityClass);
        
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    protected Tracker getTracker() {

        if (tracker == null) {
            tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.analytics);
        }

        return tracker;
    }
}
