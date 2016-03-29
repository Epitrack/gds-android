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
 
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.epitrack.guardioes.utility.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;

/**
 * @author Igor Morais
 */
public final class PrefManager extends BaseManager implements IPref {

    private static final String TAG = PrefManager.class.getSimpleName();

    private ObjectMapper mapper;

    private SharedPreferences pref;

    public PrefManager(final Context context) {
        super(context);
    }

    private ObjectMapper getMapper() {

        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        return mapper;
    }

    private SharedPreferences getPref() {

        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        }

        return pref;
    }

    @Override
    public boolean contain(final String key) {
        return getPref().contains(key);
    }

    @Override
    public boolean getBoolean(final String key, final boolean defValue) {
        return getPref().getBoolean(key, defValue);
    }

    @Override
    public int getInt(final String key, final int defValue) {
        return getPref().getInt(key, defValue);
    }

    @Override
    public long getLong(final String key, final long defValue) {
        return getPref().getLong(key, defValue);
    }

    @Override
    public float getFloat(final String key, final float defValue) {
        return getPref().getFloat(key, defValue);
    }

    @Override
    public String getString(final String key, final String defValue) {
        return getPref().getString(key, defValue);
    }

    @Override
    public Set<String> getSet(final String key, final Set<String> defValue) {
        return getPref().getStringSet(key, defValue);
    }
    
    @Override
    public <T> T get(final String key, final Class<T> type) {
        
        final String json = getPref().getString(key, null);
        
        if (json == null) {
            return null;
        }

        try {

            return getMapper().readValue(json, type);

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }

        return null;
    }
    
    @Override
    public <T> T get(final String key, final TypeReference<T> type) {
        
        final String json = getPref().getString(key, null);

        if (json == null) {
            return null;
        }

        try {

            return getMapper().readValue(json, type);

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }

        return null;
    }

    @Override
    public boolean putBoolean(final String key, final boolean value) {

        return getPref().edit()
                        .putBoolean(key, value)
                        .commit();
    }

    @Override
    public boolean putInt(final String key, final int value) {

        return getPref().edit()
                        .putInt(key, value)
                        .commit();
    }

    @Override
    public boolean putLong(final String key, final long value) {

        return getPref().edit()
                        .putLong(key, value)
                        .commit();
    }

    @Override
    public boolean putFloat(final String key, final float value) {

        return getPref().edit()
                        .putFloat(key, value)
                        .commit();
    }

    @Override
    public boolean putString(final String key, final String value) {

        return getPref().edit()
                        .putString(key, value)
                        .commit();
    }

    @Override
    public boolean putSet(final String key, final Set<String> value) {

        return getPref().edit()
                        .putStringSet(key, value)
                        .commit();
    }

    @Override
    public <T> boolean put(final String key, final T entity) {

        try {

            final String json = getMapper().writeValueAsString(entity);

            return getPref().edit()
                            .putString(key, json)
                            .commit();

        } catch (JsonProcessingException e) {
            Logger.logError(TAG, e.getMessage());
        }

        return false;
    }

    @Override
    public boolean remove(final String key) {

        return getPref().edit()
                        .remove(key)
                        .commit();
    }

    @Override
    public boolean clear() {

        return getPref().edit()
                        .clear()
                        .commit();
    }
}