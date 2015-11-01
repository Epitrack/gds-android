package com.epitrack.guardioes.view.survey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.ViewUtility;
import com.epitrack.guardioes.view.base.BaseActivity;
import com.epitrack.guardioes.view.HomeActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class ShareActivity extends BaseActivity {

    private static final float MARGIN_TOP = 25;

    @Bind(R.id.text_view_hospital_message_hint)
    TextView textViewHospitalMessage;

    @Bind(R.id.text_view_social_message_hint)
    TextView textViewHSocialMessage;

    @Bind(R.id.share_facebook)
    Button buttonShareFacebook;

    @Bind(R.id.share_twitter)
    Button buttonShareTwitter;

    ShareDialog shareDialog;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.share);
        shareDialog = new ShareDialog(this);

        final boolean hasBadState = getIntent().getBooleanExtra(Constants.Bundle.BAD_STATE, false);

        if (hasBadState) {

            textViewHospitalMessage.setVisibility(View.VISIBLE);

            ViewUtility.setMarginTop(textViewHSocialMessage, ViewUtility.toPixel(this, MARGIN_TOP));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        navigateTo();
    }

    @OnClick(R.id.button_confirm)
    public void onConfirm() {
        navigateTo();
    }

    private void navigateTo() {

        navigateTo(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                       Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @OnClick(R.id.share_facebook)
    public void shareFacebook() {

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentDescription("Acabei de participar do Guardiões da Saúde, participe você também: www.guardioesdasaude.org")
                .setContentTitle("Guardiões da Saúde")
                .setContentUrl(Uri.parse("http://www.guardioesdasaude.org"))
                .build();

        shareDialog.show(content);
    }

    @OnClick(R.id.share_twitter)
    public void sahreTwitter() {

        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Acabei de participar do Guardiões da Saúde, participe você também: www.guardioesdasaude.org");
        builder.show();
    }
}
