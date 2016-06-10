package com.epitrack.guardioes.view.game;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.game.model.Phase;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameMapActivity extends BaseAppCompatActivity {

    @Bind(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @Bind(R.id.image_view_map)
    ImageView imageView;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game_map);



        load();
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

                    imageView.setImageDrawable(drawable);

                    relativeLayout.addView(imageView);
                }

                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
