/*
 * Copyright 2016 Igor Morais
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Morais
 */
public final class BundleManager implements IBundle {
    
    private static final String TAG = BundleManager.class.getSimpleName();

    private final Map<String, Object> bundleMap = new HashMap<>();

    private BundleManager() {

    }

    private static final class LazyLoader {
        private static final BundleManager INSTANCE = new BundleManager();
    }

    public static BundleManager with() {
        return LazyLoader.INSTANCE;
    }

    @Override
    public boolean contain(final String key) {
        return bundleMap.containsKey(key);
    }

    @Override
    public <T> T get(final String key) {
        return (T) bundleMap.get(key);
    }

    @Override
    public <T> T put(final String key, final T type) {
        return (T) bundleMap.put(key, type);
    }

    @Override
    public <T> T remove(final String key) {
        return (T) bundleMap.remove(key);
    }

    @Override
    public void clear() {
        bundleMap.clear();
    }
}
