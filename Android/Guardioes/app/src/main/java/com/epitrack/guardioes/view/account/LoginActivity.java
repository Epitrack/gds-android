package com.epitrack.guardioes.view.account;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.push.HashReceiver;
import com.epitrack.guardioes.push.RegisterService;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

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

    private final LoadDialog loadDialog = new LoadDialog();

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
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(HashReceiver.HASH_RECEIVER));

        getTracker().setScreenName("Login Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        super.onPause();
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

    private HashReceiver receiver = new HashReceiver() {

        public void onHash(final String hash) {

            final String mail = editTextMail.getText().toString().toLowerCase().trim();
            final String password = editTextPassword.getText().toString().trim();

            new UserRequester(LoginActivity.this).login(mail, password, hash, new RequestListener<User>() {

                @Override
                public void onStart() {

                }

                @Override
                public void onError(final Exception e) {

                    new DialogBuilder(LoginActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.generic_error_login)
                            .positiveText(R.string.ok)
                            .show();
                }

                @Override
                public void onSuccess(final User user) {

                    navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                   Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            });
        }
    };

    private class ValidationHandler implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {

            loadDialog.show(getFragmentManager(), LoadDialog.TAG);

            startService(new Intent(LoginActivity.this, RegisterService.class));
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
