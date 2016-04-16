package com.epitrack.guardioes.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.Application;
import com.epitrack.guardioes.model.Notice;
import com.epitrack.guardioes.model.Point;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.utility.Logger;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Igor Morais
 */
public class MapRequester extends BaseRequester {

    private static final String TAG = NoticeRequester.class.getSimpleName();

    public MapRequester(final Context context) {
        super(context);
    }

    // TODO: Removing self certificate, but on finish we add it. This is not the best way..
    public void loadPharmacy(final double latitude, final double longitude, final RequestListener<List<Point>> listener) {

        Ion.getDefault(getContext()).getHttpClient().getSSLSocketMiddleware().setSSLContext(null);
        Ion.getDefault(getContext()).getHttpClient().getSSLSocketMiddleware().setTrustManagers(null);

        final String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=pharmacy&location=" + latitude + "," + longitude + "&radius=10000&key=AIzaSyDYl7spN_NpAjAWL7Hi183SK2cApiIS3Eg";

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                // TODO: Removing self certificate, but on finish we add it. This is not the best way..
                if (getContext().getApplicationContext() instanceof Application) {
                    ((Application) getContext().getApplicationContext()).loadAuth();
                }

                if (error == null) {

                    try {

                        final List<Point> pointList = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(response.getResult());

                        if (jsonObject.getString("status").equalsIgnoreCase("ok")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                JSONObject jsonObjectLocation = jsonObject.getJSONObject("geometry").getJSONObject("location");

                                final Point point = new Point();

                                point.setLatitude(jsonObjectLocation.getDouble("lat"));
                                point.setLongitude(jsonObjectLocation.getDouble("lng"));
                                point.setLogradouro(jsonObject.getString("formatted_address"));
                                point.setName(jsonObject.getString("name"));

                                pointList.add(point);
                            }

                            listener.onSuccess(pointList);
                        }

                    } catch (final Exception e) {
                        Logger.logError(TAG, e.getMessage());

                        listener.onError(e);
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }
}
