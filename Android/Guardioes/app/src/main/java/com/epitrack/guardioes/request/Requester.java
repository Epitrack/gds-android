package com.epitrack.guardioes.request;

import android.content.Context;
import android.util.Log;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.utility.Logger;
import com.epitrack.guardioes.utility.Utility;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.util.Map;

/**
 * @author Igor Morais
 */
public class Requester extends BaseRequester implements IRequester {

    private static final String TAG = Requester.class.getSimpleName();
    //public static final String API_URL = "https://api.guardioesdasaude.org/";
    public static final String API_URL = "http://rest.guardioesdasaude.org/";

    public Requester(final Context context) {
        super(context);
    }

    @Override
    public <T> void request(final Method method,
                            final String url,
                            final RequestListener<T> listener) {

        request(method, url, null, null, null, listener);
    }

    @Override
    public <T> void request(final Method method,
                            final String url,
                            final Map<String, String> headerMap,
                            final RequestListener<T> listener) {

        request(method, url, null, headerMap, null, listener);
    }

    @Override
    public <T> void request(final Method method,
                            final String url,
                            final Map<String, String> paramMap,
                            final Map<String, String> headerMap,
                            final RequestListener<T> listener) {

        if (method == Method.GET || method == Method.HEAD || method == Method.DELETE) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The paramMap is a queryMap.");
            }

            request(method, url, paramMap, headerMap, null, listener);

        } else {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The paramMap is a bodyMap.");
            }

            request(method, url, null, headerMap, paramMap, listener);
        }
    }

    @Override
    public <E, T> void request(final Method method,
                               final String url,
                               final Map<String, String> headerMap,
                               final E body,
                               final RequestListener<T> listener) {

        request(method, url, null, null, body, listener);
    }

    @Override
    public <T> void request(final Method method,
                            final String url,
                            final Map<String, String> queryMap,
                            final Map<String, String> headerMap,
                            final Map<String, String> bodyMap,
                            final RequestListener<T> listener) {

        if (method == null) {
            throw new IllegalArgumentException("The method cannot be null.");
        }

        if (url == null) {
            throw new IllegalArgumentException("The url cannot be null.");
        }

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        final Builders.Any.B builder = Ion.with(getContext())
                                          .load(method.getName(), url);

        if (queryMap == null || queryMap.isEmpty()) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The queryMap is null or empty.");
            }

        } else {

            for (final Map.Entry<String, String> entryMap : queryMap.entrySet()) {
                builder.addQuery(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (headerMap == null || headerMap.isEmpty()) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The headerMap is null or empty.");
            }

        } else {

            for (final Map.Entry<String, String> entryMap : headerMap.entrySet()) {
                builder.addHeader(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (bodyMap == null || bodyMap.isEmpty()) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The bodyMap is null or empty.");
            }

        } else {

            for (final Map.Entry<String, String> entryMap : bodyMap.entrySet()) {
                builder.setBodyParameter(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (BuildConfig.DEBUG) {

            Utility.print(queryMap);
            Utility.print(headerMap);
            Utility.print(bodyMap);

            builder.setLogging(TAG, Log.DEBUG);
        }

        builder.asString().setCallback(new RequestHandler<>(listener));
    }

    @Override
    public <E, T> void request(final Method method,
                               final String url,
                               final Map<String, String> queryMap,
                               final Map<String, String> headerMap,
                               final E body,
                               final RequestListener<T> listener) {

        if (method == null) {
            throw new IllegalArgumentException("The method cannot be null.");
        }

        if (url == null) {
            throw new IllegalArgumentException("The url cannot be null.");
        }

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        final Builders.Any.B builder = Ion.with(getContext())
                .load(method.getName(), url);

        if (queryMap == null || queryMap.isEmpty()) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The queryMap is null or empty.");
            }

        } else {

            for (final Map.Entry<String, String> entryMap : queryMap.entrySet()) {
                builder.addQuery(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (headerMap == null || headerMap.isEmpty()) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The headerMap is null or empty.");
            }

        } else {

            for (final Map.Entry<String, String> entryMap : headerMap.entrySet()) {
                builder.addHeader(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (body == null) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The body is null.");
            }

        } else {

            builder.setJsonPojoBody(body);
        }

        if (BuildConfig.DEBUG) {

            Utility.print(queryMap);
            Utility.print(headerMap);
            Utility.print(body);

            builder.setLogging(TAG, Log.DEBUG);
        }

        builder.asString().setCallback(new RequestHandler<>(listener));
    }
}
