package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestException;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.request.base.RequesterConfig;
import com.google.android.gms.maps.model.LatLng;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Morais
 */
public class SurveyRequester extends BaseRequester {

    public SurveyRequester(Context context) {
        super(context);
    }

    public void saveSurveyGood(final User user, final LatLng latLng, final RequestListener<Boolean> listener) {

        final String url = RequesterConfig.URL + "/survey/create";

        final SingleUser singleUser = SingleUser.getInstance();

        final Map<String, Object> bodyMap = new HashMap<>();

        bodyMap.put("user_id", singleUser.getId());

        if (!user.getId().equals(singleUser.getId())) {
            bodyMap.put("household_id", user.getId());
        }

        bodyMap.put("lat", user.getLat());
        bodyMap.put("lon", user.getLon());
        bodyMap.put("app_token", user.getAppToken());
        bodyMap.put("platform", user.getPlatform());
        bodyMap.put("client", user.getClient());
        bodyMap.put("no_symptom", "Y");
        bodyMap.put("token", singleUser.getUserToken());

        new Requester(getContext()).request(Method.POST, url, getAuthHeaderMap(), bodyMap, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    try {

                        final JSONObject jsonObject = new JSONObject(response.getResult());

                        if (jsonObject.getBoolean("error")) {
                            listener.onError(new RequestException(jsonObject.getString("message")));

                        } else {

                            listener.onSuccess(true);
                        }

                    } catch (JSONException e) {

                    }
                }
            }
        });
    }
}