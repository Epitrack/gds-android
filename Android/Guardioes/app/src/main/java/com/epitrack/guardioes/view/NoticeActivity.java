package com.epitrack.guardioes.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Notice;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class NoticeActivity extends AppCompatActivity implements NoticeListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private Tracker mTracker;
    public static List<Notice> noticeList;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.notice);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        ButterKnife.bind(this);

/*        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.notice);*/

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NoticeAdapter(this, noticeList));
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Notice Screen - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.notice, menu);

        return true;
    }

    public void onPrivacy(final MenuItem item) {

    }

    private void setupHeader(final Notice notice) {

        // TODO: Stub only
        //imageView.setImageResource(R.drawable.img_news);
    }

    @Override
    public void onNoticeSelect(final Notice notice) {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Show Notice")
                .build());

        new DialogBuilder(NoticeActivity.this).load()
                .title(R.string.attention)
                .content(R.string.open_link)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onNegative(final MaterialDialog dialog) {

                    }

                    @Override
                    public void onPositive(final MaterialDialog dialog) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notice.getLink())));
                    }
                }).show();
    }
}
