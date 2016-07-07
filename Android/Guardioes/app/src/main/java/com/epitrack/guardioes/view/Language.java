package com.epitrack.guardioes.view;

public enum Language {

    ARABIC          (7, "AR"),
    CHINESE         (6, "ZH"),
    ENGLISH         (1, "EN"),
    FRENCH          (3, "FR"),
    PORTUGUESE      (4, "PT"),
    RUSSIAN         (5, "RU"),
    SPANISH         (2, "ES");

    private final int id;
    private final String locale;

    Language(final int id, final String locale) {
        this.id = id;
        this.locale = locale;
    }

    public final int getId() {
        return id;
    }

    public final String getLocale() {
        return locale;
    }

    public static Language getBy(final long id) {

        for (final Language language : Language.values()) {

            if (language.getId() == id) {
                return language;
            }
        }

        throw new IllegalArgumentException("The Language has not found.");
    }
}
