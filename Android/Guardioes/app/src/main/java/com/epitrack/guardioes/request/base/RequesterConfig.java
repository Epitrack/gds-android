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
public final class RequesterConfig {
    
    private static final String ADDRESS = "https://api.guardioesdasaude.org";
    private static final String ADDRESS_DEV = "http://rest.guardioesdasaude.org";
    private static final String PORT = "";
    private static final String CONTEXT = "";
    
    public static final String URL_PROD = ADDRESS + CONTEXT;
    public static final String URL_DEV = ADDRESS_DEV + CONTEXT;
    public static final String URL = URL_PROD;
    
    public static final int TIMEOUT = 25;
    
    public static class Api {
        
        public static final String USER = "/user";
        public static final String NOTICE = "/news";
    }
}
