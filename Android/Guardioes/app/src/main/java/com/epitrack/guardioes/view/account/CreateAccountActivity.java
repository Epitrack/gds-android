package com.epitrack.guardioes.view.account;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.Mask;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.push.HashReceiver;
import com.epitrack.guardioes.push.RegisterService;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.ItemAdapter;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.epitrack.guardioes.view.welcome.TermActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class CreateAccountActivity extends BaseAppCompatActivity {

    private static final int REQUEST_MAIL = 6669;

    private static final int MIN_CHAR_NICKNAME = 3;

    private static final String SOCIAL_FRAGMENT = "social_fragment";

    @Bind(R.id.linear_layout_social_account)
    LinearLayout linearLayoutSocial;

    @Bind(R.id.linear_layout_account)
    ScrollView layoutAccount;

    @Email(messageResId = R.string.validation_mail)
    @Bind(R.id.edit_text_mail)
    EditText editTextMail;

    @Password(messageResId = R.string.validation_password)
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;

    @Length(min = MIN_CHAR_NICKNAME, trim = true, messageResId = R.string.validation_length)
    @Bind(R.id.edit_text_name)
    EditText editTextNickname;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Bind(R.id.edit_text_birth_date)
    EditText editTextBirthDate;

    @Bind(R.id.spinner_race)
    Spinner spinnerRace;

    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;

    private SocialFragment socialFragment;
    private Validator validator;
    private State state = State.SOCIAL;

    private final LoadDialog loadDialog = new LoadDialog();

    private User user = new User();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.create_account);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        load();

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHandler());

        getSocialFragment().setListener(new AccountHandler());
    }

    private void load() {

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

        spinnerGender.setAdapter(new ItemAdapter(this, getResources().getStringArray(R.array.gender_array)));

        spinnerRace.setAdapter(new ItemAdapter(this, getResources().getStringArray(R.array.race_array)));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.privacy, menu);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(HashReceiver.HASH_RECEIVER));

        getTracker().setScreenName("Create Account Screen - " + this.getClass().getSimpleName());
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

            if (state == State.SOCIAL) {
                return super.onOptionsItemSelected(item);

            } else {

                handlerState();
            }

        } else {

            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void onPrivacy(final MenuItem item) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.privacy);
        ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.image_button_close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {

        if (state == State.SOCIAL) {
            super.onBackPressed();

        } else {
            handlerState();
        }
    }

    private void previous() {
        state = State.getBy(state.getId() - 1);
    }

    private void next() {
        state = State.getBy(state.getId() + 1);
    }

    private void handlerState() {

        if (state == State.ACCOUNT) {
            onPreviousAnimation(linearLayoutSocial, layoutAccount);
        }
    }

    @OnClick(R.id.button_mail)
    public void onMail() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Create Account by Email Button")
                .build());

        navigateForResult(TermActivity.class, REQUEST_MAIL);
    }

    @OnClick(R.id.button_create_account)
    public void onNext() {
        validator.validate();
    }

    private void onPreviousAnimation(final View visibleView, final View invisibleView) {

        final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        slideIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(final Animation animation) {
                visibleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                previous();

                invisibleView.setVisibility(View.INVISIBLE);

                invisibleView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        final Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        visibleView.startAnimation(slideIn);

        invisibleView.startAnimation(slideOut);
    }

    private void onNextAnimation(final View visibleView, final View invisibleView) {

        final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        slideIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(final Animation animation) {
                visibleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                next();

                invisibleView.setVisibility(View.INVISIBLE);

                invisibleView.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        final Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        visibleView.startAnimation(slideIn);

        invisibleView.startAnimation(slideOut);
    }

    private class AccountHandler implements AccountListener {

        @Override
        public void onCancel() {

        }

        @Override
        public void onError() {

        }

        @Override
        public void onNotFound(final User user) {

            editTextNickname.setText(user.getNick());
            editTextMail.setText(user.getEmail());

            linearLayoutSocial.setVisibility(View.INVISIBLE);
            layoutAccount.setVisibility(View.VISIBLE);

            setUser(user);
        }

        @Override
        public void onSuccess(final User user) {

            new DialogBuilder(CreateAccountActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.already_registered)
                    .positiveText(R.string.ok)
                    .show();
        }
    }

    private SocialFragment getSocialFragment() {

        if (socialFragment == null) {
            socialFragment = (SocialFragment) getFragmentManager().findFragmentByTag(SOCIAL_FRAGMENT);
        }

        return socialFragment;
    }

    @OnClick(R.id.button_create_account)
    public void onCreateAccount() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Register Yourself Button")
                .build());

        validator.validate();
    }

    private HashReceiver receiver = new HashReceiver() {

        public void onHash(final String hash) {

            user.setNick(editTextNickname.getText().toString().trim());
            user.setDob(editTextBirthDate.getText().toString().trim().toLowerCase());
            String gender = spinnerGender.getSelectedItem().toString().substring(0, 1);
            user.setGender(gender.toUpperCase());
            user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());
            user.setEmail(editTextMail.getText().toString().trim().toLowerCase());
            user.setPassword(editTextPassword.getText().toString().trim());
            user.setGcmToken(hash);

            new UserRequester(CreateAccountActivity.this).createAccount(user, new RequestListener<User>() {

                @Override
                public void onStart() {

                }

                @Override
                public void onError(final Exception e) {

                    loadDialog.dismiss();

                    Toast.makeText(CreateAccountActivity.this, R.string.erro_new_user, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(final User type) {

                    loadDialog.dismiss();

                    navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            });
        }
    };

    private class ValidationHandler implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {

            if (state == State.ACCOUNT) {

                boolean dobIsFail = false;

                if (!DateFormat.isDate(editTextBirthDate.getText().toString().trim().toLowerCase())) {
                    dobIsFail = true;
                } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) < 12) {
                    dobIsFail = true;
                } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) > 120) {
                    dobIsFail = true;
                }

                if (dobIsFail) {
                    new DialogBuilder(CreateAccountActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.dob_invalid)
                            .positiveText(R.string.ok)
                            .show();
                } else {

                    loadDialog.show(getFragmentManager(), LoadDialog.TAG);

                    startService(new Intent(CreateAccountActivity.this, RegisterService.class));
                }
            }
        }

        @Override
        public void onValidationFailed(final List<ValidationError> errorList) {

            for (final ValidationError error : errorList) {

                final String message = error.getCollatedErrorMessage(CreateAccountActivity.this);

                final View view = error.getView();

                if (view instanceof EditText) {

                    ((EditText) view).setError(message);
                }
            }
        }
    }

    public void setUser(final User user) {
        this.user = user;
    }

    enum State {

        SOCIAL(1), ACCOUNT(2);

        private final int id;

        State(final int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static State getBy(final int id) {

            for (final State state : State.values()) {

                if (state.getId() == id) {
                    return state;
                }
            }

            throw new IllegalArgumentException("The State has not found.");
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == REQUEST_MAIL) {

            if (resultCode == RESULT_OK) {
                linearLayoutSocial.setVisibility(View.INVISIBLE);
                layoutAccount.setVisibility(View.VISIBLE);
            }

        } else {

            getSocialFragment().onActivityResult(requestCode, resultCode, intent);
        }
    }
}
