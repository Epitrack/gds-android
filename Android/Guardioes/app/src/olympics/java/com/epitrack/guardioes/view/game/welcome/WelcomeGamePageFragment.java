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
public class WelcomeGamePageFragment extends Fragment {

    @Bind(R.id.text_view)
    TextView textViewMessage;

    @Bind(R.id.image_view_map)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.welcome_game_page, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        bind(view);

        final WelcomeGame welcomeGame = (WelcomeGame) getArguments().getSerializable(Constants.Bundle.WELCOME_GAME);

        if (welcomeGame == null) {
            throw new IllegalArgumentException("The welcomeGame cannot be null.");
        }

        textViewMessage.setText(welcomeGame.getMessage());
        imageView.setBackgroundResource(welcomeGame.getImage());
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
