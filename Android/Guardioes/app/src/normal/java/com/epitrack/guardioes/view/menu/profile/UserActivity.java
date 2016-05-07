package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.AvatarHelper;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.Mask;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 * @author Miqueias Lopes
 */
public class UserActivity extends BaseAppCompatActivity {

    @Bind(R.id.text_view_message)
    TextView textViewMessage;

    @Bind(R.id.edit_text_nickname)
    EditText editTextNickname;

    @Bind(R.id.image_view_image)
    CircularImageView imageViewImage;

    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;

    @Bind(R.id.spinner_race)
    Spinner spinnerRace;

    @Bind(R.id.edit_text_birth_date)
    EditText editTextBirthDate;

    @Bind(R.id.text_layout_mail)
    TextInputLayout textLayoutMail;

    @Bind(R.id.edit_text_mail)
    EditText editTextMail;

    @Bind(R.id.linear_layout_password)
    LinearLayout linearLayoutPassword;

    @Bind(R.id.edit_text_password)
    EditText editTextPassword;

    @Bind(R.id.edit_text_confirm_password)
    EditText editTextConfirmPassword;

    @Bind(R.id.spinner_relationship)
    Spinner spinnerRelationship;

    @Bind(R.id.text_view_relationship)
    TextView textViewRelationship;

    private boolean socialNew;
    private boolean newMenber;
    private boolean mainMember;

    private SingleUser singleUser = SingleUser.getInstance();

    private int image;
    private String path;

    private SharedPreferences shpGCMToken;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        socialNew = getIntent().getBooleanExtra(Constants.Bundle.SOCIAL_NEW, false);
        newMenber = getIntent().getBooleanExtra(Constants.Bundle.NEW_MEMBER, false);
        mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        setContentView(R.layout.user);

        shpGCMToken = PreferenceManager.getDefaultSharedPreferences(UserActivity.this);

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

        if (mainMember || socialNew) {
            textViewRelationship.setVisibility(View.INVISIBLE);
            spinnerRelationship.setVisibility(View.INVISIBLE);
        }

        if (newMenber || socialNew) {
            textLayoutMail.setVisibility(View.VISIBLE);
            editTextMail.setEnabled(true);
            editTextMail.setVisibility(View.VISIBLE);
        }

