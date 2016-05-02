package com.epitrack.guardioes.view;

/**
 * @author Igor Morais
 */
public interface IMenu {

    int getId();

    int getName();

    int getIcon();

    String getTag();

    boolean isDialogFragment();

    boolean isFragment();

    boolean isActivity();

    Class<?> getType();
}
