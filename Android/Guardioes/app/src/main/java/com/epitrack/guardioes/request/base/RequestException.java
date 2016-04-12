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

/**
 * @author Igor Morais
 */
public class RequestException extends Exception {

    public RequestException() {

    }

    public RequestException(final String message) {
        super(message);
    }

    public RequestException(final String message, final Exception e) {
        super(message, e);
    }

    public RequestException(final Exception e) {
        super(e);
    }
}
