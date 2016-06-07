package com.epitrack.guardioes.view.game.model;

public enum Phase {

    ONE (1, 500, 500);

    private final int id;
    private final int x;
    private final int y;

    Phase(final int id, final int x, final int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public final int getId() {
        return id;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }
}
