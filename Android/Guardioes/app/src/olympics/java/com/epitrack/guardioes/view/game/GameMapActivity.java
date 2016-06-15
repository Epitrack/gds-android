package com.epitrack.guardioes.view.game;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.GameRequester;
import com.epitrack.guardioes.request.base.RequestHandler;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.dialog.EnergyDialog;
import com.epitrack.guardioes.view.game.model.Phase;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameMapActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String ENERGY_FRAGMENT = "energy_fragment";

    @Bind(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @Bind(R.id.image_view_map)
    ImageView imageView;

    private EnergyFragment energyFragment;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game_map);

        final User user = new PrefManager(this).get(Constants.Pref.USER, User.class);

        new GameRequester(this).update(0, 0, null, user, new UserHandler(this));

        load();

        getEnergyFragment().setEnergy(10);
    }

    private void load() {

        final Drawable drawable = getResources().getDrawable(R.drawable.icon_phase);

        final int widthSize = drawable.getIntrinsicWidth() / 2;
        final int heightSize = drawable.getIntrinsicHeight() - 25;

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int width = imageView.getWidth();
                final int height = imageView.getHeight();

                for (final Phase phase : Phase.values()) {

                    final ImageView imageView = new ImageView(GameMapActivity.this);

                    imageView.setX(phase.getX(width) - widthSize);
                    imageView.setY(phase.getY(height) - heightSize);

                    imageView.setTag(phase);
                    imageView.setImageDrawable(drawable);
                    imageView.setOnClickListener(GameMapActivity.this);

                    relativeLayout.addView(imageView);
                }

                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private class UserHandler extends RequestHandler<User> {

        public UserHandler(final Context context) {
            super(context);
        }

        @Override
        public void onSuccess(User type) {
            super.onSuccess(type);
        }
    }

    @Override
    public void onClick(final View view) {

        if (false) {

            new EnergyDialog().show(getFragmentManager(), EnergyDialog.TAG);

        } else {

            final Bundle bundle = new Bundle();

            bundle.putSerializable(Constants.Bundle.PHASE, (Phase) view.getTag());

            navigateTo(GameActivity.class, bundle);
        }
    }

    private EnergyFragment getEnergyFragment() {

        if (energyFragment == null) {
            energyFragment = (EnergyFragment) getFragmentManager().findFragmentByTag(ENERGY_FRAGMENT);
        }

        return energyFragment;
    }
}
