package com.epitrack.guardioes.view.game;

import com.epitrack.guardioes.view.game.dialog.AnswerDialog;

public interface IAnswer {

    void onTimeOver(AnswerDialog dialog, int energy);

    void onEnergyOver(AnswerDialog dialog, int energy);

    void onWrong(AnswerDialog dialog, int energy);

    void onCorrect(AnswerDialog dialog, int energy);
}
