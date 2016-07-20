package com.epitrack.guardioes.view.menu.help;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Miqueias Lopes
 */
public class ReportActivity extends BaseAppCompatActivity {

    @Bind(R.id.txt_report_subject)
    EditText txtSubject;

    @Bind(R.id.report_message)
    EditText txtMessage;
    private final SingleUser singleUser = SingleUser.getInstance();
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.report);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("ReportActivity Problem Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnClick(R.id.button_send_email)
    public void sendEmail() {
        String info ="";
        try{
            info="\n\nInfos:";
            info += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
            info += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
            info += "\n Device: " + android.os.Build.DEVICE;
            info += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        }catch(Exception e){
        }
        //Toast.makeText(ReportActivity.this, Locale.getDefault().toString(), Toast.LENGTH_SHORT).show();
       getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Send ReportActivity Button")
                .build());

        boolean isError = txtSubject.getText().toString().trim().isEmpty() || txtMessage.getText().toString().trim().isEmpty();

        if (isError) {

            new DialogBuilder(ReportActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.email_null)
                    .positiveText(R.string.ok)
                    .show();
        } else {

            try {

                JSONObject jsonPost = new JSONObject();
                jsonPost.put("email", singleUser.getEmail());
                jsonPost.put("title", txtSubject.getText().toString().trim());
                jsonPost.put("text", txtMessage.getText().toString().trim()+" ; "+info);

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
                    new DialogBuilder(ReportActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.email_not_send)
                            .positiveText(R.string.ok)
                            .show();
                } else {
                    new DialogBuilder(ReportActivity.this).load()
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
