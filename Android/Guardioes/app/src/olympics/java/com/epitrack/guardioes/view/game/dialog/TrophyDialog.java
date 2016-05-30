package com.epitrack.guardioes.view.game.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.game.TrophyAdapter;

import java.util.ArrayList;

/**
 * @author Igor Morais
 */
public class TrophyDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = TrophyDialog.class.getSimpleName();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setStyle(STYLE_NORMAL, R.style.Theme_Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.dialog_trophy, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        final ListView listView = (ListView) view.findViewById(R.id.list_view);

        listView.setAdapter(new TrophyAdapter(getActivity(), new ArrayList<String>(10)));

        view.findViewById(R.id.image_button_close).setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        dismiss();
    }
}
