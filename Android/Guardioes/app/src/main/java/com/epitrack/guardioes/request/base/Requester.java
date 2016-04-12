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
import android.util.Log;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.utility.Logger;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.util.Map;

/**
 * @author Igor Morais
 */
public final class Requester extends BaseRequester implements IRequester {
    
    private static final String TAG = Requester.class.getSimpleName();
    
    public Requester(final Context context) {
        super(context);
    }

    @Override
    public void request(final Method method, final String url, final FutureCallback<Response<String>> listener) {
        request(method, url, null, listener);
    }

    @Override
    public <T> void request(final Method method, final String url, final T type, final FutureCallback<Response<String>> listener) {
        request(method, url, null, type, listener);
    }

    @Override
    public void request(final Method method, final String url, final Map<String, String> headerMap, final FutureCallback<Response<String>> listener) {
        request(method, url, headerMap, null, listener);
    }

    @Override
    public <T> void request(final Method method, final String url, final Map<String, String> headerMap, final T type, final FutureCallback<Response<String>> listener) {
        request(method, url, null, headerMap, type, listener);
    }

    @Override
    public void request(final Method method, final String url, final Map<String, String> headerMap, final Map<String, String> paramMap, final FutureCallback<Response<String>> listener) {

        if (method == Method.HEAD || method == Method.GET) {
            request(method, url, paramMap, headerMap, null, listener);

        } else {
            request(method, url, null, headerMap, paramMap, listener);
        }
    }

    @Override
    public <T> void request(final Method method, final String url, final Map<String, String> queryMap, final Map<String, String> headerMap, final T type, final FutureCallback<Response<String>> listener) {

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
            Logger.logDebug(TAG, "The queryMap is null or empty");

        } else {

            for (final Map.Entry<String, String> entryMap : queryMap.entrySet()) {
                builder.addQuery(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (headerMap == null || headerMap.isEmpty()) {
            Logger.logDebug(TAG, "The headerMap is null or empty");

        } else {

            for (final Map.Entry<String, String> entryMap : headerMap.entrySet()) {
                builder.addHeader(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (type == null) {
            Logger.logDebug(TAG, "The body is null or empty");

        } else {

            builder.setJsonPojoBody(type);
        }

        if (BuildConfig.DEBUG) {
            builder.setLogging(TAG, Log.DEBUG);
        }

        builder.asString().withResponse()
                          .setCallback(new Handler(getContext(), builder).setListener(listener));
    }

    @Override
    public void request(final Method method, final String url, final Map<String, String> queryMap, final Map<String, String> headerMap, final Map<String, String> bodyMap, final FutureCallback<Response<String>> listener) {

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
            Logger.logDebug(TAG, "The queryMap is null or empty");

        } else {

            for (final Map.Entry<String, String> entryMap : queryMap.entrySet()) {
                builder.addQuery(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (headerMap == null || headerMap.isEmpty()) {
            Logger.logDebug(TAG, "The headerMap is null or empty");

        } else {

            for (final Map.Entry<String, String> entryMap : headerMap.entrySet()) {
                builder.addHeader(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (bodyMap == null || bodyMap.isEmpty()) {
            Logger.logDebug(TAG, "The bodyMap is null or empty");

        } else {

            for (final Map.Entry<String, String> entryMap : bodyMap.entrySet()) {
                builder.setBodyParameter(entryMap.getKey(), entryMap.getValue());
            }
        }

        if (BuildConfig.DEBUG) {
            builder.setLogging(TAG, Log.DEBUG);
        }

        builder.asString().withResponse()
                          .setCallback(new Handler(getContext(), builder).setListener(listener));
    }
}
