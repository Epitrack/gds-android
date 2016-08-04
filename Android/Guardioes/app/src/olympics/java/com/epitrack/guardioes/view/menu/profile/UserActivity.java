package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.AvatarHelper;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.Helper;
import com.epitrack.guardioes.helper.Mask;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.Country;
import com.epitrack.guardioes.model.Parent;
import com.epitrack.guardioes.model.Race;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.AuthRequester;
import com.epitrack.guardioes.request.base.RequestHandler;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.CountryAdapter;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 * @author Miqueias Lopes
 */
public class UserActivity extends BaseAppCompatActivity {

    private static final int NONE = 0;

    private static final int BRAZIL = 30;
    private static final int FRANCE = 76;

    @Bind(R.id.text_view_message)
    TextView textViewMessage;

    @Bind(R.id.image_view_image)
    CircularImageView imageViewImage;

    @Bind(R.id.edit_text_nickname)
    AppCompatEditText editTextNickname;

    @Bind(R.id.text_layout_mail)
    TextInputLayout textLayoutMail;

    @Bind(R.id.edit_text_mail)
    AppCompatEditText editTextMail;

    @Bind(R.id.edit_text_birth_date)
    AppCompatEditText editTextBirthDate;

    @Bind(R.id.text_view_state)
    TextView textViewState;

    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;

    @Bind(R.id.text_view_race)
    TextView textViewRace;

    @Bind(R.id.spinner_race)
    Spinner spinnerRace;

    @Bind(R.id.spinner_parent)
    Spinner spinnerParent;

    @Bind(R.id.spinner_country)
    Spinner spinnerCountry;

    @Bind(R.id.spinner_state)
    Spinner spinnerState;

    @Bind(R.id.spinner_profile)
    Spinner spinnerProfile;

    @Bind(R.id.button_change_password)
    Button btnChangePassword;

    private int image;
    private String path;

