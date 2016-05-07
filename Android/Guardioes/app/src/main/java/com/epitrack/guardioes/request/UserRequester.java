package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestException;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.request.base.RequesterConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRequester extends BaseRequester {

    private static final String TAG = UserRequester.class.getSimpleName();

    public UserRequester(final Context context) {
        super(context);
    }

    public void login(final String mail, final String password, final RequestListener<User> listener) {

        final String url = RequesterConfig.URL + "/user/login";

        final Map<String, Object> bodyMap = new HashMap<>();

        bodyMap.put("email", mail);
        bodyMap.put("password", password);

        listener.onStart();

        new Requester(getContext()).request(Method.POST, url, getHeaderMap(), bodyMap, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        try {

                            final JSONObject json = new JSONObject(response.getResult());

                            if (json.getBoolean("error")) {

                                listener.onError(new RequestException());

                            } else {

                                final User user = new User();

                                JSONObject jsonObjectUser = json.getJSONObject("user");

                                user.setNick(jsonObjectUser.getString("nick"));
                                user.setEmail(jsonObjectUser.getString("email"));
                                user.setGender(jsonObjectUser.getString("gender"));
                                user.setId(jsonObjectUser.getString("id"));
                                user.setRace(jsonObjectUser.getString("race"));
                                user.setDob(jsonObjectUser.getString("dob"));
                                user.setUserToken(json.getString("token"));
                                user.setImage(jsonObjectUser.getInt("picture"));
                                user.setHashtags(jsonObjectUser.getJSONArray("hashtags"));

                                if (new PrefManager(getContext()).put(Constants.Pref.USER, user)) {

                                    listener.onSuccess(user);
                                }
                            }

                        } catch (final JSONException e) {
                            Logger.logDebug(TAG, e.getMessage());

                            listener.onError(new RequestException());
                        }

                    } else {

                        listener.onError(new RequestException());
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    public void createAccount(final User user, final RequestListener<User> listener) {

        final String url = RequesterConfig.URL + "user/create";

        final Map<String, Object> bodyMap = new HashMap<>();

        bodyMap.put("nick", user.getNick());
        bodyMap.put("email", user.getEmail());
        bodyMap.put("password", user.getPassword());
        bodyMap.put("client", user.getClient());
        bodyMap.put("dob", DateFormat.getDate(user.getDob()));
        bodyMap.put("gender", user.getGender());
        bodyMap.put("app_token", user.getAppToken());
        bodyMap.put("race", user.getRace());
        bodyMap.put("platform", user.getPlatform());
        bodyMap.put("picture", "0");
        bodyMap.put("gcm_token", user.getGcmToken());

        listener.onStart();

        new Requester(getContext()).request(Method.POST, url, getHeaderMap(), bodyMap, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        try {

                            final JSONObject json = new JSONObject(response.getResult());

                            if (json.getBoolean("error")) {

                                listener.onError(new RequestException());

                            } else {

                                final User user = new User();

                                JSONObject jsonObjectUser = json.getJSONObject("user");

                                user.setNick(jsonObjectUser.getString("nick"));
                                user.setEmail(jsonObjectUser.getString("email"));
                                user.setGender(jsonObjectUser.getString("gender"));
                                user.setImage(jsonObjectUser.getInt("picture"));
                                user.setId(jsonObjectUser.getString("id"));
                                user.setRace(jsonObjectUser.getString("race"));
                                user.setDob(jsonObjectUser.getString("dob"));
                                user.setUserToken(jsonObjectUser.getString("token"));

                                if (new PrefManager(getContext()).put(Constants.Pref.USER, user)) {

                                    listener.onSuccess(user);
                                }
                            }

                        } catch (final JSONException e) {
                            Logger.logDebug(TAG, e.getMessage());

                            listener.onError(new RequestException());
                        }

                    } else {

                        listener.onError(new RequestException());
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    public void getAllProfiles(final String id, final RequestListener<List<User>> listener) {

        final String url = RequesterConfig.URL + RequesterConfig.Api.USER + "/household/" + id;

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, getAuthHeaderMap(), new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    try {

                        final List<User> parentList = new ArrayList<>();

                        final JSONObject json = new JSONObject(response.getResult());

                        if (!json.getBoolean("error")) {

                            final JSONArray jsonArray = json.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                final JSONObject jsonObject = jsonArray.getJSONObject(i);

                                final User user = new User(jsonObject.getString("nick"),
                                        "",
                                        jsonObject.getString("id"),
                                        jsonObject.getString("dob"),
                                        jsonObject.getString("race"),
                                        jsonObject.getString("gender"));

                                try {

                                    user.setImage(jsonObject.getInt("picture"));

                                } catch (Exception e) {
                                    Logger.logError(TAG, e.getMessage());
                                }

                                try {

                                    user.setRelationship(jsonObject.getString("relationship"));

                                } catch (Exception e) {
                                    user.setRelationship("");
                                }

                                try {

                                    user.setEmail(jsonObject.getString("email"));

                                } catch (Exception e) {
                                }

                                parentList.add(user);
                            }
                        }

                        listener.onSuccess(parentList);

                    } catch (final Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    public void getAllHousehold(final String id, final RequestListener<List<User>> listener) {

        final String url = RequesterConfig.URL + RequesterConfig.Api.USER + "/household/" + id;

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, getAuthHeaderMap(), new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    try {

                        final List<User> parentList = new ArrayList<>();

                        final JSONObject json = new JSONObject(response.getResult());

                        if (!json.getBoolean("error")) {

                            final JSONArray jsonArray = json.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                final JSONObject jsonObject = jsonArray.getJSONObject(i);

                                final User user = new User(
                                        jsonObject.getString("nick"),
                                        "",
                                        jsonObject.getString("id"),
                                        jsonObject.getString("dob"),
                                        jsonObject.getString("race"),
                                        jsonObject.getString("gender"));

                                try {

                                    user.setImage(jsonObject.getInt("picture"));

                                } catch (Exception e) {

                                }

                                parentList.add(user);
                            }
                        }

                        listener.onSuccess(parentList);

                    } catch (final Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }
}
