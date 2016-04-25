package com.epitrack.guardioes.helper;

/**
 * @author Igor Morais
 */
public final class Constants {

    private Constants() {

    }

    public static final int MAX_USER = 10;

    public static final String PATH = "file://";
    public static final String DIRECTORY_TEMP = "Guardioes";

    public static class Google {

        public static final String KEY_API_MAPS = "AIzaSyDYl7spN_NpAjAWL7Hi183SK2cApiIS3Eg";
        public static final int  API_UNAVAILABLE = 16;
        public static final int  CANCELED = 13;
        public static final int  DEVELOPER_ERROR = 10;
        public static final int  INTERNAL_ERROR = 8;
        public static final int  INTERRUPTED = 15;
        public static final int  INVALID_ACCOUNT = 5;
        public static final int  LICENSE_CHECK_FAILED = 11;
        public static final int  NETWORK_ERROR = 7;
        public static final int  RESOLUTION_REQUIRED = 6;
        public static final int  SERVICE_DISABLED = 3;
        public static final int  SERVICE_INVALID = 9;
        public static final int  SERVICE_MISSING = 1;
        public static final int  SERVICE_MISSING_PERMISSION = 19;
        public static final int  SERVICE_UPDATING = 18;
        public static final int  SERVICE_VERSION_UPDATE_REQUIRED = 2;
        public static final int  SIGN_IN_FAILED = 17;
        public static final int  SIGN_IN_REQUIRED = 4;
        public static final int  SUCCESS = 0;
        public static final int  TIMEOUT = 14;

    }

    public static class General {

        public static final String ADD = "ADD";
        public static final String REMOVE = "REMOVE";
    }

    public static class Twitter {

        public static final String KEY = "btijYH36dtguxb6CcHTprkERG";
        public static final String SECRET = "UJR1qUL7ReNnEnFulMKumaP84Ff9JUxffNybxbr5oyMdIo6wro";
    }

    public static class Config {

    }

    public static class State {

        public static final String GOOD = "GOOD";
        public static final String BAD = "BAD";
    }

    public static class RequestCode {

        public static final int IMAGE = 5000;
    }

    public static class Bundle {

        public static final String WELCOME = "Welcome";
        public static final String TIP = "Tip";
        public static final String AVATAR = "Avatar";
        public static final String IMAGE = "Image";
        public static final String URI = "Uri";
        public static final String MAIN_MEMBER = "main_member";
        public static final String BAD_STATE = "has_bad_state";
        public static final String NEW_MEMBER = "new_member";
        public static final String SOCIAL_NEW = "social_new";
        public static final String GOOGLE = "google";
        public static final String FACEBOOK = "facebook";
        public static final String TWITTER = "twitter";
        public static final String EMAIL = "Email";
        public static final String ACCESS_SOCIAL = "access_social";
        public static final String PATH = "Path";
        public static final String NAME = "Name";
        public static final String DELETE = "Delete";
    }

    public static class Pref {

        public static final String USER = "user";
        public static final String PREFS_NAME = "preferences_user_token";
        public static final String PREFS_SOCIAL = "preferences_social";
        public static final String PREFS_IMAGE = "preferences_image";
        public static final String PREFS_IMAGE_USER_TOKEN = "preferences_image_user_token";
    }

    public static class Push {

        public static final String SENDER = "1008511817333";
        public static final String SENDER_ID = "SENDER_ID";
    }
}
