package com.epitrack.guardioes.view.menu.help;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;

/**
 * @author Igor Morais
 */
public enum HelpOption implements IMenu {

    TUTORIAL    (1, R.string.tutorial, R.drawable.icon_tutorial),
    TERM        (2, R.string.term, R.drawable.icon_term);

    private final int id;
    private final int name;
    private final int icon;

    HelpOption(final int id, final int name, final int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
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
        return icon;
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

    public static HelpOption getBy(final int id) {

        for (final HelpOption help : HelpOption.values()) {

            if (help.getId() == id) {
                return help;
            }
        }

        throw new IllegalArgumentException("The HelpOption has not found.");
    }
}
