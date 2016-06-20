package com.epitrack.guardioes.view.game;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.request.GameRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.game.dialog.AnswerDialog;
import com.epitrack.guardioes.view.game.dialog.EnergyDialog;
import com.epitrack.guardioes.view.game.model.Phase;
import com.epitrack.guardioes.view.game.model.Question;
import com.epitrack.guardioes.view.game.model.QuestionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;

public class GameFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.text_view_level)
    TextView textViewLevel;

    @Bind(R.id.grid_view)
    GridView gridView;

    private int position;

    private final Map<Integer, Boolean> pieceMap = getPieceMap();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.game_fragment, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        bind(view);

        final Phase phase = ((GameActivity) getActivity()).getPhase();

        textViewLevel.setText(getString(R.string.level, phase.getId()));

        gridView.setAdapter(new PieceAdapter(getActivity(), phase.getPieceArray()));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

        final int energy = ((GameActivity) getActivity()).getUser().getEnergy();

        if (energy > 0) {

            if (!pieceMap.get(position)) {

                QuestionHandler.with().requestQuestion(getActivity(), new IQuestion() {

                    @Override
                    public void onQuestion(final Question question) {

                        new AnswerDialog().setEnergy(energy).setQuestion(question).setListener(new IAnswer() {

                            @Override
                            public void onTimeOver(final AnswerDialog dialog, final int energy) {

                                dialog.dismiss();
                            }

                            @Override
                            public void onEnergyOver(final AnswerDialog dialog, final int energy) {

                                dialog.dismiss();

                                new EnergyDialog().setEnergy(energy).show(getFragmentManager(), EnergyDialog.TAG);
                            }

                            @Override
                            public void onWrong(final AnswerDialog dialog, final int energy) {

                            }

                            @Override
                            public void onCorrect(final AnswerDialog dialog, final int energy) {

                                final Phase phase = ((GameActivity) getActivity()).getPhase();

                                update(question.getId(), energy, phase.getId(), pieceMap);
                            }

                        }).show(getFragmentManager(), AnswerDialog.TAG);
                    }
                });

            } else {

                new EnergyDialog().setEnergy(energy).show(getFragmentManager(), EnergyDialog.TAG);
            }
        }

        setPosition(position);
    }

    private void flip(final int position, final View view) {

        final AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.flip);

        animator.setTarget(view);

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(final Animator animation) {

            }

            @Override
            public void onAnimationEnd(final Animator animation) {

                final Phase phase = ((GameActivity) getActivity()).getPhase();

                ((ImageView) view).setImageResource(phase.getPieceArray()[position]);

                final AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.alpha);

                animator.setTarget(view);

                animator.start();
            }

            @Override
            public void onAnimationCancel(final Animator animation) {

            }

            @Override
            public void onAnimationRepeat(final Animator animation) {

            }
        });

        animator.start();
    }

    private Map<Integer, Boolean> getPieceMap() {

        final Map<Integer, Boolean> pieceMap = new LinkedHashMap<>(9);

        pieceMap.put(0, false);
        pieceMap.put(1, false);
        pieceMap.put(2, false);
        pieceMap.put(3, false);
        pieceMap.put(4, false);
        pieceMap.put(5, false);
        pieceMap.put(6, false);
        pieceMap.put(7, false);
        pieceMap.put(8, false);

        return pieceMap;
    }

    private void update(final String id, final int energy, final int level, final Map<Integer, Boolean> pieceMap) {

        new GameRequester(getActivity()).update(id, energy, level, pieceMap, new RequestListener<Boolean>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {


                //pieceMap.put(position, true);

                //flip(position, view);

            }

            @Override
            public void onSuccess(final Boolean result) {

                ((GameActivity) getActivity()).onCorrect(position);
            }
        });
    }

    private void setPosition(final int position) {
        this.position = position;
    }
}
