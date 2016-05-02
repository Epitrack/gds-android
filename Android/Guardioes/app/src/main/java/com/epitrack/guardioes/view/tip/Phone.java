package com.epitrack.guardioes.view.tip;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;

/**
 * @author Igor Morais
 */
public enum Phone implements IMenu {

    SUS             (1, R.string.sus, ""),
    EMERGENCY       (2, R.string.emergency, ""),
    POLICE          (3, R.string.police, ""),
    FIREMAN         (4, R.string.fireman, ""),
    DEFENSE         (5, R.string.defense, "");


    private final int id;
    private final int name;
    private final String number;

    Phone(final int id, final int name, final String number) {
        this.id = id;
        this.name = name;
        this.number = number;
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
    public int getIcon() {
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


    public final String getNumber() {
        return number;
    }

    public static Phone getBy(final long id) {

        for (final Phone phone : Phone.values()) {

            if (phone.getId() == id) {
                return phone;
            }
        }

        throw new IllegalArgumentException("The Phone has not found.");
    }
}
