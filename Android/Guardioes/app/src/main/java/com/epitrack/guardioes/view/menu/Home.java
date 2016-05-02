package com.epitrack.guardioes.view.menu;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.MenuItem;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.HomeFragment;
import com.epitrack.guardioes.view.IMenu;
import com.epitrack.guardioes.view.menu.help.HelpFragment;

/**
 * @author Igor Morais
 */
public enum Home implements IMenu {

    HOME        (R.id.home, R.string.home, R.drawable.icon_home, HomeFragment.class),
    PROFILE     (R.id.profile, R.string.profile, R.drawable.icon_profile, null),
    ABOUT       (R.id.about, R.string.about, R.drawable.icon_about, AboutFragment.class),
    FACEBOOK    (R.id.facebook, R.string.facebook, R.drawable.icon_facebook, null),
    TWITTER     (R.id.twitter, R.string.twitter, R.drawable.icon_twitter, null),
    HELP        (R.id.help, R.string.help, R.drawable.icon_help, HelpFragment.class),
    EXIT        (R.id.exit, R.string.exit, R.drawable.icon_exit, null);

    private final int id;
    private final int name;
    private final int icon;
    private final Class<? extends Fragment> type;

    Home(final int id, final int name, final int icon, final Class<? extends Fragment> type) {
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

    @Override
    public final String getTag() {
        return type.getSimpleName();
    }

    @Override
    public final boolean isDialogFragment() {
        return DialogFragment.class.isAssignableFrom(type);
    }

    @Override
    public final boolean isFragment() {
        return Fragment.class.isAssignableFrom(type);
    }

    @Override
    public final boolean isActivity() {
        return Activity.class.isAssignableFrom(type);
    }

    @Override
    public final Class<? extends Fragment> getType() {
        return type;
    }

    public static Home getBy(final int id) {

        for (final Home menu : Home.values()) {

            if (menu.getId() == id) {
                return menu;
            }
        }

        throw new IllegalArgumentException("The Home has not found.");
    }

    public static Home getBy(final MenuItem item) {

        for (final Home home : Home.values()) {

            if (home.getId() == item.getItemId()) {
                return home;
            }
        }

        throw new IllegalArgumentException("The Home has not found.");
    }
}