        if (socialNew) {
            imageViewImage.setVisibility(View.INVISIBLE);

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.new_user_social_media)
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            loadUser();
                        }
                    }).show();
        } else {

            imageViewImage.setVisibility(View.VISIBLE);

            loadUser();
        }
    }

    public void onResume() {
        super.onResume();

        getTracker().setScreenName("User Form Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void loadUser() {
        String nick;
        String dob;
        String gender;
        String race;
        String email;
        int image;
        String relationship;

        if (socialNew) {
            nick = singleUser.getNick();
            dob = singleUser.getDob();
            gender = singleUser.getGender();
            race = singleUser.getRace();
            email = singleUser.getEmail();
            image = singleUser.getImage();
            relationship = null;

        } else {
            nick = getIntent().getStringExtra("nick");
            dob = getIntent().getStringExtra("dob");
            gender = getIntent().getStringExtra("gender");
            race = getIntent().getStringExtra("race");
            email = getIntent().getStringExtra("email");
            image = getIntent().getIntExtra("picture", 0);
            relationship = getIntent().getStringExtra("relationship");
        }

        if (!newMenber || socialNew) {
            editTextNickname.setText(nick);

            if (dob != null) {
                String dateFormat = DateFormat.getDate(dob, "dd/MM/yyyy");
                editTextBirthDate.setText(dateFormat);
            }
            editTextMail.setText(email);

            if (race != null) {
                if (race.equals("branco")) {
                    spinnerRace.setSelection(0);
                } else if (race.equals("preto")) {
                    spinnerRace.setSelection(1);
                } else if (race.equals("pardo")) {
                    spinnerRace.setSelection(2);
                } else if (race.equals("amarelo")) {
                    spinnerRace.setSelection(3);
                } else if (race.equals("indígena")) {
                    spinnerRace.setSelection(4);
                }
            }

            if (relationship != null) {
                switch (relationship) {
                    case "pai":
                        spinnerRelationship.setSelection(0);
                        break;
                    case "mae":
                        spinnerRelationship.setSelection(1);
                        break;
                    case "filho":
                        spinnerRelationship.setSelection(2);
                        break;
                    case "irmao":
                        spinnerRelationship.setSelection(3);
                        break;
                    case "avo":
                        spinnerRelationship.setSelection(4);
                        break;
                    case "neto":
                        spinnerRelationship.setSelection(5);
                        break;
                    case "tio":
                        spinnerRelationship.setSelection(6);
                        break;
                    case "sobrinho":
                        spinnerRelationship.setSelection(7);
                        break;
                    case "bisavo":
                        spinnerRelationship.setSelection(8);
                        break;
                    case "bisneto":
                        spinnerRelationship.setSelection(9);
                        break;
                    case "primo":
                        spinnerRelationship.setSelection(10);
                        break;
                    case "sogro":
                        spinnerRelationship.setSelection(11);
                        break;
                    case "genro":
                        spinnerRelationship.setSelection(12);
                        break;
                    case "nora":
                        spinnerRelationship.setSelection(13);
                        break;
                    case "padrasto":
                        spinnerRelationship.setSelection(14);
                        break;
                    case "madrasta":
                        spinnerRelationship.setSelection(15);
                        break;
                    case "enteado":
                        spinnerRelationship.setSelection(16);
                        break;
                    case "conjuge":
                        spinnerRelationship.setSelection(17);
                        break;
                    case "outros":
                        spinnerRelationship.setSelection(18);
                        break;
                    default:
                        spinnerRelationship.setSelection(18);
                        break;
                }
            }

            if (gender != null) {
                if (gender.equals("M")) {
                    spinnerGender.setSelection(0);
                } else {
                    spinnerGender.setSelection(1);
                }
            }
        }

        if (mainMember) {

            textViewMessage.setText(R.string.message_fields);
            textLayoutMail.setVisibility(View.VISIBLE);
            linearLayoutPassword.setVisibility(View.VISIBLE);
            editTextMail.setEnabled(true);
            editTextMail.setVisibility(View.VISIBLE);

            new AvatarHelper().loadImage(this, imageViewImage, singleUser);

        } else if (socialNew) {
            if (singleUser.getEmail() == null) {
                textLayoutMail.setVisibility(View.VISIBLE);
                editTextMail.setEnabled(true);
                editTextMail.setVisibility(View.VISIBLE);
            }
        } else {
            textLayoutMail.setVisibility(View.VISIBLE);
            editTextMail.setEnabled(true);
            editTextMail.setVisibility(View.VISIBLE);

            User household = new User();

            household.setPath(path);
            household.setImage(image);

            household.setNick(nick);
            household.setDob(dob);
            household.setGender(gender);
            household.setRace(race);
            household.setEmail(email);
            household.setRelationship(relationship);

            new AvatarHelper().loadImage(this, imageViewImage, household);
        }
    }

    @OnClick(R.id.image_view_image)
    public void onImage() {

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, mainMember);

        navigateForResult(AvatarActivity.class, Constants.RequestCode.IMAGE, bundle);
    }

    private boolean validate(final User user) {

        if (!DateFormat.isDate(user.getDob())) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();

            return false;

        } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) > 120) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();

            return false;

        } else if (DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString().trim())) < 0) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();

            return false;
        }

        return true;
    }

    @OnClick(R.id.button_save)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Create New Member/User Button")
                .build());

        final User user = new User();

        user.setNick(editTextNickname.getText().toString().trim());
        user.setDob(editTextBirthDate.getText().toString().trim().toLowerCase());
        user.setGender(spinnerGender.getSelectedItem().toString().substring(0, 1).toUpperCase());
        user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());
        user.setEmail(editTextMail.getText().toString().toLowerCase().trim());
        user.setPath(path);
        user.setImage(image);

        String relationship = spinnerRelationship.getSelectedItem().toString().toLowerCase();
        relationship = relationship.replace("ô", "o");
        relationship = relationship.replace("ã", "a");
        relationship = relationship.replace("ã", "a");

        user.setRelationship(relationship.toLowerCase());

        if (validate(user)) {

            JSONObject jsonObject = new JSONObject();
            SimpleRequester simpleRequester = new SimpleRequester();

            try {

                jsonObject.put("nick", user.getNick());
                jsonObject.put("dob", DateFormat.getDate(user.getDob()));
                jsonObject.put("gender", user.getGender());
                jsonObject.put("race", user.getRace());
                jsonObject.put("client", user.getClient());
                jsonObject.put("race", user.getRace());
                jsonObject.put("relationship", user.getRelationship());
                jsonObject.put("email", user.getEmail());
                jsonObject.put("picture", user.getImage());
                
                if (!socialNew) {

                    if (mainMember) {

                        String password = editTextPassword.getText().toString().trim();
                        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                        if (!password.isEmpty() || !confirmPassword.isEmpty()) {

                            if ((password.length() < 6) || (confirmPassword.length() < 6)) {

                                new DialogBuilder(UserActivity.this).load()
                                        .title(R.string.attention)
                                        .content(R.string.password_fail)
                                        .positiveText(R.string.ok)
                                        .show();
                                return;
                            }

                            if (!password.equals(confirmPassword)) {

                                new DialogBuilder(UserActivity.this).load()
                                        .title(R.string.attention)
                                        .content(R.string.password_not_equal)
                                        .positiveText(R.string.ok)
                                        .show();
                                return;
                            }

                            if (!password.equals("")) {
                                if (password.equals(confirmPassword)) {
                                    user.setPassword(password);
                                    jsonObject.put("password", user.getPassword());
                                }
                            }
                        }
                    }

                    if (newMenber) {
                        jsonObject.put("user", singleUser.getId());

                    } else if (mainMember) {

                        jsonObject.put("gcm_token", shpGCMToken.getString(Constants.Push.SENDER_ID, ""));
                        jsonObject.put("id", singleUser.getId());

                    } else {
                        jsonObject.put("gcm_token", shpGCMToken.getString(Constants.Push.SENDER_ID, ""));
                        jsonObject.put("id", getIntent().getStringExtra("id"));
                    }

                    if (newMenber) {
                        simpleRequester.setUrl(Requester.API_URL + "household/create");

                    } else if (mainMember) {
                        simpleRequester.setUrl(Requester.API_URL + "user/update");

                    } else {
                        simpleRequester.setUrl(Requester.API_URL + "household/update");
                    }

                } else {

                    jsonObject.put("password", singleUser.getPassword());
                    jsonObject.put("app_token", user.getAppToken());
                    jsonObject.put("platform", user.getPlatform());
                    jsonObject.put("gl", singleUser.getGl());
                    jsonObject.put("tw", singleUser.getTw());
                    jsonObject.put("fb", singleUser.getFb());
                    jsonObject.put("picture", singleUser.getImage());
                    jsonObject.put("gcm_token", shpGCMToken.getString(Constants.Push.SENDER_ID, ""));

                   /* try {
                        locationUtility = new LocationUtility(getApplicationContext());

                        if (locationUtility.getLocation() != null) {
                            jsonObject.put("lat", locationUtility.getLatitude());
                            jsonObject.put("lon", locationUtility.getLongitude());
                        }
                    } catch (Exception e) {

                    }*/

                    if (singleUser.getEmail() == null || singleUser.getEmail() == "") {
                        jsonObject.put("email", editTextMail.getText().toString().toLowerCase());
                        jsonObject.put("password", editTextMail.getText().toString().toLowerCase());

                    } else {
                        jsonObject.put("email", singleUser.getEmail());
                    }

                    simpleRequester.setUrl(Requester.API_URL + "user/create");
                }

                simpleRequester.setJsonObject(jsonObject);
                simpleRequester.setMethod(Method.POST);

                String jsonStr = simpleRequester.execute(simpleRequester).get();

                jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString() == "true") {
                    //Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    //R.string.error_add_new_member
                    new DialogBuilder(UserActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.error_add_new_member)
                            .positiveText(R.string.ok)
                            .show();

                } else {

                    if (socialNew) {

                        JSONObject jsonObjectUser = jsonObject.getJSONObject("user");

                        singleUser.setNick(jsonObjectUser.getString("nick"));
                        singleUser.setEmail(jsonObjectUser.getString("email"));
                        singleUser.setGender(jsonObjectUser.getString("gender"));
                        singleUser.setImage(0);
                        singleUser.setId(jsonObjectUser.getString("id"));
                        singleUser.setPassword(jsonObjectUser.getString("email"));
                        singleUser.setRace(jsonObjectUser.getString("race"));
                        singleUser.setDob(jsonObjectUser.getString("dob"));

                        try {

                            singleUser.setHashtags(jsonObjectUser.getJSONArray("hashtags"));

                        } catch (Exception e) {

                        }

                        SharedPreferences sharedPreferences = null;

                        jsonObject = new JSONObject();

                        jsonObject.put("email", singleUser.getEmail());
                        jsonObject.put("password", singleUser.getPassword());

                        simpleRequester = new SimpleRequester();
                        simpleRequester.setUrl(Requester.API_URL + "user/login");
                        simpleRequester.setJsonObject(jsonObject);
                        simpleRequester.setMethod(Method.POST);

                        jsonStr = simpleRequester.execute(simpleRequester).get();

                        jsonObject = new JSONObject(jsonStr);

                        if (jsonObject.get("error").toString() == "true") {
                            Toast.makeText(getApplicationContext(), "Erro - " + jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

                        } else {

                            singleUser.setUserToken(jsonObject.get("token").toString());

                            sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Constants.Pref.PREFS_NAME, singleUser.getUserToken());
                            editor.commit();

                            sharedPreferences = getSharedPreferences(Constants.Pref.PREFS_SOCIAL, 0);
                            SharedPreferences.Editor editorSocial = sharedPreferences.edit();

                            editorSocial.putString(Constants.Pref.PREFS_NAME, "true");
                            editor.commit();
                        }

                        new DialogBuilder(UserActivity.this).load()
                                .title(R.string.attention)
                                .content(R.string.cadastro_sucesso)
                                .positiveText(R.string.ok)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(final MaterialDialog dialog) {
                                        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                    }
                                }).show();
                    } else {

                        if (newMenber) {

                            lookup();

                            onBackPressed();

                        } else {

                            lookup();

                            new DialogBuilder(UserActivity.this).load()
                                    .title(R.string.attention)
                                    .content(R.string.generic_update_data_ok)
                                    .positiveText(R.string.ok)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        }

                        //editTextNickname.setText("");
                        //editTextBirthDate.setText("");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == Constants.RequestCode.IMAGE) {

            if (resultCode == RESULT_OK) {

                if (intent.hasExtra(Constants.Bundle.AVATAR)) {

                    final Avatar avatar = (Avatar) intent.getSerializableExtra(Constants.Bundle.AVATAR);

                    this.path = null;
                    this.image = avatar.getId();

                    imageViewImage.setImageResource(avatar.getSmall());

                } else if (intent.hasExtra(Constants.Bundle.PATH)) {

                    final String path = intent.getStringExtra(Constants.Bundle.PATH);

                    this.image = 0;
                    this.path = path;

                    final int width = imageViewImage.getWidth();
                    final int height = imageViewImage.getHeight();

                    Picasso.with(this).load(Constants.PATH + path)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .resize(width, height)
                            .centerCrop()
                            .into(imageViewImage);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void lookup() {
        SingleUser singleUser = SingleUser.getInstance();
        JSONObject jsonObject = new JSONObject();

        SimpleRequester sendPostRequest = new SimpleRequester();
        sendPostRequest.setUrl(Requester.API_URL + "user/lookup/");
        sendPostRequest.setJsonObject(jsonObject);
        sendPostRequest.setMethod(Method.GET);

        String jsonStr;
        try {
            jsonStr = sendPostRequest.execute(sendPostRequest).get();

            jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString().equals("true")) {

            } else {

                JSONObject jsonObjectUser = jsonObject.getJSONObject("data");

                singleUser.setNick(jsonObjectUser.getString("nick"));
                singleUser.setEmail(jsonObjectUser.getString("email"));
                singleUser.setGender(jsonObjectUser.getString("gender"));
                singleUser.setId(jsonObjectUser.getString("id"));
                singleUser.setRace(jsonObjectUser.getString("race"));
                singleUser.setDob(jsonObjectUser.getString("dob"));
                singleUser.setUserToken(jsonObjectUser.get("token").toString());
                singleUser.setPath(mainMember ? path : this.singleUser.getPath());

                try {
                    singleUser.setImage(jsonObjectUser.getInt("picture"));

                } catch (Exception e) {
                }
                //navigateTo(ProfileActivity.class);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {

        if (socialNew) {

            final Intent intent = new Intent(this, WelcomeActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        } else {

            super.onBackPressed();
        }
    }
}
