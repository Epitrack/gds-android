package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.GameRequester;
import com.epitrack.guardioes.request.base.RequestHandler;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.game.dialog.ScoreDialog;
import com.epitrack.guardioes.view.game.dialog.TrophyDialog;
import com.epitrack.guardioes.view.game.model.Phase;
import com.epitrack.guardioes.view.game.model.Score;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class EnergyFragment extends BaseFragment {

    @Bind(R.id.text_view_energy)
    TextView textViewEnergy;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.energy, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        bind(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("USER.getEnergy",""+GameActivity.USER.getEnergy());
        setEnergy(GameActivity.USER.getEnergy());
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

        final List<Phase> phaseList = new ArrayList<>();

        final int level = new PrefManager(getActivity()).get(Constants.Pref.USER, User.class).getLevel();

        for (final Phase phase : Phase.values()) {

            if (phase.getId() >= level) {
                break;
            }

            phaseList.add(phase);
        }

        new TrophyDialog().setPhaseList(phaseList).show(getFragmentManager(), TrophyDialog.TAG);
    }

    public void setEnergy(final int energy) {
        textViewEnergy.setText(getString(R.string.energy, energy));
    }
}
