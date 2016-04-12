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

import com.epitrack.guardioes.model.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

/**
 * @author Igor Morais
 */
class Handler implements FutureCallback<Response<String>> {
    
    private final Context context;

    private final Builders.Any.B builder;

    private FutureCallback<Response<String>> listener;

    public Handler(final Context context, final Builders.Any.B builder) {
        this.context = context;
        this.builder = builder;
    }
    
    private boolean isUnAuth(final Response<String> response) {
        return response.getHeaders().code() == StatusCode.UNAUTHORIZED.getCode();
    }
    
    public final Handler setListener(final FutureCallback<Response<String>> listener) {
        this.listener = listener;

        return this;
    }
    
    private class AuthHandler implements RequestListener<User> {
        
        @Override
        public final void onStart() {

        }

        @Override
        public final void onError(final Exception e)  {
            listener.onCompleted(e, null);
        }

        @Override
        public final void onSuccess(final User user) {
            builder.asString().withResponse().setCallback(listener);
        }
    }
    
    @Override
    public final void onCompleted(final Exception e, final Response<String> response) {
        
        if (e == null) {
            
            if (isUnAuth(response)) {
                
                new AuthRequester(context).loadAuth(new AuthHandler());

            } else {

                listener.onCompleted(null, response);
            }

        } else {

            listener.onCompleted(e, response);
        }
    }
}
