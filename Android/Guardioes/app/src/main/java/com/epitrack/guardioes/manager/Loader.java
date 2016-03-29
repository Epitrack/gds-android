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

package com.epitrack.guardioes.manager;

import android.os.Handler;
import android.os.HandlerThread;

import com.epitrack.guardioes.utility.Logger;

/**
 * @author Igor Morais
 */
public final class Loader {

    private static final String TAG = Loader.class.getSimpleName();

    private Handler handler;

    private final HandlerThread handlerThread = new HandlerThread(TAG);

    private Loader() {

        handlerThread.start();

        Logger.logDebug(TAG, "Start");
    }

    private static final class LazyLoader {
        private static final Loader INSTANCE = new Loader();
    }

    public static Loader with() {
        return LazyLoader.INSTANCE;
    }

    public final Handler handler() {

        if (handler == null) {
            handler = new Handler(handlerThread.getLooper());
        }

        return handler;
    }
}