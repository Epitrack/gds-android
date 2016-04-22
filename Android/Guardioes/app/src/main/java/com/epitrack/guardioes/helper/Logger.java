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

package com.epitrack.guardioes.helper;

import android.util.Log;

/**
 * @author Igor Morais
 */
public final class Logger {

    private Logger() {

    }

    public static void logDebug(final String tag, final String debugMessage) {
        Log.d(tag, debugMessage);
    }

    public static <T> void logDebug(final Class<T> someClass, final String debugMessage) {
        logDebug(someClass.getSimpleName(), debugMessage);
    }

    public static void logDebug(final Object object, final String debugMessage) {
        logDebug(object.getClass().getSimpleName(), debugMessage);
    }

    public static void logInfo(final String tag, final String infoMessage) {
        Log.i(tag, infoMessage);
    }

    public static <T> void logInfo(final Class<T> someClass, final String infoMessage) {
        logInfo(someClass.getSimpleName(), infoMessage);
    }

    public static void logInfo(final Object object, final String infoMessage) {
        logInfo(object.getClass().getSimpleName(), infoMessage);
    }

    public static void logWarn(final String tag, final String warnMessage) {
        Log.w(tag, warnMessage);
    }

    public static <T> void logWarn(final Class<T> someClass, final String warnMessage) {
        logWarn(someClass.getSimpleName(), warnMessage);
    }

    public static void logWarn(final Object object, final String warnMessage) {
        logWarn(object.getClass().getSimpleName(), warnMessage);
    }

    public static void logError(final String tag, final String errorMessage) {
        Log.e(tag, errorMessage);
    }

    public static <T> void logError(final Class<T> someClass, final String errorMessage) {
        logError(someClass.getSimpleName(), errorMessage);
    }

    public static void logError(final Object object, final String errorMessage) {
        logError(object.getClass().getSimpleName(), errorMessage);
    }
}