package com.epitrack.guardioes.view.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class AboutFragment extends BaseFragment {

    @Bind(R.id.message_about_content_01)
    TextView textViewAbout;

    @Bind(R.id.txt_version_build)
    TextView textViewVersionBuild;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.about, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle bundle) {

        bind(view);

        textViewVersionBuild.setText("Versão " + SingleUser.getInstance().getVersionBuild());
    }

    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);

        getSupportActionBar().setTitle(R.string.about);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("About Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
