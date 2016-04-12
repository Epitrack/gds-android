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
 
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import java.util.Map;

/**
 * @author Igor Morais
 */
public interface IRequester {

    void request(Method method, String url, FutureCallback<Response<String>> listener);

    <T> void request(Method method, String url, T type, FutureCallback<Response<String>> listener);

    void request(Method method, String url, Map<String, String> headerMap, FutureCallback<Response<String>> listener);

    <T> void request(Method method, String url, Map<String, String> headerMap, T type, FutureCallback<Response<String>> listener);

    void request(Method method, String url, Map<String, String> headerMap, Map<String, String> paramMap, FutureCallback<Response<String>> listener);

    <T> void request(Method method, String url, Map<String, String> queryMap, Map<String, String> headerMap, T type, FutureCallback<Response<String>> listener);

    void request(Method method, String url, Map<String, String> queryMap, Map<String, String> headerMap, Map<String, String> bodyMap, FutureCallback<Response<String>> listener);
}
