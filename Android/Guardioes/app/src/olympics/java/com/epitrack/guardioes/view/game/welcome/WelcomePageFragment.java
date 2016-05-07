package com.epitrack.guardioes.view.game.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class WelcomePageFragment extends Fragment {

    @Bind(R.id.text_view_message)
    TextView textViewMessage;

    @Bind(R.id.image_view)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.welcome_game_page, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        bind(view);

        final Welcome welcome = (Welcome) getArguments().getSerializable(Constants.Bundle.WELCOME);

        if (welcome == null) {
            throw new IllegalArgumentException("The welcome cannot be null.");
        }

        textViewMessage.setText(welcome.getMessage());
        imageView.setBackgroundResource(welcome.getImage());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    private void bind(final View view) {
        ButterKnife.bind(this, view);
    }
}
