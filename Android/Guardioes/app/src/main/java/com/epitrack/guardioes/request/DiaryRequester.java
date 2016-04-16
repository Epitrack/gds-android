package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.request.base.RequesterConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

public class DiaryRequester extends BaseRequester {

    public DiaryRequester(final Context context) {
        super(context);
    }

    public void loadPieChart(final String url, final RequestListener<String> listener) {

        listener.onStart();

        new Requester(getContext()).request(Method.GET, RequesterConfig.URL + "/" + url, getAuthHeaderMap(), new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception e, Response<String> response) {

                if (e == null) {

                    listener.onSuccess(response.getResult());

                } else {

                    listener.onError(e);
                }
            }
        });
    }
}
