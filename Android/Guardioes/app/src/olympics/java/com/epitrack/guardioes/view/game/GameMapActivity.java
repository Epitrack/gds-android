package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameMapActivity extends BaseAppCompatActivity {

    @Bind(R.id.relative_layout)
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game_map);

           /* findViewById(R.id.image_view_map).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {
                    navigateTo(GameActivity.class);
                }
            });

            relativeLayout.addView(imageView);*/
        //}
    }
}
