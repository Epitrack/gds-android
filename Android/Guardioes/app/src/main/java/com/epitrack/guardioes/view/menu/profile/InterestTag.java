package com.epitrack.guardioes.view.menu.profile;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;

/**
 * @author Igor Morais
 */
public enum InterestTag implements IMenu {

    MINISTRY    (1, R.string.tag_ministry),
    FLU         (2, R.string.tag_flu),
    APP         (3, R.string.tag_app);

    private final int id;
    private final int name;
    private String idApi;

    InterestTag(final int id, final int name) {
        this.id = id;
        this.name = name;
    }

    InterestTag(final int id, final int name, String idApi) {
        this.id = id;
        this.name = name;
        this.idApi = idApi;
    }

    @Override
    public final int getId() {
        return id;
    }

    @Override
    public final int getName() {
        return name;
    }

    @Override
    public final int getIcon() {
        return 0;
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public boolean isDialogFragment() {
        return false;
    }

    @Override
    public boolean isFragment() {
        return false;
    }

    @Override
    public boolean isActivity() {
        return false;
    }

    @Override
    public Class<?> getType() {
        return null;
    }


    public String getIdApi() {
        return idApi;
    }

    public void setIdApi(String idApi) {
        this.idApi = idApi;
    }

    public static InterestTag getBy(final int id) {

        for (final InterestTag tag : InterestTag.values()) {

            if (tag.getId() == id) {
                return tag;
            }
        }

        throw new IllegalArgumentException("The InterestTag has not found.");
    }

}
