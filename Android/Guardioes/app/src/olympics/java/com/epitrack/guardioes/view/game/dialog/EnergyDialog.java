package com.epitrack.guardioes.view.game.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public class EnergyDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = EnergyDialog.class.getSimpleName();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setStyle(STYLE_NORMAL, R.style.Theme_Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.dialog_energy, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {


        view.findViewById(R.id.image_button_close).setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        dismiss();
    }
}
