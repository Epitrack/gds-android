package com.epitrack.guardioes.request;

import android.content.Context;
import android.util.Log;

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

import java.util.Calendar;
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
        bodyMap.put("app_token", user.getAppToken());
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

    public void getSymptom(final RequestListener<String> listener) {

        final String url = RequesterConfig.URL + "/symptoms";

        new Requester(getContext()).request(Method.GET, url ,getAuthHeaderMap(), new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception e, final Response<String> response) {

                if (e == null) {

                    listener.onSuccess(response.getResult());

                } else {

                    listener.onError(e);
                }
            }
        });
    }

    public void sendSurvey(final Map<String, Object> bodyMap, final RequestListener<String> listener) {

        final String url = RequesterConfig.URL + "/survey/create";

        new Requester(getContext()).request(Method.POST, url, getAuthHeaderMap(), bodyMap, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception e, final Response<String> response) {

                if (e == null) {

                    listener.onSuccess(response.getResult());

                } else {

                    listener.onError(e);
                }
            }
        });
    }

    public void hasSurvey(final Calendar calendar, final RequestListener<Boolean> listener) {

        final String url = RequesterConfig.URL + "/user/calendar/day";

        final Map<String, String> paramMap = new HashMap<>();

        paramMap.put("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        paramMap.put("month", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        paramMap.put("year", String.valueOf(calendar.get(Calendar.YEAR)));

        new Requester(getContext()).request(Method.GET, url, paramMap, getAuthHeaderMap(), null, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    try {

                        final JSONObject jsonObject = new JSONObject(response.getResult());

                        if (jsonObject.getBoolean("error")) {

                            listener.onError(new RequestException());

                        } else {

                            if (jsonObject.getJSONArray("data").length() == 0) {

                                listener.onSuccess(false);

                            } else {

                                listener.onSuccess(true);
                            }
                        }

                    } catch (JSONException e) {

                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }

    public void hasSurveyToday(final RequestListener<Integer> listener) {

        final String url = RequesterConfig.URL + "/user/calendar/day";

        final Map<String, String> paramMap = new HashMap<>();

        final Calendar calendar = Calendar.getInstance();

        paramMap.put("day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        paramMap.put("month", String.valueOf(calendar.get(Calendar.MONTH)));
        paramMap.put("year", String.valueOf(calendar.get(Calendar.YEAR)));

        new Requester(getContext()).request(Method.GET, url, paramMap, getAuthHeaderMap(), null, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    try {

                        final JSONObject jsonObject = new JSONObject(response.getResult());
                        Log.d("jsonObject",""+ jsonObject.toString());
                        if (jsonObject.getBoolean("error")) {

                            listener.onError(new RequestException());

                        } else {

                            listener.onSuccess(jsonObject.getJSONArray("data").length());
                        }

                    } catch (JSONException e) {

                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }
}
