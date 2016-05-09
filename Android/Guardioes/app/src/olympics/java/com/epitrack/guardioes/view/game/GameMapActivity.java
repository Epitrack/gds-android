package com.epitrack.guardioes.view.game;

import android.os.Bundle;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameMapActivity extends BaseAppCompatActivity {

    @Bind(R.id.image_view_map)
    SubsamplingScaleImageView imageViewMap;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game_map);

        imageViewMap.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
    }
}
