package com.epitrack.guardioes.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.FileHandler;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.SurveyRequester;
import com.epitrack.guardioes.request.base.AuthRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.BaseActivity;
import com.epitrack.guardioes.view.survey.StateActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.analytics.HitBuilders;

import java.util.Calendar;
import java.util.Locale;

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

        AppLinkData.fetchDeferredAppLinkData(this,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        // Processar dados do link de aplicativo
                    }
                }
        );
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

        final Language language = new PrefManager(SplashActivity.this).get(Constants.Pref.LANGUAGE, Language.class);

        if (language == null) {

            new LanguageDialog().setListener(new LanguageDialog.ILanguage() {

                @Override
                public void onLanguage(final Language language) {

                    if (new PrefManager(SplashActivity.this).put(Constants.Pref.LANGUAGE, language)) {

                        loadLang(new Locale(language.getLocale()));

                        loadAuth();
                    }
                }

            }).show(getFragmentManager(), LanguageDialog.TAG);

        } else {

            loadLang(new Locale(language.getLocale()));

            loadAuth();
        }
    }

    private void loadLang(final Locale locale) {

        Locale.setDefault(locale);

        final Resources resource = getResources();

        final Configuration configuration = resource.getConfiguration();

        configuration.locale = locale;

        resource.updateConfiguration(configuration, resource.getDisplayMetrics());
    }

    private void loadAuth() {

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
                    hasSurvey();
                }
            });
        }
    }

    private void hasSurvey() {

        new SurveyRequester(this).hasSurvey(Calendar.getInstance(), new RequestListener<Boolean>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {
                navigateTo(HomeActivity.class);
            }

            @Override
            public void onSuccess(final Boolean has) {

                if (has) {

                    navigateTo(HomeActivity.class);

                } else {

                    final Bundle bundle = new Bundle();

                    bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);

                    navigateTo(StateActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, bundle);
                }
            }
        });
    }
    private void loadDirectory() {

        getExternalCacheDir();

        new FileHandler().createDirectory(Constants.DIRECTORY_TEMP);
    }
}
