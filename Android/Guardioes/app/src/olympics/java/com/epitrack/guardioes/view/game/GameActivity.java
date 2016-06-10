package com.epitrack.guardioes.view.game;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.model.Phase;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String ENERGY_FRAGMENT = "energy_fragment";

    @Bind(R.id.text_view_level)
    TextView textViewLevel;

    @Bind(R.id.grid_view)
    GridView gridView;

    private Phase phase;

    private EnergyFragment energyFragment;

    @Override
    protected void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game);

        phase = (Phase) getIntent().getSerializableExtra(Constants.Bundle.PHASE);

        textViewLevel.setText(getString(R.string.level, phase.getId()));

        gridView.setAdapter(new PieceAdapter(this, phase.getPieceArray()));
        gridView.setOnItemClickListener(this);

        getEnergyFragment().setEnergy(10);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

        flip(position, view);

        //new AnswerDialog().show(getFragmentManager(), AnswerDialog.TAG);
    }

    private void flip(final int position, final View view) {

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
    }

    private EnergyFragment getEnergyFragment() {

        if (energyFragment == null) {
            energyFragment = (EnergyFragment) getFragmentManager().findFragmentByTag(ENERGY_FRAGMENT);
        }

        return energyFragment;
    }
}
