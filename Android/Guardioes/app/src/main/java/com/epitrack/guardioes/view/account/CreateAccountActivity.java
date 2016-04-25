package com.epitrack.guardioes.view.account;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.DTO;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.LocationUtility;
import com.epitrack.guardioes.helper.Mask;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.menu.help.Term;
import com.epitrack.guardioes.view.menu.profile.UserActivity;
import com.epitrack.guardioes.view.welcome.TermActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class CreateAccountActivity extends BaseAppCompatActivity implements SocialAccountListener {

    private static final int MIN_CHAR_NICKNAME = 3;

    private static final String SOCIAL_FRAGMENT = "social_fragment";

    @Bind(R.id.linear_layout_social_account)
    LinearLayout linearLayoutSocial;

    @Bind(R.id.linear_layout_next)
    LinearLayout linearLayoutNext;

    @Bind(R.id.linear_layout_create)
    LinearLayout linearLayoutCreate;

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

    @Bind(R.id.button_mail)
    Button buttonMail;

    //Miquéias Lopes
    @Bind(R.id.spinner_race)
    Spinner spinnerRace;

    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;

    private SocialFragment socialFragment;
    private Validator validator;
    private State state = State.SOCIAL;
    private SharedPreferences sharedPreferences = null;

    private Bundle bundle;
    private String gcmToken;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.create_account);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {
            throw new IllegalArgumentException("The actionBar is null.");
        }

        actionBar.setDisplayShowTitleEnabled(false);

        buttonMail.setEnabled(true);

        getSocialFragment().setEnable(false);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHandler());

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

        getSocialFragment().setEnable(true);
        getSocialFragment().isIsCreateAccount(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gcmToken = sharedPreferences.getString(Constants.Push.SENDER_ID, "");

        editTextBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(final View v, boolean hasFocus) {

                if (hasFocus) {
                    ((EditText) v).setHint(" ex: 01/01/1991");

                } else {
                    ((EditText) v).setHint("");
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.privacy, menu);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Create Account Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());

        String bundle = getIntent().getStringExtra(Constants.Bundle.EMAIL);

        if (bundle != null) {

            if (bundle.equals("email")) {
                onNextAnimation(linearLayoutNext, linearLayoutSocial);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        hideSoftKeyboard();

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

        // custom dialog
        final Dialog dialog = new Dialog(this);
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

    @OnCheckedChanged(R.id.check_box_term)
    public void onCheck(final boolean checked) {

        buttonMail.setEnabled(checked);

        getSocialFragment().setEnable(checked);
    }

    @OnClick(R.id.text_view_term)
    public void onTerm() {
        navigateTo(Term.class);
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        hideSoftKeyboard();

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

        if (state == State.NEXT) {
            onPreviousAnimation(linearLayoutSocial, linearLayoutNext);

        } else if (state == State.CREATE) {
            onPreviousAnimation(linearLayoutNext, linearLayoutCreate);
        }
    }

    @OnClick(R.id.button_mail)
    public void onMail() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Create Account by Email Button")
                .build());

        bundle = new Bundle();

        bundle.putString(Constants.Bundle.EMAIL, Constants.Bundle.EMAIL);

        navigateTo(TermActivity.class, bundle);
    }

    @OnClick(R.id.button_next)
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

    @Override
    public void onError() {

        if (DTO.object != null) {
            if (DTO.object instanceof Bundle) {

                Bundle bundle = (Bundle) DTO.object;

                String typeBundle = bundle.getString(Constants.Bundle.ACCESS_SOCIAL);

                if (typeBundle == Constants.Bundle.GOOGLE) {

                    new DialogBuilder(CreateAccountActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.google_fail)
                            .positiveText(R.string.ok)
                            .show();

                } else if (typeBundle == Constants.Bundle.FACEBOOK) {

                    new DialogBuilder(CreateAccountActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.facebook_fail)
                            .positiveText(R.string.ok)
                            .show();

                } else if (typeBundle == Constants.Bundle.TWITTER) {

                    new DialogBuilder(CreateAccountActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.twitter_fail)
                            .positiveText(R.string.ok)
                            .show();
                }

                DTO.object = null;
            }
        }
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Cancel..", Toast.LENGTH_SHORT).show();
    }

    private SocialFragment getSocialFragment() {

        if (socialFragment == null) {
            socialFragment = (SocialFragment) getFragmentManager().findFragmentByTag(SOCIAL_FRAGMENT);
        }

        return socialFragment;
    }

    @Override
    public void onSuccess() {

        if (DTO.object != null) {
            if (DTO.object instanceof Bundle) {

                Bundle dtoBundle = new Bundle();

                dtoBundle.putBoolean(Constants.Bundle.SOCIAL_NEW, true);
                dtoBundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
                dtoBundle.putBoolean(Constants.Bundle.MAIN_MEMBER, false);

                DTO.object = null;

                navigateTo(UserActivity.class, dtoBundle);
            }
        } else {
            navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    @OnClick(R.id.button_create_account)
    public void onCreateAccount() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Register Yourself Button")
                .build());
        // TODO: Uncomment this to validate

        validator.validate();
    }

    private class ValidationHandler implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {

            if (state == State.NEXT) {

                boolean dobIsFail;

                if (!DateFormat.isDate(editTextBirthDate.getText().toString().trim().toLowerCase())) {
                    dobIsFail = true;
                } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) < 12) {
                    dobIsFail = true;
                } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) > 120) {
                    dobIsFail = true;
                } else {
                    dobIsFail = false;
                }

                if (dobIsFail) {
                    new DialogBuilder(CreateAccountActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.dob_invalid)
                            .positiveText(R.string.ok)
                            .show();
                } else {
                    onNextAnimation(linearLayoutCreate, linearLayoutNext);
                }


            } else {

                // TODO: Make request

                // Miquéias Lopes
                User user = new User();

                user.setNick(editTextNickname.getText().toString().trim());
                user.setDob(editTextBirthDate.getText().toString().trim().toLowerCase());
                String gender = spinnerGender.getSelectedItem().toString();
                gender = gender.substring(0, 1);
                user.setGender(gender.toUpperCase());
                user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());
                user.setEmail(editTextMail.getText().toString().trim().toLowerCase());
                user.setPassword(editTextPassword.getText().toString().trim());

                if (user.getPassword().length() <= 5) {

                    new DialogBuilder(CreateAccountActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.password_fail)
                            .positiveText(R.string.ok)
                            .show();

                } else {

                    final JSONObject jRoot = new JSONObject();

                    try {

                        jRoot.put("nick", user.getNick());
                        jRoot.put("email", user.getEmail());
                        jRoot.put("password", user.getPassword());
                        jRoot.put("client", user.getClient());
                        jRoot.put("dob", DateFormat.getDate(user.getDob()));
                        jRoot.put("gender", user.getGender());
                        jRoot.put("app_token", user.getAppToken());
                        jRoot.put("race", user.getRace());
                        jRoot.put("platform", user.getPlatform());
                        jRoot.put("picture", "0");
                        jRoot.put("gcm_token", gcmToken);

                        LocationUtility locationUtility = new LocationUtility(getApplicationContext());

                        try {

                            if (locationUtility.getLocation() != null) {
                                jRoot.put("lat", locationUtility.getLatitude());
                                jRoot.put("lon", locationUtility.getLongitude());
                            }

                        } catch (Exception e) {
                            jRoot.put("lat", -8.0464492);
                            jRoot.put("lon", -34.9324883);
                        }

                        SimpleRequester simpleRequester = new SimpleRequester();
                        simpleRequester.setUrl(Requester.API_URL + "user/create");
                        simpleRequester.setJsonObject(jRoot);
                        simpleRequester.setMethod(Method.POST);

                        String jsonStr = simpleRequester.execute(simpleRequester).get();

                        JSONObject jsonObject = new JSONObject(jsonStr);

                        if (jsonObject.get("error").toString() == "true") {
                            Toast.makeText(getApplicationContext(), R.string.erro_new_user, Toast.LENGTH_SHORT).show();

                        } else {

                            JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                            SingleUser singleUser = SingleUser.getInstance();
                            singleUser.setNick(jsonObjectUser.getString("nick"));
                            singleUser.setEmail(jsonObjectUser.getString("email"));
                            singleUser.setGender(jsonObjectUser.getString("gender"));
                            singleUser.setImage(jsonObjectUser.getInt("picture"));
                            singleUser.setId(jsonObjectUser.getString("id"));
                            singleUser.setRace(jsonObjectUser.getString("race"));
                            singleUser.setDob(jsonObjectUser.getString("dob"));
                            singleUser.setUser_token(jsonObjectUser.getString("token"));

                            sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUserToken());
                            editor.commit();

                            navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                           Intent.FLAG_ACTIVITY_NEW_TASK);
                        }

                    } catch (final Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.erro_new_user, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @Override
        public void onValidationFailed(final List<ValidationError> errorList) {

            for (final ValidationError error : errorList) {

                final String message = error.getCollatedErrorMessage(CreateAccountActivity.this);

                final View view = error.getView();

                if (view.getVisibility() == View.VISIBLE && view instanceof EditText) {

                    ((EditText) view).setError(message);
                }
            }
        }
    }

    enum State {

        SOCIAL(1), NEXT(2), CREATE(3);

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
}
