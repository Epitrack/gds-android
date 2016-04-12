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
 public enum Method {

    OPTIONS ("OPTIONS"),
    GET ("GET"),
    HEAD ("HEAD"),
    POST ("POST"),
    PUT ("PUT"),
    PATCH ("PATCH"),
    DELETE ("DELETE"),
    TRACE ("TRACE"),
    CONNECT ("CONNECT");
    
    private final String name;

    Method(final String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public static Method getBy(final String name) {

        for (final Method method : Method.values()) {

            if (method.getName().equals(name)) {
                return method;
            }
        }

        throw new IllegalArgumentException("The Method has not found.");
    }
}
