package com.epitrack.guardioes.model;

public enum Race {

    WHITE           (1, "branco"),
    BLACK           (2, "preto"),
    BROWN           (3, "pardo"),
    YELLOW          (4, "amarelo"),
    INDIGENOUS      (5, "indigena");

    private final int id;
    private final String value;

    Race(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    public final int getId() {
        return id;
    }

    public final String getValue() {
        return value;
    }

    public static Race getBy(final long id) {

        for (final Race race : Race.values()) {

            if (race.getId() == id) {
                return race;
            }
        }

        throw new IllegalArgumentException("The Race has not found.");
    }

    public static Race getBy(final String value) {

        for (final Race race : Race.values()) {

            if (race.getValue().equals(value)) {
                return race;
            }
        }

        throw new IllegalArgumentException("The Race has not found.");
    }
}
