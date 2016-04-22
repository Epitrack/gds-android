package com.epitrack.guardioes.view.menu.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.NetworkUtility;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class HelpFragment extends BaseFragment {

    @Bind(R.id.list_view_option)
    ListView listViewOption;

    @Bind(R.id.list_view_report_error)
    ListView listViewReport;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.help, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle bundle) {

        bind(view);

        listViewOption.setAdapter(new HelpAdapter(getActivity(), HelpOption.values()));

        listViewOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (HelpOption.getBy(position + 1).getId() == HelpOption.TERM.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Terms Button")
                            .build());

                    navigateTo(Term.class);

                } else if (HelpOption.getBy(position + 1).getId() == HelpOption.TUTORIAL.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Tutorial Button")
                            .build());

                    navigateTo(TutorialActivity.class);
                }
            }
        });

        listViewReport.setAdapter(new HelpAdapter(getActivity(), HelpReport.values()));

        listViewReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (HelpReport.getBy(position + 1).getId() == HelpReport.REPORT.getId()) {

                    getTracker().send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Help Contact ReportActivity Button")
                            .build());

                    if (NetworkUtility.isOnline(getActivity().getApplication())) {

                        navigateTo(ReportActivity.class);

                    } else {

                        new DialogBuilder(getActivity()).load()
                                .title(R.string.attention)
                                .content(R.string.network_fail)
                                .positiveText(R.string.ok)
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);

        getSupportActionBar().setTitle(R.string.help);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Help Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
