package com.epitrack.guardioes.view.game.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

/**
 * @author Igor Morais
 */
public class AnswerDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = AnswerDialog.class.getSimpleName();

    private static final int TIME = 15;
    private static final int DELAY = 1000;
    private static final float FACTOR = 15 / 1f;

    private TextView textViewTime;
    private MagicProgressCircle progressCircle;

    private final Handler handler = new Handler();

    private TimerHandler timer;

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

        textViewTime = (TextView) view.findViewById(R.id.text_view_time);

        progressCircle = (MagicProgressCircle) view.findViewById(R.id.progress_timer);

        view.findViewById(R.id.image_button_close).setOnClickListener(this);

        textViewTime.setText(String.valueOf(TIME));

        load();
    }

    @Override
    public void onPause() {
        super.onPause();

        handler.removeCallbacks(timer);
    }

    private void load() {

        timer = new TimerHandler(TIME);

        handler.postDelayed(timer, DELAY);
    }

    @Override
    public void onClick(final View view) {
        dismiss();
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
            }
        }
    }
}
