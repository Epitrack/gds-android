package com.epitrack.guardioes.view.game.welcome;

import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public enum Welcome {

    ONE (1, R.string.welcome_game_message_1, R.drawable.image_welcome_game_1),
    TWO (2, R.string.welcome_game_message_2, R.drawable.image_welcome_game_2),
    THREE (3, R.string.welcome_game_message_3, R.drawable.image_welcome_game_3),
    FOUR (4, R.string.welcome_game_message_4, R.drawable.image_welcome_game_4);

    private final int id;
    private final int message;
    private final int image;

    Welcome(final int id, final int message, final int image) {
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

    public static Welcome getBy(final int id) {

        for (final Welcome welcome : Welcome.values()) {

            if (welcome.getId() == id) {
                return welcome;
            }
        }

        throw new IllegalArgumentException("The Welcome has not found.");
    }
}
