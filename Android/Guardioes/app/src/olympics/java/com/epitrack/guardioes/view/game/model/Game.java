package com.epitrack.guardioes.view.game.model;

import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public enum Game {

    ONE (1, R.drawable.image_question);

    private final int position;
    private final int image;

    Game(final int position, final int image) {
        this.position = position;
        this.image = image;
    }

    public int getPosition() {
        return position;
    }

    public int getImage() {
        return image;
    }
}
