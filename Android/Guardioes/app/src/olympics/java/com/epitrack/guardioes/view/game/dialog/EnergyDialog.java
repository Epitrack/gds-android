package com.epitrack.guardioes.view.game.dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.survey.SelectParticipantActivity;

/**
 * @author Igor Morais
 */
public class EnergyDialog extends DialogFragment {

    public static final String TAG = EnergyDialog.class.getSimpleName();

    TextView textViewEnergy;

    private int energy;

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

        textViewEnergy = (TextView) view.findViewById(R.id.text_view_energy);

        textViewEnergy.setText(getString(R.string.energy, energy));

        view.findViewById(R.id.image_button_join).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View element) {

                final Intent intent = new Intent();

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);

                getActivity().startActivity(new Intent(getActivity(), SelectParticipantActivity.class));
            }
        });

        view.findViewById(R.id.image_button_close).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View element) {
                dismiss();
            }
        });
    }

    public EnergyDialog setEnergy(final int energy) {
        this.energy = energy;

        return this;
    }
}
