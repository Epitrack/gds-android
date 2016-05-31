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

import android.app.Activity;
import android.content.Context;

import com.epitrack.guardioes.view.dialog.LoadDialog;

import java.net.UnknownHostException;

/**
 * @author Igor Morais
 */
public abstract class RequestHandler<T> implements RequestListener<T> {

    private final Context context;

    private final LoadDialog loadDialog = new LoadDialog();

    public RequestHandler(final Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {

        if (context instanceof Activity) {
            loadDialog.show(((Activity) context).getFragmentManager(), LoadDialog.TAG);
        }
    }

    @Override
    public void onError(final Exception e) {

        loadDialog.dismiss();

        if (e instanceof UnknownHostException) {

        } else if (e instanceof AuthException) {

        }
    }

    @Override
    public void onSuccess(final T type) {
        loadDialog.dismiss();
    }
}
