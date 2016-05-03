package com.epitrack.guardioes.view.tip;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.MapPointActivity;

/**
 * @author Igor Morais
 */
public enum Tip implements IMenu {

    ZIKA            (1, R.string.zika, R.drawable.icon_zika, ZikaTipActivity.class),
    HOSPITAL        (2, R.string.hospital, R.drawable.icon_hospital, MapPointActivity.class),
    VACCINE         (3, R.string.vaccine, R.drawable.icon_vaccine, VaccineActivity.class),
    TELEPHONE       (4, R.string.phone, R.drawable.icon_phone, null),
    PHARMACY        (5, R.string.pharmacy, R.drawable.icon_pharmacy, MapPointActivity.class),
    CARE            (6, R.string.care, R.drawable.icon_care, CareActivity.class),
    PREVENTION      (7, R.string.prevention, R.drawable.icon_prevention, PreventionActivity.class);

    private final int id;
    private final int name;
    private final int icon;
    private final Class<?> type;

    Tip(final int id, final int name, final int icon, final Class<?> type) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
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

    public final String getTag() {
        return type.getSimpleName();
    }

    @Override
    public boolean isDialogFragment() {
        return false;
    }

    public final boolean isDialog() {

        if (type == null) {
            return false;
        }

        return DialogFragment.class.isAssignableFrom(type);
    }

    public final boolean isFragment() {

        if (type == null) {
            return false;
        }

        return Fragment.class.isAssignableFrom(type);
    }

    public final boolean isActivity() {

        if (type == null) {
            return false;
        }

        return Activity.class.isAssignableFrom(type);
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    public static Tip getBy(final long id) {

        for (final Tip tip : Tip.values()) {

            if (tip.getId() == id) {
                return tip;
            }
        }

        throw new IllegalArgumentException("The Tip has not found.");
    }
}
