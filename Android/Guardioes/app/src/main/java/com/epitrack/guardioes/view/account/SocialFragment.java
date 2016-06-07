package com.epitrack.guardioes.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.welcome.TermActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class SocialFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_TERM = 6661;

    private static final int REQUEST_FACEBOOK = 6662;
    private static final int REQUEST_TWITTER = 6663;
    private static final int REQUEST_GOOGLE = 6664;

    private static final String NAME = "Name";
    private static final String MAIL = "Mail";
    private static final String DATE = "Date";

    private AccountListener listener;

    private GoogleApiClient apiClient;

    private CallbackManager manager = CallbackManager.Factory.create();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        FacebookSdk.sdkInitialize(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.social, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle bundle) {
        bind(view);
    }

    @OnClick(R.id.button_google)
    public void onGoogle() {

        final Bundle bundle = new Bundle();

        bundle.putInt(Constants.Bundle.TYPE, REQUEST_GOOGLE);

        navigateForResult(TermActivity.class, REQUEST_TERM, bundle);
    }

    @OnClick(R.id.button_twitter)
    public void onTwitter() {

        final Bundle bundle = new Bundle();

        bundle.putInt(Constants.Bundle.TYPE, REQUEST_TWITTER);

        navigateForResult(TermActivity.class, REQUEST_TERM, bundle);
    }

    @OnClick(R.id.fragment_button_facebook)
    public void onFaceBook() {

        final Bundle bundle = new Bundle();

        bundle.putInt(Constants.Bundle.TYPE, REQUEST_FACEBOOK);

        navigateForResult(TermActivity.class, REQUEST_TERM, bundle);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == REQUEST_TERM) {

            final int type = intent.getIntExtra(Constants.Bundle.TYPE, Integer.MIN_VALUE);

            if (type == REQUEST_GOOGLE) {

                final GoogleSignInOptions option = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_client_id))
                        .requestEmail()
                        .build();

                apiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Auth.GOOGLE_SIGN_IN_API, option)
                        .build();

            } else if (type == REQUEST_TWITTER) {

                new TwitterAuthClient().authorize(getActivity(), new Callback<TwitterSession>() {

                    @Override
                    public void success(final Result<TwitterSession> result) {

                        final String user = result.data.getUserName();
                        final String token = result.data.getAuthToken().token;

                    }

                    @Override
                    public void failure(final TwitterException e) {
                        listener.onError();
                    }
                });

            } else if (type == REQUEST_FACEBOOK) {

                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));

                LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(final JSONObject json, final GraphResponse response) {

                                try {

                                    final Bundle bundle = new Bundle();

                                    bundle.putString(Constants.Bundle.NAME, json.getString(NAME));
                                    bundle.putString(Constants.Bundle.MAIL, json.getString(MAIL));
                                    bundle.putString(Constants.Bundle.DATE, json.getString(DATE));

                                    listener.onSuccess(bundle);

                                } catch (final JSONException e) {

                                }
                            }

                        }).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        listener.onCancel();
                    }

                    @Override
                    public void onError(final FacebookException error) {
                        listener.onError();
                    }
                });
            }

        } else if (resultCode == REQUEST_GOOGLE) {

        } else if (resultCode == REQUEST_TWITTER) {

        } else if (resultCode == REQUEST_FACEBOOK) {

            manager.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
