package com.epitrack.guardioes.view.account;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.utility.Logger;
import com.epitrack.guardioes.view.base.DialogListener;

/**
 * @author Igor Morais
 */
public class NotifyDialog extends DialogFragment implements OnClickListener {

    public static final String TAG = NotifyDialog.class.getSimpleName();

    private static final String ACTION_NEGATIVE = "action_negative";
    private static final String ACTION_NEUTRAL = "action_neutral";
    private static final String ACTION_POSITIVE = "action_positive";

    private int requestCode;

    private DialogListener listener;

    public NotifyDialog() {

    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setStyle(STYLE_NORMAL, R.style.Theme_NotifyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final View view = inflater.inflate(getLayout(), viewGroup, false);

        findView(view);

        return view;
    }

    public void findView(final View view) {

        View actionView = view.findViewWithTag(ACTION_NEGATIVE);

        if (actionView == null) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The actionView with tag " + ACTION_NEGATIVE + " is null.");
            }

        } else {
            actionView.setOnClickListener(this);
        }

        actionView = view.findViewWithTag(ACTION_NEUTRAL);

        if (actionView == null) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The actionView with tag " + ACTION_NEUTRAL + " is null.");
            }

        } else {
            actionView.setOnClickListener(this);
        }

        actionView = view.findViewWithTag(ACTION_NEGATIVE);

        if (actionView == null) {

            if (BuildConfig.DEBUG) {
                Logger.logDebug(TAG, "The actionView with tag " + ACTION_NEGATIVE + " is null.");
            }

        } else {
            actionView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View view) {

        if (ACTION_POSITIVE.equals(view.getTag())) {
            listener.onActionPositive(this, requestCode);

        } else if (ACTION_NEGATIVE.equals(view.getTag())) {
            listener.onActionPositive(this, requestCode);

        } else if (ACTION_NEUTRAL.equals(view.getTag())) {
            listener.onActionPositive(this, requestCode);
        }
    }

    public final int getRequestCode() {
        return requestCode;
    }

    public final void setRequestCode(final int requestCode) {
        this.requestCode = requestCode;
    }

    public final DialogListener getListener() {
        return listener;
    }

    public final void setListener(final DialogListener listener) {
        this.listener = listener;
    }

    public int getLayout() {
        return R.layout.privacy;
    }
}
