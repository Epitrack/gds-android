package com.epitrack.guardioes.view.game.welcome;

import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public enum WelcomeGame {

    WELCOME (1, R.string.welcome_game_welcome, R.drawable.image_welcome_welcome),
    POINT (2, R.string.welcome_game_point, R.drawable.image_welcome_point),
    PHASE (3, R.string.welcome_game_phase, R.drawable.image_welcome_phase),
    PLAY (4, R.string.welcome_game_play, R.drawable.image_welcome_play),
    ANSWER (5, R.string.welcome_game_answer, R.drawable.image_welcome_answer),
    CARD (6, R.string.welcome_game_card, R.drawable.image_welcome_card);

    private final int id;
    private final int message;
    private final int image;

    WelcomeGame(final int id, final int message, final int image) {
        this.id = id;
        this.message = message;
        this.image = image;
    }

    public final int getId() {
        return id;
    }

    public final int getMessage() {
        return message;
    }

    public final int getImage() {
        return image;
    }

    public static WelcomeGame getBy(final int id) {

        for (final WelcomeGame welcome : WelcomeGame.values()) {

            if (welcome.getId() == id) {
                return welcome;
            }
        }

        throw new IllegalArgumentException("The WelcomeGame has not found.");
    }
}
