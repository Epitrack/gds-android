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

import com.epitrack.guardioes.utility.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koushikdutta.ion.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Igor Morais
 */
public class BaseRequester {
    
    private static final String TAG = BaseRequester.class.getSimpleName();

    protected static final int EMPTY = 0;
    
    private final Context context;
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    public BaseRequester(final Context context) {
        this.context = context;
    }
    
    protected final Context getContext() {
        return context;
    }
    
    protected final ObjectMapper getMapper() {
        return mapper;
    }
    
    protected final boolean isSuccess(final Response<String> response) {
        
        final StatusCode[] statusCodeArray = { StatusCode.OK,
                                               StatusCode.CREATED,
                                               StatusCode.ACCEPTED,
                                               StatusCode.NON_AUTHORITATIVE_INFORMATION,
                                               StatusCode.NO_CONTENT,
                                               StatusCode.RESET_CONTENT,
                                               StatusCode.PARTIAL_CONTENT,
                                               StatusCode.MULTI_STATUS,
                                               StatusCode.ALREADY_REPORTED,
                                               StatusCode.IM_USED };
                                               
        for (final StatusCode statusCode : statusCodeArray) {
            
            if (statusCode.getCode() == response.getHeaders().code()) {
                return true;
            }
        }
        
        return false;
    }
    
    protected final boolean isUnAuth(final Response<String> response) {
        return response.getHeaders().code() == StatusCode.UNAUTHORIZED.getCode();
    }
    
    protected final Map<String, String> getHeaderMap() {
        
        final Map<String, String> headerMap = new HashMap<>();
        
        headerMap.put(Header.CONTENT_TYPE, Header.ContentType.JSON);
        
        return headerMap;
    }
    
    protected final Map<String, String> getAuthHeaderMap() {
        
        final Map<String, String> headerMap = new HashMap<>();
        
        headerMap.put(Header.CONTENT_TYPE, Header.ContentType.JSON);
        headerMap.put(Header.AUTHORIZATION, "");
        
        return headerMap;
    }
    
    protected final <T> T toEntity(final String json, final Class<T> type) {
        
        try {
            
            return getMapper().readValue(json, type);

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }
        
        return null;
    }
    
    protected final <T> T toEntity(final String json, final TypeReference<T> type) {
        
        try {
            
            return getMapper().readValue(json, type);

        } catch (final IOException e) {
            Logger.logError(TAG, e.getMessage());
        }
        
        return null;
    }
}
