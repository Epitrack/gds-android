package com.epitrack.guardioes.view.menu.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.SocialShare;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Miqueias Lopes
 */
public class Report extends BaseAppCompatActivity {

    @Bind(R.id.txt_report_subject)
    EditText txtSubject;

    @Bind(R.id.report_message)
    EditText txtMessage;

    @Bind(R.id.button_send_email)
    Button buttonSendEmail;

    private Tracker mTracker;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.report);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Report Problem Screen - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnClick(R.id.button_send_email)
    public void sendEmail() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Send Report Button")
                .build());

        boolean isError = false;

        if (txtSubject.getText().toString().trim().equals("")) {
            isError = true;
        } else if (txtMessage.getText().toString().trim().equals("")) {
            isError = true;
        }

        if (isError) {
            new DialogBuilder(Report.this).load()
                    .title(R.string.attention)
                    .content(R.string.email_null)
                    .positiveText(R.string.ok)
                    .show();
        } else {

            try {
                JSONObject jsonPost = new JSONObject();
                jsonPost.put("title", txtSubject.getText().toString().trim());
                jsonPost.put("text", txtMessage.getText().toString().trim());
                jsonPost.put("email", SingleUser.getInstance().getEmail());

                SimpleRequester simpleRequester = new SimpleRequester();
                simpleRequester.setMethod(Method.POST);
                simpleRequester.setJsonObject(jsonPost);

                simpleRequester.setUrl(Requester.API_URL + "email/log");

                String jsonStr = simpleRequester.execute(simpleRequester).get();

                JSONObject jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString().equals("false")) {
                    isError = false;
                } else {
                    isError = true;
                }

            } catch (JSONException e) {
                isError = true;
            } catch (InterruptedException e) {
                isError = true;
            } catch (ExecutionException e) {
                isError = true;
            } finally {
                if (isError) {
                    new DialogBuilder(Report.this).load()
                            .title(R.string.attention)
                            .content(R.string.email_not_send)
                            .positiveText(R.string.ok)
                            .show();
                } else {
                    new DialogBuilder(Report.this).load()
                            .title(R.string.app_name)
                            .content(R.string.email_send)
                            .positiveText(R.string.ok)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(final MaterialDialog dialog) {
                                    onBackPressed();
                                }
                            })
                            .show();

                    txtSubject.setText("");
                    txtMessage.setText("");
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

}
