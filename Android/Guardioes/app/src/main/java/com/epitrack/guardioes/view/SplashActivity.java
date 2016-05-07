package com.epitrack.guardioes.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.FileHandler;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.AuthRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.BaseActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.google.android.gms.analytics.HitBuilders;

/**
 * @author Igor Morais
 */
public class SplashActivity extends BaseActivity implements Runnable {

    private static final long WAIT_TIME = 1000;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash);

        loadDirectory();

        SingleUser.getInstance().setVersionBuild(BuildConfig.VERSION_NAME);
    }

    @Override
    public void onResume() {
        super.onResume();

        handler.postDelayed(this, WAIT_TIME);

        getTracker().setScreenName("Splash Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(this);
    }

    @Override
    public void run() {

        final User user = new PrefManager(SplashActivity.this).get(Constants.Pref.USER, User.class);

        if (user == null) {

            navigateTo(WelcomeActivity.class);

        } else {

            new AuthRequester(this).loadAuth(new RequestListener<User>() {

                @Override
                public void onStart() {

                }

                @Override
                public void onError(final Exception e) {
                    navigateTo(WelcomeActivity.class);
                }

                @Override
                public void onSuccess(final User user) {

                    navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                   Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            });
        }
    }

    private void loadDirectory() {

        getExternalCacheDir();

        new FileHandler().createDirectory(Constants.DIRECTORY_TEMP);
    }
}
