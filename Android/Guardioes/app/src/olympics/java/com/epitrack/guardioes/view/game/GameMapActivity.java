package com.epitrack.guardioes.view.game;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.model.Phase;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameMapActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = GameMapActivity.class.getSimpleName();

    private static final String ENERGY_FRAGMENT = "energy_fragment";

    @Bind(R.id.scroll_view)
    ScrollView scrollView;

    @Bind(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @Bind(R.id.image_view_map)
    ImageView imageView;

    private EnergyFragment energyFragment;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game_map);

        GameActivity.USER = new PrefManager(this).get(Constants.Pref.USER, User.class);

        load(GameActivity.USER.getLevel());

        getEnergyFragment().setEnergy(GameActivity.USER.getEnergy());
    }

    private void load(final int level) {

        final Drawable iconPhase = getResources().getDrawable(R.drawable.icon_phase);
        final Drawable iconPhaseCurrent = getResources().getDrawable(R.drawable.icon_phase_current);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int width = imageView.getWidth();
                final int height = imageView.getHeight();

                final ImageView imageView = new ImageView(GameMapActivity.this);

                for (final Phase phase : Phase.values()) {

                    if (phase.getId() == level) {

                        final int widthSize = iconPhaseCurrent.getIntrinsicWidth() / 2;
                        final int heightSize = iconPhaseCurrent.getIntrinsicHeight() - 25;

                        imageView.setX(phase.getX(width) - widthSize);
                        imageView.setY(phase.getY(height) - heightSize);

                        imageView.setImageDrawable(iconPhaseCurrent);
                        imageView.setTag(phase);
                        imageView.setOnClickListener(GameMapActivity.this);

                        relativeLayout.addView(imageView);

                        break;

                    } else {

                        final int widthSize = iconPhase.getIntrinsicWidth() / 2;
                        final int heightSize = iconPhase.getIntrinsicHeight() - 25;

                        imageView.setX(phase.getX(width) - widthSize);
                        imageView.setY(phase.getY(height) - heightSize);

                        imageView.setImageDrawable(iconPhase);
                        imageView.setTag(phase);
                        imageView.setOnClickListener(GameMapActivity.this);

                        relativeLayout.addView(imageView);
                    }
                }

                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onClick(final View view) {

        final Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.Bundle.PHASE, (Phase) view.getTag());

        navigateTo(GameActivity.class, bundle);
    }

    private EnergyFragment getEnergyFragment() {

        if (energyFragment == null) {
            energyFragment = (EnergyFragment) getFragmentManager().findFragmentByTag(ENERGY_FRAGMENT);
        }

        return energyFragment;
    }
}
