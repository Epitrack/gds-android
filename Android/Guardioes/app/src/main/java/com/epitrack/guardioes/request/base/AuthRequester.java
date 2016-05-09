/*
 * Copyright 2015 Igor Morais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epitrack.guardioes.request.base;

import android.content.Context;

import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.helper.Utility;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Igor Morais
 */
public final class AuthRequester extends BaseRequester {

    private static final String TAG = AuthRequester.class.getSimpleName();

    public AuthRequester(final Context context) {
        super(context);
    }

    public void loadAuth(final RequestListener<User> listener) {

        final String url = RequesterConfig.URL + "/user/lookup/";

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, getAuthHeaderMap(), null, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    if (isSuccess(response)) {

                        try {

                            final JSONObject json = new JSONObject(response.getResult());

                            if (json.getBoolean("error")) {

                                listener.onError(new RequestException());

                            } else {

                                loadUser(json.getJSONObject("data"));

                                final User user = SingleUser.getInstance();

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

    // TODO: Refactor soon..
    private void loadUser(JSONObject json) {

        try {

            final SingleUser user = SingleUser.getInstance();

            user.setNick(json.getString("nick"));
            user.setEmail(json.getString("email"));
            user.setGender(json.getString("gender"));
            user.setId(json.getString("id"));
            user.setRace(json.getString("race"));
            user.setDob(json.getString("dob"));
            user.setUserToken(json.getString("token"));

            try {
                user.setPath(json.getString("file"));

            } catch (Exception e) {
            }

            try {
                user.setImage(json.getInt("picture"));

            } catch (Exception e) {

            }

            try {

                user.setHashtags(Utility.toList(json.getJSONArray("hashtags")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
