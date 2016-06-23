package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.request.GameRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.epitrack.guardioes.view.game.dialog.AnswerDialog;
import com.epitrack.guardioes.view.game.dialog.EnergyDialog;
import com.epitrack.guardioes.view.game.model.Phase;
import com.epitrack.guardioes.view.game.model.Question;
import com.epitrack.guardioes.view.game.model.QuestionHandler;

import java.util.Map;

import butterknife.Bind;

public class GameFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.text_view_level)
    TextView textViewLevel;

    @Bind(R.id.grid_view)
    GridView gridView;

    private int piecePosition;
    private View pieceView;

    private Question question;

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

            final Map<Integer, Boolean> pieceMap = ((GameActivity) getActivity()).getPieceMap();

            if (!pieceMap.get(position)) {

                QuestionHandler.with().requestQuestion(getActivity(), new IQuestion() {

                    @Override
                    public void onQuestion(final Question question) {
                        loadAnswer(position, energy, pieceMap, question);
                    }
                });
            }

        } else {

            new EnergyDialog().setEnergy(energy).show(getFragmentManager(), EnergyDialog.TAG);
        }

        setPiecePosition(position);
        setPieceView(view);
    }

    public void loadAnswer(final int position, final int energy, final Map<Integer, Boolean> pieceMap, final Question question) {

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
            public void onCorrect(final AnswerDialog dialog, final int amount, final int energy) {

                final Phase phase = ((GameActivity) getActivity()).getPhase();

                new GameRequester(getActivity()).update(question.getId(), energy, phase.getId(), pieceMap, new RequestListener<Boolean>() {

                    private final LoadDialog loadDialog = new LoadDialog();

                    @Override
                    public void onStart() {
                        loadDialog.show(getFragmentManager(), LoadDialog.TAG);
                    }

                    @Override
                    public void onError(final Exception e) {

                    }

                    @Override
                    public void onSuccess(final Boolean result) {
                        loadDialog.dismiss();

                        dialog.dismiss();

                        ((GameActivity) getActivity()).onCorrect(amount, position);
                    }
                });
            }

        }).show(getFragmentManager(), AnswerDialog.TAG);

        setPiecePosition(position);
        setPieceView(gridView.getChildAt(position));

        setQuestion(question);
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public void setPiecePosition(final int piecePosition) {
        this.piecePosition = piecePosition;
    }

    public final View getPieceView() {
        return pieceView;
    }

    public void setPieceView(final View pieceView) {
        this.pieceView = pieceView;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(final Question question) {
        this.question = question;
    }
}
