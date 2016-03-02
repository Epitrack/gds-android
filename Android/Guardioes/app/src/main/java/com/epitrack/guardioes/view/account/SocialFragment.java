package com.epitrack.guardioes.view.account;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.DTO;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.view.Navigate;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.welcome.TermsAction;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

/**
 * @author Igor Morais
 */
public class SocialFragment extends BaseFragment {

    private static boolean IS_CREATE_ACCOUNT = false;

    @Bind(R.id.fragment_button_facebook)
    Button buttonFaceBook;

    @Bind(R.id.button_google)
    Button buttonGoogle;

    @Bind(R.id.button_twitter)
    Button buttonTwitter;

    private SocialAccountListener listener;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof SocialAccountListener)) {

            throw new IllegalStateException("The " +
                    activity.getClass().getSimpleName() + " must implement SocialAccountListener.");
        }

        listener = (SocialAccountListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final View view = inflater.inflate(R.layout.social, viewGroup, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }


   @OnClick(R.id.button_google)
    public void onGoogle() {

       DTO.object = Constants.Bundle.GOOGLE;

       if (IS_CREATE_ACCOUNT) {
           IS_CREATE_ACCOUNT = false;
           final Bundle bundle = new Bundle();

           bundle.putString(Constants.Bundle.GOOGLE, Constants.Bundle.GOOGLE);
           navigateTo(TermsAction.class, bundle);
       } else {
           navigateTo(SocialLoginActivity.class);
       }
    }

    @OnClick(R.id.button_twitter)
    public void onTwitter() {

        DTO.object = Constants.Bundle.TWITTER;

        if (IS_CREATE_ACCOUNT) {
            IS_CREATE_ACCOUNT = false;
            final Bundle bundle = new Bundle();

            bundle.putString(Constants.Bundle.TWITTER, Constants.Bundle.TWITTER);
            navigateTo(TermsAction.class, bundle);
        } else {
            navigateTo(SocialLoginActivity.class);
        }
    }

    @OnClick(R.id.fragment_button_facebook)
    public void onFaceBook() {

        DTO.object = Constants.Bundle.FACEBOOK;

        if (IS_CREATE_ACCOUNT) {
            IS_CREATE_ACCOUNT = false;
            final Bundle bundle = new Bundle();

            bundle.putString(Constants.Bundle.FACEBOOK, Constants.Bundle.FACEBOOK);
            navigateTo(TermsAction.class, bundle);
        } else {
            navigateTo(SocialLoginActivity.class);
        }
    }

    public void setEnable(final boolean enable) {

        buttonTwitter.setEnabled(enable);
        buttonFaceBook.setEnabled(enable);
        buttonGoogle.setEnabled(enable);
    }

    public void isIsCreateAccount(boolean isCreateAccount) {
        IS_CREATE_ACCOUNT = isCreateAccount;
    }
}
