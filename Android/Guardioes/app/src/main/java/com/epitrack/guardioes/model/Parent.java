package com.epitrack.guardioes.model;

public enum Parent {

    PAI        (1, "pai"),
    MAE        (2, "mae"),
    FILHO      (3, "filho"),
    IRMAO      (4, "irmao"),
    AVO        (5, "avo"),
    NETO       (6, "neto"),
    TIO        (7, "tio"),
    SOBRINHO   (8, "sobrinho"),
    BISAVO     (9, "bisavo"),
    BISNETO    (10, "bisneto"),
    PRIMO      (11, "primo"),
    SOGRO      (12, "sogro"),
    GENRO      (13, "genro"),
    NORA       (14, "nora"),
    PADRASTO   (15, "padrasto"),
    MADRASTA   (16, "madrasta"),
    ENTEADO    (17, "enteado"),
    CONJUGE    (18, "conjuge"),
    OUTROS     (19, "outros");

    private final int id;
    private final String value;

    Parent(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    public final int getId() {
        return id;
    }

    public final String getValue() {
        return value;
    }

    public static Parent getBy(final long id) {

        for (final Parent parent : Parent.values()) {

            if (parent.getId() == id) {
                return parent;
            }
        }

        throw new IllegalArgumentException("The Parent has not found.");
    }

    public static Parent getBy(final String value) {

        for (final Parent parent : Parent.values()) {

            if (parent.getValue().equals(value)) {
                return parent;
            }
        }

        throw new IllegalArgumentException("The Parent has not found.");
    }
}
