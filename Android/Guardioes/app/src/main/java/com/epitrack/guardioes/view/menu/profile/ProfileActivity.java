package com.epitrack.guardioes.view.menu.profile;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.NetworkUtility;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.menu.help.Report;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class ProfileActivity extends BaseAppCompatActivity implements UserListener {

    @Bind(R.id.list_view)
    ListView listView;

    SingleUser singleUser = SingleUser.getInstance();
    private final Map<String, Fragment> fragmentMap = new HashMap<>();

    private Tracker mTracker;

    public static ArrayList<User> userArrayList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.profile_activity);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        //progressDialog = new ProgressDialog(ProfileActivity.this, R.style.Theme_MyProgressDialog);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
        //progressDialog.setTitle(R.string.app_name);
        //progressDialog.setMessage("Carregando...");
        //progressDialog.show();

        //upadteAdapter();
        /*if (userArrayList == null) {
            userArrayList = loadProfiles();
        }

        listView.setAdapter(new UserAdapter(this, userArrayList, this));*/

    }

    private void back() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        progressDialog = new ProgressDialog(ProfileActivity.this, R.style.Theme_MyProgressDialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        upadteAdapter();

        super.onResume();
        mTracker.setScreenName("List Profile Screen - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void upadteAdapter() {

        final ProfileActivity me = this;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                userArrayList = loadProfiles();
                listView.setAdapter(new UserAdapter(ProfileActivity.this, userArrayList, me));
                progressDialog.dismiss();
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            //navigateTo(ProfileFragment.class);
        } else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    @OnClick(R.id.button_add)
    public void onAdd() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Add New Member Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
        bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
        navigateTo(UserActivity.class, bundle);
    }

    @Override
    public void onEdit(final User user) {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Edit Profile Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
        bundle.putString("nick", user.getNick());
        bundle.putString("dob", user.getDob());
        bundle.putString("gender", user.getGender());
        bundle.putString("race", user.getRace());
        bundle.putString("email", user.getEmail());
        bundle.putString("password", user.getPassword());
        bundle.putString("id", user.getId());
        bundle.putString("picture", user.getPicture());
        bundle.putString("relationship", user.getRelationship());
        bundle.putString("file", user.getFile());

        if (user.getPicture().equals("0")) {
            if (user.getGender().equals("F")) {
                switch (user.getRace()) {
                    case "branco":
                        bundle.putString("picture", "8");
                        break;
                    case "preto":
                        bundle.putString("picture", "1");
                        break;
                    case "pardo":
                        bundle.putString("picture", "2");
                        break;
                    case "amarelo":
                        bundle.putString("picture", "7");
                        break;
                    case "indigena":
                        bundle.putString("picture", "8");
                        break;
                }
            } else {
                switch (user.getGender()) {
                    case "branco":
                        bundle.putString("picture", "11");
                        break;
                    case "preto":
                        bundle.putString("picture", "5");
                        break;
                    case "pardo":
                        bundle.putString("picture", "4");
                        break;
                    case "amarelo":
                        bundle.putString("picture", "10");
                        break;
                    case "indigena":
                        bundle.putString("picture", "4");
                        break;
                }
            }
        } else {
            bundle.putString("picture", user.getPicture());
        }


        // TODO: Check if is main member..
        if (singleUser.getId() == user.getId()) {
            bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);
        }
        navigateTo(UserActivity.class, bundle);
    }

    @Override
    public void onDelete(final User user) {
        //Miquéias Lopes

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Delete Member Button")
                .build());

        if (singleUser.getId() == user.getId()) {

            new DialogBuilder(ProfileActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.not_remove_member)
                    .positiveText(R.string.ok)
                    .show();
        } else {

            new DialogBuilder(ProfileActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.delete_profile)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onNegative(final MaterialDialog dialog) {

                        }

                        @Override
                        public void onPositive(final MaterialDialog dialog) {

                            SimpleRequester simpleRequester = new SimpleRequester();
                            simpleRequester.setMethod(Method.GET);
                            simpleRequester.setUrl(Requester.API_URL + "household/delete/" + user.getId() + "?client=api");
                            simpleRequester.setJsonObject(null);

                            try {
                                String jsonStr = simpleRequester.execute(simpleRequester).get();

                                JSONObject jsonObject = new JSONObject(jsonStr);

                                if (jsonObject.get("error").toString() == "true") {
                                    refresh(true);
                                } else {
                                    refresh(false);
                                }

                            } catch (InterruptedException e) {
                                Toast.makeText(getApplicationContext(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (ExecutionException e) {
                                Toast.makeText(getApplicationContext(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }).show();
        }
    }

    private void refresh(boolean error) {
        if (error) {
            Toast.makeText(getApplicationContext(), R.string.generic_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.delete_user, Toast.LENGTH_SHORT).show();
            listView.setAdapter(new UserAdapter(this, new ArrayList<User>(), this));
        }
    }

    private ArrayList<User> loadProfiles() {
        ArrayList<User> userList = new ArrayList<User>();

        SingleUser singleUser = SingleUser.getInstance();

        userList.add(new User(R.drawable.image_avatar_small_2, singleUser.getNick(), singleUser.getEmail(), singleUser.getId(),
                singleUser.getDob(), singleUser.getRace(), singleUser.getGender(), singleUser.getPicture(), "", singleUser.getFile()));

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setUrl(Requester.API_URL + "user/household/" + singleUser.getId());
        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString().equals("false")) {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (jsonArray.length() > 0) {

                    JSONObject jsonObjectHousehold;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObjectHousehold = jsonArray.getJSONObject(i);

                        User user = new User(R.drawable.image_avatar_small_8, jsonObjectHousehold.get("nick").toString(),
                                "", jsonObjectHousehold.get("id").toString(),
                                jsonObjectHousehold.get("dob").toString(), jsonObjectHousehold.get("race").toString(),
                                jsonObjectHousehold.get("gender").toString(), jsonObjectHousehold.get("picture").toString());
                        try {
                            user.setRelationship(jsonObjectHousehold.get("relationship").toString());
                        } catch (Exception e) {
                            user.setRelationship("");
                        }

                        try {
                            user.setEmail(jsonObjectHousehold.get("email").toString());
                        } catch (Exception e) {
                            user.setEmail("");
                        }

                        userList.add(user);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userList;
    }

}
