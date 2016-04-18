package com.epitrack.guardioes.view.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class LoginActivity extends BaseAppCompatActivity implements SocialAccountListener {

    @Bind(R.id.linear_layout_social_login)
    LinearLayout linearLayoutSocialLogin;

    @Bind(R.id.linear_layout_login)
    LinearLayout linearLayoutLogin;

    @Email(messageResId = R.string.validation_mail)
    @Bind(R.id.edit_text_mail)
    EditText editTextMail;

    @Password(messageResId = R.string.validation_password)
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;

    private boolean inLogin;
    private Validator validator;
    SharedPreferences sharedPreferences = null;
    private SharedPreferences shpGCMToken;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.login);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {
            throw new IllegalArgumentException("The actionBar is null.");
        }

        actionBar.setDisplayShowTitleEnabled(false);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHandler());

        shpGCMToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
        String prefUserToken = sharedPreferences.getString(Constants.Pref.PREFS_NAME, "");

        sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_IMAGE, 0);
        String preImage = sharedPreferences.getString(Constants.Pref.PREFS_IMAGE, "");

        sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_IMAGE_USER_TOKEN, 0);
        String prefImagUserToken = sharedPreferences.getString(Constants.Pref.PREFS_IMAGE_USER_TOKEN, "");

        if (!prefUserToken.equals("")) {

            SingleUser singleUser = SingleUser.getInstance();
            JSONObject jsonObject = new JSONObject();

            singleUser.setUser_token(prefUserToken);

            SimpleRequester sendPostRequest = new SimpleRequester();
            sendPostRequest.setUrl(Requester.API_URL + "user/lookup/");
            sendPostRequest.setJsonObject(jsonObject);
            sendPostRequest.setMethod(Method.GET);
            sendPostRequest.setContext(this);

            String jsonStr;
            try {
                jsonStr = sendPostRequest.execute(sendPostRequest).get();

                jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString() == "true") {
                    Toast.makeText(getApplicationContext(), "Erro ao fazer o login. - " + jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

                } else {

                    JSONObject jsonObjectUser = jsonObject.getJSONObject("data");

                    singleUser.setNick(jsonObjectUser.getString("nick"));
                    singleUser.setEmail(jsonObjectUser.getString("email"));
                    singleUser.setGender(jsonObjectUser.getString("gender"));
                    //singleUser.setPicture(jsonObjectUser.getString("picture").toString());
                    singleUser.setId(jsonObjectUser.getString("id"));
                    singleUser.setRace(jsonObjectUser.getString("race"));
                    singleUser.setDob(jsonObjectUser.getString("dob"));
                    singleUser.setUser_token(jsonObjectUser.get("token").toString());
                    jsonObject.put("gcm_token", shpGCMToken.getString(Constants.Push.SENDER_ID, ""));

                    if (prefUserToken == prefImagUserToken) {
                        singleUser.setPicture(prefUserToken);
                    } else {
                        try {
                            singleUser.setPicture(jsonObjectUser.get("picture").toString());
                        } catch (Exception e) {
                            singleUser.setPicture("0");
                        }
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
                e.printStackTrace();
            }

        } else {

            final SimpleRequester sendPostRequest = new SimpleRequester();

            sendPostRequest.setContext(this);
            sendPostRequest.updateContext();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Login Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (inLogin) {
                handlerAnimation();

            } else {
                return super.onOptionsItemSelected(item);
            }

        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if (inLogin) {
            handlerAnimation();

        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.text_view_forgot_password)
    public void onForgotPassword() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Forgot Password Button")
                .build());

        navigateTo(ForgotPasswordActivity.class);
    }


    private String forgotPassword(String email) {

        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("email", email);

            SimpleRequester sendPostRequest = new SimpleRequester();
            sendPostRequest.setUrl(Requester.API_URL + "user/forgot-password");
            sendPostRequest.setJsonObject(jsonObject);
            sendPostRequest.setMethod(Method.POST);
            sendPostRequest.setContext(this);

            String jsonStr = sendPostRequest.execute(sendPostRequest).get();

            jsonObject = new JSONObject(jsonStr);

            return jsonObject.get("message").toString();

        } catch (Exception e) {
            return "Não foi possível enviar o e-mail. Tente novamente em alguns minutos";
        }
    }

    @OnClick(R.id.button_mail)
    public void onLoginAnimation() {

        final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        slideIn.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(final Animation animation) {
                linearLayoutLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                inLogin = true;

                linearLayoutSocialLogin.setVisibility(View.INVISIBLE);

                linearLayoutSocialLogin.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        final Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        linearLayoutLogin.startAnimation(slideIn);
        linearLayoutSocialLogin.startAnimation(slideOut);
    }

    private void handlerAnimation() {

        final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        slideIn.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(final Animation animation) {
                linearLayoutSocialLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                inLogin = false;

                linearLayoutLogin.setVisibility(View.INVISIBLE);

                linearLayoutLogin.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        final Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        linearLayoutSocialLogin.startAnimation(slideIn);
        linearLayoutLogin.startAnimation(slideOut);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Houston, we have a problem", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Cancel..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {

        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                       Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @OnClick(R.id.button_login)
    public void onLogin() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Login by Email Button")
                .build());

        validator.validate();
    }

    private class ValidationHandler implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {

            final User user = new User();

            user.setEmail(editTextMail.getText().toString().trim().toLowerCase());
            user.setPassword(editTextPassword.getText().toString().trim());

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("email", user.getEmail());
                jsonObject.put("password", user.getPassword());


                final SimpleRequester sendPostRequest = new SimpleRequester();
                sendPostRequest.setUrl(Requester.API_URL + "user/login");
                sendPostRequest.setJsonObject(jsonObject);
                sendPostRequest.setMethod(Method.POST);

                String jsonStr = sendPostRequest.execute(sendPostRequest).get();

                jsonObject = new JSONObject(jsonStr);

                if (jsonObject.getBoolean("error")) {

                    new DialogBuilder(LoginActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.generic_error_login)
                            .positiveText(R.string.ok)
                            .show();

                } else {

                    JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                    SingleUser singleUser = SingleUser.getInstance();
                    singleUser.setNick(jsonObjectUser.getString("nick"));
                    singleUser.setEmail(jsonObjectUser.getString("email"));
                    singleUser.setGender(jsonObjectUser.getString("gender"));
                    singleUser.setId(jsonObjectUser.getString("id"));
                    singleUser.setRace(jsonObjectUser.getString("race"));
                    singleUser.setDob(jsonObjectUser.getString("dob"));
                    singleUser.setUser_token(jsonObject.get("token").toString());
                    singleUser.setPicture(jsonObjectUser.get("picture").toString());
                    singleUser.setHashtags(jsonObjectUser.getJSONArray("hashtags"));

                    sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUserToken());
                    editor.commit();

                    navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onValidationFailed(final List<ValidationError> errorList) {

            for (final ValidationError error : errorList) {

                final String message = error.getCollatedErrorMessage(LoginActivity.this);

                final View view = error.getView();

                if (view instanceof EditText) {

                    ((EditText) view).setError(message);
                }
            }
        }
    }
}
