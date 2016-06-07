package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.user);
    }

    public void onResume() {
        super.onResume();

        getTracker().setScreenName("User Form Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnClick(R.id.image_view_image)
    public void onImage() {

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, mainMember);

        navigateForResult(AvatarActivity.class, Constants.RequestCode.IMAGE, bundle);
    }

    @OnClick(R.id.button_save)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Create New Member/User Button")
                .build());
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

    @Override
    public void onBackPressed() {

    }
}
