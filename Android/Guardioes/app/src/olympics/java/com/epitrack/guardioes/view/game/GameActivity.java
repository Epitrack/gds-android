package com.epitrack.guardioes.view.game;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.model.Phase;
import com.epitrack.guardioes.view.game.model.Question;
import com.epitrack.guardioes.view.game.model.QuestionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Igor Morais
 */
public class GameActivity extends BaseAppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private static final int WAIT = 500;

    private final Handler handler = new Handler();

    private static final String ENERGY_FRAGMENT = "energy_fragment";
    private static final String GAME_FRAGMENT = "game_fragment";
    private static final String PLAY_FRAGMENT = "play_fragment";

    private EnergyFragment energyFragment;
    private GameFragment gameFragment;
    private PlayFragment playFragment;

    private Phase phase;
    private User user;

    private Map<Integer, Boolean> pieceMap;

    @Override
    protected void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game);

        loadPieceMap();

        getEnergyFragment().setEnergy(getUser().getEnergy());

        showGameFragment();
    }

    private EnergyFragment getEnergyFragment() {

        if (energyFragment == null) {
            energyFragment = (EnergyFragment) getFragmentManager().findFragmentByTag(ENERGY_FRAGMENT);
        }

        return energyFragment;
    }

    private GameFragment getGameFragment() {

        if (gameFragment == null) {
            gameFragment = (GameFragment) getFragmentManager().findFragmentByTag(GAME_FRAGMENT);
        }

        return gameFragment;
    }

    private PlayFragment getPlayFragment() {

        if (playFragment == null) {
            playFragment = (PlayFragment) getFragmentManager().findFragmentByTag(PLAY_FRAGMENT);
        }

        return playFragment;
    }

    public final Phase getPhase() {

        if (phase == null) {
            phase = (Phase) getIntent().getSerializableExtra(Constants.Bundle.PHASE);
        }

        return phase;
    }

    public User getUser() {

        if (user == null) {
            user = new PrefManager(this).get(Constants.Pref.USER, User.class);
        }

        return user;
    }

    public void showGameFragment()  {

        getFragmentManager().beginTransaction()
                            .hide(getPlayFragment())
                            .show(getGameFragment())
                            .show(getEnergyFragment())
                            .commit();
    }

    public void showPlayFragment()  {

        getFragmentManager().beginTransaction()
                            .hide(getGameFragment())
                            .hide(getEnergyFragment())
                            .show(getPlayFragment())
                            .commit();
    }

    public void setEnergy(final int energy) {
        getEnergyFragment().setEnergy(energy);
    }

    public void onReload() {

        final int position = getGameFragment().getPiecePosition();
        final Question question = getGameFragment().getQuestion();

        getGameFragment().loadAnswer(position, user.getEnergy(), pieceMap, question);
    }

    public void onGame() {

        showGameFragment();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                flip();
            }

        }, WAIT);
    }

    public void onPlay() {

        for (final Map.Entry<Integer, Boolean> entry : pieceMap.entrySet()) {

            if (!entry.getValue()) {

                QuestionHandler.with().requestQuestion(this, new IQuestion() {

                    @Override
                    public void onQuestion(final Question question) {

                        getGameFragment().loadAnswer(entry.getKey(), user.getEnergy(), pieceMap, question);
                    }
                });
            }
        }
    }

    public void onCorrect(final int amount, final int piece) {

        getPlayFragment().setAmount(amount);
        getPlayFragment().setPiece(getPhase().getPieceArray()[piece]);

        showPlayFragment();
    }

    private void flip() {

        final int position = getGameFragment().getPiecePosition();
        final View view = getGameFragment().getPieceView();

        final AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flip);

        animator.setTarget(view);

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(final Animator animation) {

            }

            @Override
            public void onAnimationEnd(final Animator animation) {

                ((ImageView) view).setImageResource(phase.getPieceArray()[position]);

                final AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(GameActivity.this, R.animator.alpha);

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

        pieceMap.put(position, true);
    }

    public final Map<Integer, Boolean> getPieceMap() {
        return pieceMap;
    }

    public final void setPieceMap(final Map<Integer, Boolean> pieceMap) {
        this.pieceMap = pieceMap;
    }

    private void loadPieceMap() {

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

        setPieceMap(pieceMap);
    }
}
