package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.request.GameRequester;
import com.epitrack.guardioes.request.base.RequestHandler;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.game.dialog.EnergyDialog;
import com.epitrack.guardioes.view.game.dialog.ScoreDialog;
import com.epitrack.guardioes.view.game.model.Score;

import java.util.List;

import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class EnergyFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.energy, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        bind(view);
    }

    @OnClick(R.id.button_medal)
    public void onMedal() {

        new GameRequester(getActivity()).getScore(new RequestHandler<List<Score>>(getActivity()) {

            @Override
            public void onSuccess(final List<Score> scoreList) {
                super.onSuccess(scoreList);

                new ScoreDialog().setScoreList(scoreList)
                        .show(getFragmentManager(), ScoreDialog.TAG);
            }
        });
    }

    @OnClick(R.id.button_trophy)
    public void onTrophy() {

        new EnergyDialog().show(getFragmentManager(), EnergyDialog.TAG);
    }
}