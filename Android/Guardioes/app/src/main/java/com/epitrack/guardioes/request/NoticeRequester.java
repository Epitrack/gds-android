package com.epitrack.guardioes.request;

import android.content.Context;

import com.epitrack.guardioes.model.Notice;
import com.epitrack.guardioes.request.base.BaseRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.base.Requester;
import com.epitrack.guardioes.request.base.RequesterConfig;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.Logger;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Igor Morais
 */
public final class NoticeRequester extends BaseRequester {

    private static final String TAG = NoticeRequester.class.getSimpleName();

    private static final String GET = "/get";

    public NoticeRequester(final Context context) {
        super(context);
    }

    public void getAll(final RequestListener<List<Notice>> listener) {

        final String url = RequesterConfig.URL + RequesterConfig.Api.NOTICE + GET;

        listener.onStart();

        new Requester(getContext()).request(Method.GET, url, getAuthHeaderMap(), new FutureCallback<Response<String>>() {

            @Override
            public void onCompleted(final Exception error, final Response<String> response) {

                if (error == null) {

                    try {

                        final List<Notice> noticeList = new ArrayList<>();

                        final JSONArray jsonArray = new JSONObject(response.getResult()).getJSONObject("data")
                                .getJSONArray("statuses");

                        final SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
                        final SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        final SimpleDateFormat format3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

                                format1.setLenient(true);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            final JSONObject json = jsonArray.getJSONObject(i);

                            final Notice notice = new Notice();

                            notice.setTitle(json.get("text").toString());
                            notice.setLink("https://twitter.com/minsaude/status/" + json.get("id_str").toString());
                            notice.setLike(json.getString("favorite_count"));

                            final Date date = format1.parse(json.get("created_at").toString());

                            notice.setPublicationDate(format2.format(date));

                            final int hour = DateFormat.getDateDiff(format3.format(date), 0);

                            notice.setClock(hour + "h");

                            noticeList.add(notice);
                        }

                        listener.onSuccess(noticeList);

                    } catch (final Exception e) {
                        Logger.logError(TAG, e.getMessage());

                        listener.onError(e);
                    }

                } else {

                    listener.onError(error);
                }
            }
        });
    }
}
