package com.epitrack.guardioes.view.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class GameActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.text_view_level)
    TextView textViewLevel;

    @Bind(R.id.grid_view)
    GridView gridView;

    @Override
    protected void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.game);

        gridView.setAdapter(new AnswerAdapter(this));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

    }
}
