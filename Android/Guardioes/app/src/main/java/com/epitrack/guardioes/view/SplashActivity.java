package com.epitrack.guardioes.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.FileHandler;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.service.QuickstartPreferences;
import com.epitrack.guardioes.service.RegistrationIntentService;
import com.epitrack.guardioes.view.base.BaseActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

/**
 * @author Igor Morais
 */
public class SplashActivity extends BaseActivity implements Runnable {

    private static final long WAIT_TIME = 1500;

    private final Handler handler = new Handler();

    SharedPreferences sharedPreferences = null;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private GoogleCloudMessaging gcm;
    private String regId;
    private static String SENDER_ID = "997325640691";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";
    private String gcmToken;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash);

        loadDirectory();

        SingleUser.getInstance().setVersionBuild(BuildConfig.VERSION_NAME);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                gcmToken = sharedPreferences.getString(Constants.Push.SENDER_ID, "");
            }
        };
        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
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

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void run() {

        sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
        String prefUserToken = sharedPreferences.getString(Constants.Pref.PREFS_NAME, "");

        if (!prefUserToken.equals("")) {

            SingleUser singleUser = SingleUser.getInstance();
            JSONObject jsonObject = new JSONObject();

            singleUser.setUserToken(prefUserToken);

            SimpleRequester sendPostRequest = new SimpleRequester();
            sendPostRequest.setUrl(Requester.API_URL + "user/lookup/");
            sendPostRequest.setJsonObject(jsonObject);
            sendPostRequest.setMethod(Method.GET);
            sendPostRequest.setContext(SplashActivity.this);

            String jsonStr;
            try {
                jsonStr = sendPostRequest.execute(sendPostRequest).get();

                jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString() == "true") {
                    navigateTo(WelcomeActivity.class);
                } else {

                    JSONObject jsonObjectUser = jsonObject.getJSONObject("data");

                    singleUser.setNick(jsonObjectUser.getString("nick"));
                    singleUser.setEmail(jsonObjectUser.getString("email"));
                    singleUser.setGender(jsonObjectUser.getString("gender"));
                    singleUser.setId(jsonObjectUser.getString("id"));
                    singleUser.setRace(jsonObjectUser.getString("race"));
                    singleUser.setDob(jsonObjectUser.getString("dob"));
                    singleUser.setUserToken(jsonObjectUser.getString("token"));

                    try {
                        singleUser.setPath(jsonObjectUser.getString("file"));
                    } catch (Exception e) {
                    }

                    try {
                        singleUser.setImage(jsonObjectUser.getInt("picture"));
                    } catch (Exception e) {

                    }

                    singleUser.setHashtags(jsonObjectUser.getJSONArray("hashtags"));

                    SharedPreferences settings = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUserToken());
                    editor.commit();

                    navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            } catch (Exception e) {
                navigateTo(WelcomeActivity.class);
            }

        } else {
            navigateTo(WelcomeActivity.class);
        }
    }

    private void loadDirectory() {

        getExternalCacheDir();

        new FileHandler().createDirectory(Constants.DIRECTORY_TEMP);
    }
}