    private User user;
    private boolean main;
    private boolean create;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.user);

        main = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);
        user = Parcels.unwrap(getIntent().getParcelableExtra(Constants.Bundle.USER));

        setCreate(user == null);

        loadView();

        if (create) {
            user = new User();

        } else {
            load(user, main);
        }
    }

    private void load(final User user, final boolean main) {
        Log.d("USER load",user.toString());
        editTextNickname.setText(user.getNick());

        spinnerGender.setSelection(user.getGender().equalsIgnoreCase("M") ? 1 : 2);

        spinnerRace.setSelection(Race.getBy(user.getRace()).getId() - 1);

        final String format = DateFormat.getDate(user.getDob(), "dd/MM/yyyy");

        editTextBirthDate.setText(format);

        new AvatarHelper().loadImage(this, imageViewImage, user);

        if (main) {
            editTextMail.setText(user.getEmail());

        } else {
            this.btnChangePassword.setVisibility(View.GONE);
            spinnerParent.setSelection(Parent.getBy(user.getRelationship()).getId() - 1);
        }

        if (user.getCountry() != null) {

            final List<Country> countryList = Helper.loadCountry();

            for (int i = 0; i < countryList.size(); i++) {

                final String name = new Locale("", countryList.get(i).getCode()).getDisplayCountry(Locale.ENGLISH).toLowerCase();

                if (user.getCountry().equalsIgnoreCase(name)) {
                    spinnerCountry.setSelection(i + 1);
                }
            }
        }

        if (user.getState() != null) {

            final String[] stateArray = getResources().getStringArray(R.array.array_state);

            for (int i = 0; i < stateArray.length; i++) {

                if (user.getState().equalsIgnoreCase(stateArray[i])) {
                    spinnerState.setSelection(i + 1);
                }
            }
        }

        spinnerProfile.setSelection(user.getProfile());
    }

    private List<String> toList(final String[] valueArray) {

        final List<String> valueList = new LinkedList<>(Arrays.asList(valueArray));

        valueList.add(0, getString(R.string.select));

        return valueList;
    }

    private void loadView() {

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

        spinnerGender.setAdapter(new ItemAdapter(this, toList(getResources().getStringArray(R.array.gender_array))));
        spinnerRace.setAdapter(new ItemAdapter(this, toList(getResources().getStringArray(R.array.race_array))));
        spinnerParent.setAdapter(new ItemAdapter(this, toList(getResources().getStringArray(R.array.relationship_array))));

        final List<Country> countryList = Helper.loadCountry();

        countryList.add(0, new Country(0, "", getString(R.string.select)));

        spinnerCountry.setAdapter(new CountryAdapter(this, CountryAdapter.Type.EDIT, countryList));

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {

                textViewState.setVisibility(id == BRAZIL ? View.VISIBLE : View.GONE);
                spinnerState.setVisibility(id == BRAZIL ? View.VISIBLE : View.GONE);

                textViewRace.setVisibility(id == NONE || id == FRANCE ? View.GONE : View.VISIBLE);
                spinnerRace.setVisibility(id == NONE || id == FRANCE ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        });

        spinnerState.setAdapter(new ItemAdapter(this, toList(getResources().getStringArray(R.array.array_state))));
        spinnerProfile.setAdapter(new ItemAdapter(this, toList(getResources().getStringArray(R.array.array_profile))));

        if (main) {

            textViewMessage.setText(R.string.message_fields);

            findViewById(R.id.linear_layout_parent).setVisibility(View.GONE);

            textLayoutMail.setVisibility(View.VISIBLE);
            editTextMail.setVisibility(View.VISIBLE);
        }
    }

    public void onResume() {
        super.onResume();

        getTracker().setScreenName("User Form Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnClick(R.id.image_view_image)
    public void onImage() {

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, main);

        navigateForResult(AvatarActivity.class, Constants.RequestCode.IMAGE, bundle);
    }

    @OnClick(R.id.button_save)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Create New Member/User Button")
                .build());

        add();
    }

    @OnClick(R.id.button_change_password)
    public void onChangePassword(){
        final Bundle bundle = new Bundle();

        bundle.putParcelable(Constants.Bundle.USER, Parcels.wrap(user));
        navigateTo(ChangePasswordActivity.class, bundle);
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

                    this.image = user.getImage();
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

    private void add() {

        user.setNick(editTextNickname.getText().toString().trim());
        user.setDob(editTextBirthDate.getText().toString().trim());
        user.setGender(spinnerGender.getSelectedItem().toString().substring(0, 1).toUpperCase());

        final String race = Race.getBy(spinnerRace.getSelectedItemPosition() + 1).getValue();
        user.setRace(race);

        user.setEmail(editTextMail.getText().toString().toLowerCase().trim());

        final String country = ((Country) spinnerCountry.getSelectedItem()).getCode();
        final String name = new Locale("", country).getDisplayCountry(Locale.ENGLISH).toLowerCase();

        user.setCountry(name);

        user.setState(spinnerState.getSelectedItem().toString().toLowerCase());
        user.setProfile(spinnerProfile.getSelectedItemPosition());
        user.setPath(path);

        if(image==0){
            user.setImage(user.getImage());
        }else{
            user.setImage(image);
        }

        final String parent = Parent.getBy(spinnerParent.getSelectedItemPosition() + 1).getValue();
        user.setRelationship(parent);

        if (validate(user)) {
            Log.d("USER /user/update",user.toString());
            final String url = main ? "/user/update" : create ? "/household/create" : "/household/update";

            new UserRequester(this).addOrUpdate(url, user, create ? SingleUser.getInstance().getId() : null, new RequestHandler<String>(this) {

                @Override
                public void onError(final Exception e) {
                    super.onError(e);

                    new DialogBuilder(UserActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.error_add_new_member)
                            .positiveText(R.string.ok)
                            .show();
                }

                @Override
                public void onSuccess(final String message) {
                    super.onSuccess(message);

                    new AuthRequester(UserActivity.this).loadAuth(new RequestListener<User>() {

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }

                        @Override
                        public void onSuccess(User type) {

                            if (main) {

                                updateUser();

                                loadMain();

                            } else {

                                new DialogBuilder(UserActivity.this).load()
                                        .title(R.string.attention)
                                        .content(create ? R.string.member_added : R.string.member_updated)
                                        .positiveText(R.string.ok)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                                            @Override
                                            public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                                                finish();
                                            }

                                        }).show();
                            }
                        }
                    });
                }
            });
        }
    }

    private void updateUser() {

        final User user = new PrefManager(UserActivity.this).get(Constants.Pref.USER, User.class);

        if (user != null) {

            user.setPath(path);

            SingleUser.getInstance().setPath(path);

            new PrefManager(UserActivity.this).put(Constants.Pref.USER, user);
        }
    }

    private void loadMain() {

        new AuthRequester(this).loadAuth(new RequestListener<User>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onSuccess(final User user) {

                new DialogBuilder(UserActivity.this).load()
                        .title(R.string.attention)
                        .content(create ? R.string.member_added : R.string.member_updated)
                        .positiveText(R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {

                            @Override
                            public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                                finish();
                            }

                        }).show();
            }
        });
    }

    private boolean validate(final User user) {

        final String name = user.getNick();

        if (name.trim().length() == 0) {

            editTextNickname.setError(getString(R.string.validation_empty));

            return false;
        }

        final int date = DateFormat.getDateDiff(DateFormat.getDate(editTextBirthDate.getText().toString()));

        if (!DateFormat.isDate(user.getDob()) || date < 0 || date > 120) {

            new DialogBuilder(UserActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.dob_invalid)
                    .positiveText(R.string.ok)
                    .show();

            return false;
        }

        if (spinnerGender.getSelectedItemPosition() == NONE) {

            Toast.makeText(this, R.string.validation_gender, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (spinnerRace.getVisibility() == View.VISIBLE && spinnerRace.getSelectedItemPosition() == NONE) {

            Toast.makeText(this, R.string.validation_race, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (spinnerCountry.getSelectedItemPosition() == NONE) {

            Toast.makeText(this, R.string.validation_country, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (spinnerState.getVisibility() == View.VISIBLE && spinnerState.getSelectedItemPosition() == NONE) {

            Toast.makeText(this, R.string.validation_state, Toast.LENGTH_SHORT).show();

            return false;
        }

        if (spinnerProfile.getSelectedItemPosition() == NONE) {

            Toast.makeText(this, R.string.validation_profile, Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    private void setCreate(final boolean create) {
        this.create = create;
    }
}
