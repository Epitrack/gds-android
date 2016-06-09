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

    public static class RequestCode {

        public static final int IMAGE = 5000;
    }

    public static class Json {

        public static final String COUNTRY = "country";
        public static final String POSITION = "position";
        public static final String URL = "flagUrl";

        public static final String TITLE = "title";
        public static final String OPTION = "option";
        public static final String CORRECT = "correct";
        public static final String QUESTION_LIST = "questions";
        public static final String OPTION_LIST = "alternatives";
    }

    public static class Bundle {

        public static final String WELCOME = "Welcome";
        public static final String WELCOME_GAME = "WelcomeGame";
        public static final String TIP = "Tip";
        public static final String AVATAR = "Avatar";
        public static final String MAIN_MEMBER = "main_member";
        public static final String BAD_STATE = "has_bad_state";
        public static final String NEW_MEMBER = "new_member";
        public static final String SOCIAL_NEW = "social_new";
        public static final String EMAIL = "Email";
        public static final String TYPE = "TYPE";
        public static final String PATH = "Path";
        public static final String DELETE = "Delete";
        public static final String NAME = "Name";
        public static final String MAIL = "Mail";
        public static final String DATE = "Date";
        public static final String USER = "User";
    }

    public static class Pref {

        public static final String USER = "user";
        public static final String PREFS_NAME = "preferences_user_token";
    }
}
