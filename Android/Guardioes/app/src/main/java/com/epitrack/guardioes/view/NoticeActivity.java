package com.epitrack.guardioes.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Notice;
import com.epitrack.guardioes.request.NoticeRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import java.util.List;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class NoticeActivity extends BaseAppCompatActivity implements NoticeListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.notice);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new NoticeRequester(this).getAll(new NoticeHandler());
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Notice Screen - " + getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.notice, menu);

        return true;
    }

    private class NoticeHandler implements RequestListener<List<Notice>> {

        @Override
        public void onStart() {

        }

        @Override
        public void onError(final Exception e) {

        }

        @Override
        public void onSuccess(final List<Notice> noticeList) {
            recyclerView.setAdapter(new NoticeAdapter(NoticeActivity.this, noticeList));
        }
    }

    @Override
    public void onNoticeSelect(final Notice notice) {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Show Notice")
                .build());

        new DialogBuilder(NoticeActivity.this).load()
                .title(R.string.attention)
                .content(R.string.open_link)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notice.getLink())));
                    }

                }).show();
    }
}
