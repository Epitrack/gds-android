package com.epitrack.guardioes.view.menu.profile;

import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public enum Avatar {

    AVATAR_CHILD_FEMALE_1   (1, R.drawable.image_avatar_small_1, R.drawable.image_avatar_1),
    AVATAR_CHILD_FEMALE_2   (2, R.drawable.image_avatar_small_5, R.drawable.image_avatar_5),
    AVATAR_CHILD_MALE_1     (3, R.drawable.image_avatar_small_4, R.drawable.image_avatar_4),
    AVATAR_CHILD_MALE_2     (4, R.drawable.image_avatar_small_6, R.drawable.image_avatar_6),
    AVATAR_ADULT_MALE_1     (5, R.drawable.image_avatar_small_2, R.drawable.image_avatar_2),
    AVATAR_ADULT_MALE_2     (6, R.drawable.image_avatar_small_3, R.drawable.image_avatar_3),
    AVATAR_ADULT_FEMALE_1   (7, R.drawable.image_avatar_small_7, R.drawable.image_avatar_7),
    AVATAR_ADULT_FEMALE_2   (8, R.drawable.image_avatar_small_8, R.drawable.image_avatar_8),
    AVATAR_OLD_MALE_1       (9, R.drawable.image_avatar_small_11, R.drawable.image_avatar_11),
    AVATAR_OLD_FEMALE_1     (10, R.drawable.image_avatar_small_9, R.drawable.image_avatar_9),
    AVATAR_OLD_MALE_2       (11, R.drawable.image_avatar_small_12, R.drawable.image_avatar_12),
    AVATAR_OLD_FEMALE_2     (12, R.drawable.image_avatar_small_10, R.drawable.image_avatar_10),
    AVATAR_CHILD_MALE_13     (13, R.drawable.image_avatar_small_6, R.drawable.image_avatar_6),
    AVATAR_CHILD_MALE_14     (14, R.drawable.image_avatar_small_6, R.drawable.image_avatar_6),
    AVATAR_CHILD_MALE_15     (15, R.drawable.image_avatar_small_6, R.drawable.image_avatar_6),
    AVATAR_CHILD_MALE_16     (16, R.drawable.image_avatar_small_6, R.drawable.image_avatar_6);

    private final int id;
    private final int small;
    private final int large;

    Avatar(final int id, final int small, final int large) {
        this.id = id;
        this.small = small;
        this.large = large;
    }

    public int getId() {
        return id;
    }

    public int getSmall() {
        return small;
    }

    public int getLarge() {
        return large;
    }

    public static Avatar getBy(final int id) {

        for (final Avatar avatar : Avatar.values()) {

            if (avatar.getId() == id) {
                return avatar;
            }
        }

        throw new IllegalArgumentException("The Avatar has not found.");
    }
}
