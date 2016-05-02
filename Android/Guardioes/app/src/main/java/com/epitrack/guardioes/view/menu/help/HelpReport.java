package com.epitrack.guardioes.view.menu.help;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;

/**
 * @author Miquéias Lopes on 04/11/15.
 */
public enum HelpReport implements IMenu {

    REPORT      (1, R.string.report, R.drawable.icon_report);

    private final int id;
    private final int name;
    private final int icon;

    HelpReport(final int id, final int name, final int icon) {
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

    public static HelpReport getBy(final int id) {

        for (final HelpReport help : HelpReport.values()) {

            if (help.getId() == id) {
                return help;
            }
        }

        throw new IllegalArgumentException("The HelpOption has not found.");
    }
}
