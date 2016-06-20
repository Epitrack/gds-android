package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class PlayFragment extends BaseFragment {

    @Bind(R.id.image_view_piece)
    ImageView imageViewPiece;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.play, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        bind(view);
    }

    @OnClick(R.id.button_reload)
    public void onReload() {

    }

    @OnClick(R.id.button_game)
    public void onGame() {

    }

    @OnClick(R.id.button_play)
    public void onPlay() {

    }

    public void setPiece(final int piece) {
        imageViewPiece.setImageResource(piece);
    }
}
