package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.AvatarHelper;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.Mask;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.AuthRequester;
import com.epitrack.guardioes.request.base.RequestHandler;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 * @author Miqueias Lopes
 */
public class UserActivity extends BaseAppCompatActivity {

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

    @Bind(R.id.spinner_gender)
    Spinner spinnerGender;

    @Bind(R.id.spinner_race)
    Spinner spinnerRace;

    @Bind(R.id.spinner_parent)
    Spinner spinnerParent;

    //@Bind(R.id.linear_layout_password)
    //LinearLayout linearLayoutPassword;

    //@Bind(R.id.edit_text_password)
    //EditText editTextPassword;

    //@Bind(R.id.edit_text_confirm_password)
    //EditText editTextConfirmPassword;

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

        editTextNickname.setText(user.getNick());

        spinnerGender.setSelection(user.getGender().equalsIgnoreCase("M") ? 0 : 1);

        final String[] raceArray = getResources().getStringArray(R.array.race_array);

        for (int i = 0; i < raceArray.length; i++) {

            if (user.getRace().equalsIgnoreCase(raceArray[i])) {
                spinnerRace.setSelection(i);
            }
        }

        final String format = DateFormat.getDate(user.getDob(), "dd/MM/yyyy");

        editTextBirthDate.setText(format);

        new AvatarHelper().loadImage(this, imageViewImage, user);

        if (main) {
            editTextMail.setText(user.getEmail());

        } else {

            final String[] parentArray = getResources().getStringArray(R.array.relationship_array);

            for (int i = 0; i < parentArray.length; i++) {

                if (user.getRelationship().equalsIgnoreCase(parentArray[i])) {
                    spinnerParent.setSelection(i);
                }
            }
        }
    }

    private void loadView() {

        editTextBirthDate.addTextChangedListener(Mask.insert("##/##/####", editTextBirthDate));

        spinnerGender.setAdapter(new ItemAdapter(this, getResources().getStringArray(R.array.gender_array)));
        spinnerRace.setAdapter(new ItemAdapter(this, getResources().getStringArray(R.array.race_array)));
        spinnerParent.setAdapter(new ItemAdapter(this, getResources().getStringArray(R.array.relationship_array)));

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

    private void add() {

        user.setNick(editTextNickname.getText().toString().trim());
        user.setDob(editTextBirthDate.getText().toString().trim());
        user.setGender(spinnerGender.getSelectedItem().toString().substring(0, 1).toUpperCase());
        user.setRace(spinnerRace.getSelectedItem().toString().toLowerCase());
        user.setEmail(editTextMail.getText().toString().toLowerCase().trim());
        user.setPath(path);
        user.setImage(image);

        final String parent = spinnerParent.getSelectedItem()
                .toString()
                .toLowerCase()
                .replace("ô", "o")
                .replace("ã", "a")
                .replace("ã", "a");

        user.setRelationship(parent);

        if (validate(user)) {

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
            });
        }
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

        return true;
    }

    private void setCreate(final boolean create) {
        this.create = create;
    }
}
