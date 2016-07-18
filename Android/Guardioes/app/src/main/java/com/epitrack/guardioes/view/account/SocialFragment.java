package com.epitrack.guardioes.view.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.RequestListener;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

    private static final String TAG = SocialFragment.class.getSimpleName();

    private static final int REQUEST_TERM = 6661;

    private static final int REQUEST_FACEBOOK = 6662;
    private static final int REQUEST_TWITTER = 6663;
    private static final int REQUEST_GOOGLE = 6664;

    private int request;
    private boolean login;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String MAIL = "email";

    private AccountListener listener;

    private TwitterAuthClient authClient = new TwitterAuthClient();

    private GoogleApiClient apiClient;

    private CallbackManager manager = CallbackManager.Factory.create();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        FacebookSdk.sdkInitialize(getActivity());

        loadFacebook();
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

        if (login) {

            setRequest(REQUEST_GOOGLE);

            loadGoogle();

        } else {

            final Bundle bundle = new Bundle();

            bundle.putInt(Constants.Bundle.TYPE, REQUEST_GOOGLE);

            navigateForResult(TermActivity.class, REQUEST_TERM, bundle);
        }
    }

    @OnClick(R.id.button_twitter)
    public void onTwitter() {

        if (login) {

            setRequest(REQUEST_TWITTER);

            loadTwitter();

        } else {

            final Bundle bundle = new Bundle();

            bundle.putInt(Constants.Bundle.TYPE, REQUEST_TWITTER);

            navigateForResult(TermActivity.class, REQUEST_TERM, bundle);
        }
    }

    @OnClick(R.id.fragment_button_facebook)
    public void onFaceBook() {

        if (login) {

            setRequest(REQUEST_FACEBOOK);

            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));

        } else {

            final Bundle bundle = new Bundle();

            bundle.putInt(Constants.Bundle.TYPE, REQUEST_FACEBOOK);

            navigateForResult(TermActivity.class, REQUEST_TERM, bundle);
        }
    }

    public void setRequest(final int request) {
        this.request = request;
    }

    private void loadTwitter() {

        authClient.authorize(getActivity(), new Callback<TwitterSession>() {

            @Override
            public void success(final Result<TwitterSession> result) {

                final String name = result.data.getUserName();
                final String token = result.data.getAuthToken().token;

                new UserRequester(getActivity()).validateSocialAccount("user/get?tw=", token, new RequestListener<User>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(final Exception error) {
                        listener.onError();
                    }

                    @Override
                    public void onSuccess(final User type) {

                        if (type == null) {

                            final User user = new User();

                            user.setNick(name);
                            user.setTw(token);

                            listener.onNotFound(user);

                        } else {

                            listener.onSuccess(type);
                        }
                    }
                });
            }

            @Override
            public void failure(final TwitterException e) {
                listener.onError();
            }
        });
    }

    private void loadFacebook() {

        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {

                final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(final JSONObject json, final GraphResponse response) {

                        try {

                            final String id = json.getString(ID);
                            final String name = json.getString(NAME);
                            final String mail = json.getString(MAIL);

                            new UserRequester(getActivity()).validateSocialAccount("user/get?fb=", id, new RequestListener<User>() {

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onError(final Exception error) {
                                    listener.onError();
                                }

                                @Override
                                public void onSuccess(final User type) {

                                    if (type == null) {

                                        final User user = new User();

                                        user.setNick(name);
                                        user.setEmail(mail);
                                        user.setPassword(mail);
                                        user.setFb(id);

                                        listener.onNotFound(user);

                                    } else {

                                        listener.onSuccess(type);
                                    }
                                }
                            });

                        } catch (final JSONException e) {
                            listener.onError();
                        }
                    }

                });

                final Bundle bundle = new Bundle();

                bundle.putString("fields", ID + "," + NAME + "," + MAIL);

                request.setParameters(bundle);

                request.executeAsync();
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

    private void loadGoogle() {

       final GoogleSignInOptions option = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        apiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, option)
                .build();
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(apiClient), REQUEST_GOOGLE);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_TERM) {

            if (resultCode == Activity.RESULT_OK) {

                setRequest(intent.getIntExtra(Constants.Bundle.TYPE, Integer.MIN_VALUE));

                if (request == REQUEST_GOOGLE) {

                    loadGoogle();

                } else if (request == REQUEST_TWITTER) {

                    loadTwitter();

                } else if (request == REQUEST_FACEBOOK) {

                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
                }
            }

        } else if (request == REQUEST_GOOGLE) {

            final GoogleSignInAccount account = Auth.GoogleSignInApi.getSignInResultFromIntent(intent).getSignInAccount();

            onGoogle(account);

        } else if (request == REQUEST_TWITTER) {

            authClient.onActivityResult(requestCode, resultCode, intent);

        } else if (request == REQUEST_FACEBOOK) {

            manager.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private void onGoogle(final GoogleSignInAccount account) {

        if (account == null) {
            Logger.logDebug(TAG, "The account is null.");

        } else {

            final String id = account.getId();
            final String name = account.getDisplayName();
            final String mail = account.getEmail();

            new UserRequester(getActivity()).validateSocialAccount("user/get?gl=", id, new RequestListener<User>() {

                @Override
                public void onStart() {

                }

                @Override
                public void onError(final Exception error) {
                    listener.onError();
                }

                @Override
                public void onSuccess(final User type) {

                    if (type == null) {

                        final User user = new User();

                        user.setNick(name);
                        user.setEmail(mail);
                        user.setPassword(mail);
                        user.setGl(id);

                        listener.onNotFound(user);

                    } else {

                        listener.onSuccess(type);
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setLogin(final boolean login) {
        this.login = login;
    }

    public void setListener(final AccountListener listener) {
        this.listener = listener;
    }
}
