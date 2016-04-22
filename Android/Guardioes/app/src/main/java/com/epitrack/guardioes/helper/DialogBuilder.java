package com.epitrack.guardioes.helper;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public final class DialogBuilder extends BaseBuilder {

    public DialogBuilder(final Context context) {
        super(context);
    }

    public MaterialDialog.Builder load() {

        return new MaterialDialog.Builder(getContext())
                                 .titleColorRes(R.color.primary)
                                 .contentColorRes(R.color.primary)
                                 .negativeColorRes(R.color.primary)
                                 .positiveColorRes(R.color.primary);
    }
}
