package com.epitrack.guardioes.model;

public enum Gender {

    MALE (1, "M"),
    FEMALE (2, "F");

    private final int id;
    private final String value;

    Gender(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    public final int getId() {
        return id;
    }

    public final String getValue() {
        return value;
    }

    public static Gender getBy(final long id) {

        for (final Gender race : Gender.values()) {

            if (race.getId() == id) {
                return race;
            }
        }

        throw new IllegalArgumentException("The Gender has not found.");
    }

    public static Gender getBy(final String value) {

        for (final Gender race : Gender.values()) {

            if (race.getValue().equals(value)) {
                return race;
            }
        }

        throw new IllegalArgumentException("The Gender has not found.");
    }
}
