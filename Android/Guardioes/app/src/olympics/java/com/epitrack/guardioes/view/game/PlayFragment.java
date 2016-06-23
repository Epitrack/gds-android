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

    private static final int LOW = 1;
    private static final int MID = 2;
    private static final int HIGH = 3;

    @Bind(R.id.image_view_piece)
    ImageView imageViewPiece;

    @Bind(R.id.image_star_center)
    ImageView imageViewStarCenter;

    @Bind(R.id.image_star_right)
    ImageView imageViewStarRight;

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

        ((GameActivity) getActivity()).onReload();
    }

    @OnClick(R.id.button_game)
    public void onGame() {

        ((GameActivity) getActivity()).onGame();
    }

    @OnClick(R.id.button_play)
    public void onPlay() {

        ((GameActivity) getActivity()).onPlay();
    }

    public void setAmount(final int amount) {

        if (amount == LOW) {

            imageViewStarCenter.setImageResource(R.drawable.image_star_center);
            imageViewStarRight.setImageResource(R.drawable.image_star_right);

        } else if (amount == MID) {

            imageViewStarCenter.setImageResource(R.drawable.image_star_center);

        } else if (amount == HIGH) {

            imageViewStarCenter.setImageResource(R.drawable.image_star_white_center);
            imageViewStarRight.setImageResource(R.drawable.image_star_white_right);
        }
    }

    public void setPiece(final int piece) {
        imageViewPiece.setImageResource(piece);
    }
}
