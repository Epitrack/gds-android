package com.epitrack.guardioes.view.game.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.view.game.GameActivity;
import com.epitrack.guardioes.view.game.IAnswer;
import com.epitrack.guardioes.view.game.model.Option;
import com.epitrack.guardioes.view.game.model.Question;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import java.util.List;

/**
 * @author Igor Morais
 */
public class AnswerDialog extends DialogFragment implements View.OnClickListener{

    public static final String TAG = AnswerDialog.class.getSimpleName();

    public static final int ZERO = 0;

    public static final int OPTION_1 = 0;
    public static final int OPTION_2 = 1;
    public static final int OPTION_3 = 2;

    private static final int TIME = 15;
    private static final int DELAY = 1000;
    private static final float FACTOR = TIME / 1f;

    private TextView textViewQuestion;

    private TextView textViewAnswer1;
    private TextView textViewAnswer2;
    private TextView textViewAnswer3;

    private ImageButton buttonAnswer1;
    private ImageButton buttonAnswer2;
    private ImageButton buttonAnswer3;

    private ImageView imageViewAnswer;

    private TextView textViewEnergy;

    private TextView textViewTime;
    private MagicProgressCircle progressCircle;

    private final Handler handler = new Handler();

    private TimerHandler timer;

    private int amount;

    private int energy;

    private Question question;

    private IAnswer listener;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setStyle(STYLE_NORMAL, R.style.Theme_Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.dialog_answer, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {

        textViewQuestion = (TextView) view.findViewById(R.id.text_view_question);

        textViewAnswer1 = (TextView) view.findViewById(R.id.text_view_answer_1);
        textViewAnswer2 = (TextView) view.findViewById(R.id.text_view_answer_2);
        textViewAnswer3 = (TextView) view.findViewById(R.id.text_view_answer_3);

        buttonAnswer1 = (ImageButton) view.findViewById(R.id.button_answer_1);
        buttonAnswer2 = (ImageButton) view.findViewById(R.id.button_answer_2);
        buttonAnswer3 = (ImageButton) view.findViewById(R.id.button_answer_3);

        imageViewAnswer = (ImageView) view.findViewById(R.id.image_view_answer);

        textViewEnergy = (TextView) view.findViewById(R.id.text_view_energy);

        textViewTime = (TextView) view.findViewById(R.id.text_view_time);

        progressCircle = (MagicProgressCircle) view.findViewById(R.id.progress_timer);

        view.findViewById(R.id.image_button_close).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View element) {
                dismiss();
            }
        });

        load();

        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();

        handler.removeCallbacks(timer);
    }

    private void startTimer() {

        timer = new TimerHandler(TIME);

        handler.postDelayed(timer, DELAY);
    }

    @Override
    public void onClick(final View view) {

        if (energy == ZERO) {

            listener.onEnergyOver(this, energy);

        } else {

            final Option option = (Option) view.getTag();

            amount++;
            energy--;

            if (option.isCorrect()) {

                textViewEnergy.setText(getString(R.string.energy, energy));

                view.setBackgroundResource(R.drawable.button_answer_correct);
                imageViewAnswer.setImageResource(R.drawable.image_answer_green);

                handler.removeCallbacks(timer);

                listener.onCorrect(this, amount, energy);

            } else {

                textViewEnergy.setText(getString(R.string.energy, energy));

                view.setBackgroundResource(R.drawable.button_answer_incorrect);

                listener.onWrong(this, energy);
            }

            GameActivity.USER.setEnergy(energy);
        }
    }

    private void load() {

        textViewQuestion.setText(question.getTitle());

        final List<Option> optionList = question.getOptionList();

        textViewAnswer1.setText(optionList.get(OPTION_1).getOption());
        textViewAnswer2.setText(optionList.get(OPTION_2).getOption());
        textViewAnswer3.setText(optionList.get(OPTION_3).getOption());

        buttonAnswer1.setTag(optionList.get(OPTION_1));
        buttonAnswer1.setOnClickListener(this);

        buttonAnswer2.setTag(optionList.get(OPTION_2));
        buttonAnswer2.setOnClickListener(this);

        buttonAnswer3.setTag(optionList.get(OPTION_3));
        buttonAnswer3.setOnClickListener(this);

        textViewEnergy.setText(getString(R.string.energy, energy));

        textViewTime.setText(String.valueOf(TIME));
    }

    public AnswerDialog setEnergy(final int energy) {
        this.energy = energy;

        return this;
    }

    public AnswerDialog setQuestion(final Question question) {
        this.question = question;

        return this;
    }

    public AnswerDialog setListener(final IAnswer listener) {
        this.listener = listener;

        return this;
    }

    private class TimerHandler implements Runnable {

        private static final int MIM = 0;

        private int time;

        public TimerHandler(final int time) {
            this.time = time;
        }

        @Override
        public void run() {

            if (time > MIM) {

                time--;

                textViewTime.setText(String.valueOf(time));

                progressCircle.setSmoothPercent((TIME - time) / FACTOR, DELAY);

                handler.postDelayed(timer, DELAY);

            } else {

                listener.onTimeOver(AnswerDialog.this, energy--);
            }
        }
    }


}
