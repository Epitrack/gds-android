package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.request.base.RequesterConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserRequester extends BaseRequester {

    public UserRequester(final Context context) {
        super(context);
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

                                final User user = new User(json.getInt("picture"),
                                        jsonObject.getString("nick"),
                                        "",
                                        jsonObject.getString("id"),
                                        jsonObject.getString("dob"),
                                        jsonObject.getString("race"),
                                        jsonObject.getString("gender"));

                                try {

                                    user.setRelationship(jsonObject.getString("relationship"));

                                } catch (Exception e) {
                                    user.setRelationship("");
                                }

                                try {

                                    user.setEmail(jsonObject.getString("email"));

                                } catch (Exception e) {
                                    user.setEmail("");
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

                                final User user = new User(json.getInt("picture"),
                                        jsonObject.getString("nick"),
                                        "",
                                        jsonObject.getString("id"),
                                        jsonObject.getString("dob"),
                                        jsonObject.getString("race"),
                                        jsonObject.getString("gender"));

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
