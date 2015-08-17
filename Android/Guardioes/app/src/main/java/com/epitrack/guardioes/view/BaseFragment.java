package com.epitrack.guardioes.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment implements Navigate {

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setDisplayTitle(true);
        setDisplayLogo(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass) {
        startActivity(new Intent(getActivity(), activityClass));
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final int flags) {

        final Intent intent = new Intent(getActivity(), activityClass);
        intent.setFlags(flags);

        startActivity(intent);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateTo(final Class<? extends Activity> activityClass, final int flags, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode) {
        startActivityForResult(new Intent(getActivity(), activityClass), requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode,
                                  final int flags) {

        final Intent intent = new Intent(getActivity(), activityClass);
        intent.setFlags(flags);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode,
                                  final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateForResult(final Class<? extends Activity> activityClass, final int requestCode,
                                  final int flags, final Bundle bundle) {

        final Intent intent = new Intent(getActivity(), activityClass);
        intent.setFlags(flags);
        intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    public final ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public final void setDisplayTitle(final boolean display) {
        getSupportActionBar().setDisplayShowTitleEnabled(display);
    }

    public final void setDisplayLogo(final boolean display) {
        getSupportActionBar().setDisplayUseLogoEnabled(display);
    }
 }
