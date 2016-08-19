package com.epitrack.guardioes.view.survey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.SocialShare;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.Country;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.SymptomList;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.SurveyRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.epitrack.guardioes.view.game.GameActivity;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class SymptomActivity extends BaseAppCompatActivity {

    private boolean isExantematica = false;
    private boolean isTravelLocation = false;

    private String country = "";

    @Bind(R.id.list_view)
    ListView listView;

    private List<SymptomList> symptomArray = new ArrayList<>();
    private String id;
    private double latitude;
    private double longitude;
    final LoadDialog loadDialog = new LoadDialog();
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        id = getIntent().getStringExtra("id_user");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        setContentView(R.layout.symptom);

        final View footerView = LayoutInflater.from(this).inflate(R.layout.symptom_footer, null);
        final String obrigado = getString(R.string.obrigado);
        footerView.findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                boolean isSelected = false;

                for (int i = 0; i < symptomArray.size(); i++) {
                    if (symptomArray.get(i).isSelected()) {
                        isSelected = true;
                    }
                }

                if (!isSelected) {
                    new DialogBuilder(SymptomActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.message_register_no_symptom)
                            .positiveText(R.string.ok)
                            .callback(new MaterialDialog.ButtonCallback() {

                            }).show();
                } else {

                    for (int i = 0; i < symptomArray.size(); i++) {
                        if (symptomArray.get(i).isSelected()) {
                            String symptomName = symptomArray.get(i).getCodigo();

                            if (symptomName.equals("hadTravelledAbroad")) {
                                isTravelLocation = true;
                                break;
                            }
                        }
                    }

                    if (isTravelLocation) {
                        isTravelLocation = false;

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SymptomActivity.this);

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                SymptomActivity.this,
                                R.layout.select_dialog_singlechoice_gds);
                        final ArrayList<Country> listCountry = new ArrayList<Country>();
                        String[] locales = Locale.getISOCountries();
                        for (String countryCode : locales) {
                            Locale obj = new Locale("", countryCode);
                            arrayAdapter.add(obj.getDisplayCountry(Locale.getDefault()));
                            listCountry.add(new Country(obj.getCountry().hashCode(),obj.getCountry(),obj.getDisplayCountry(Locale.getDefault()),obj.getDisplayCountry(Locale.ENGLISH)));
                        }

                        builderSingle.setNegativeButton(
                                R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builderSingle.setAdapter(
                                arrayAdapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String strName = listCountry.get(which).getName_en();
                                        country = strName;
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                                SymptomActivity.this);
                                        builderInner.setMessage(obrigado);
                                        builderInner.setTitle(R.string.app_name);
                                        builderInner.setPositiveButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick( DialogInterface dialog, int which) {
                                                        confirmSendSymptons();
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builderInner.show();
                                    }
                                });
                        builderSingle.show();
                    } else {
                        confirmSendSymptons();
                    }
                }
            }
        });

        listView.addFooterView(footerView);
        final String contato = this.getString(R.string.symptom_contato);
        final String procurei = this.getString(R.string.symptom_procureiservicosaude);
        final String estivefora = this.getString(R.string.symptom_estivefora);

        final String febre = this.getString(R.string.symptom_febre);
        final String manchas_vermelhas = this.getString(R.string.symptom_manchas);
        final String dor_no_corpo = this.getString(R.string.symptom_dorcorpo);
        final String dor_nas_juntas = this.getString(R.string.symptom_dorjuntas);
        final String dor_de_cabeca = this.getString(R.string.symptom_dorcabeca);
        final String coceira = this.getString(R.string.symptom_coceira);
        final String olhos_vermelhos = this.getString(R.string.symptom_olhosvermelhos);
        final String dor_de_garganta = this.getString(R.string.symptom_dorgarganta);
        final String tosse = this.getString(R.string.symptom_tosse);
        final String falta_de_ar = this.getString(R.string.symptom_faltaar);
        final String nausea_vomito = this.getString(R.string.symptom_nauseas);
        final String diarreia = this.getString(R.string.symptom_diarreia);
        final String sangramento = this.getString(R.string.symptom_sangramento);

        new SurveyRequester(this).getSymptom(new RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onSuccess(final String result) {

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    if (!jsonObject.getBoolean("error")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectSymptoms = jsonArray.getJSONObject(i);
                                Log.d("Code",jsonObjectSymptoms.get("code").toString()+" ; "+jsonObjectSymptoms.get("name").toString());
                                String symptom_translated="";
                                switch(jsonObjectSymptoms.get("code").toString()){
                                    case "febre":
                                        symptom_translated=febre;
                                        break;
                                    case "manchas-vermelhas":
                                        symptom_translated=manchas_vermelhas;
                                        break;
                                    case "dor-no-corpo":
                                        symptom_translated=dor_no_corpo;
                                        break;
                                    case "dor-nas-juntas":
                                        symptom_translated=dor_nas_juntas;
                                        break;
                                    case "dor-de-cabeca":
                                        symptom_translated=dor_de_cabeca;
                                        break;
                                    case "coceira":
                                        symptom_translated=coceira;
                                        break;
                                    case "olhos-vermelhos":
                                        symptom_translated=olhos_vermelhos;
                                        break;
                                    case "dor-de-garganta":
                                        symptom_translated=dor_de_garganta;
                                        break;
                                    case "tosse":
                                        symptom_translated=tosse;
                                        break;
                                    case "falta-de-ar":
                                        symptom_translated=falta_de_ar;
                                        break;
                                    case "nausea-vomito":
                                        symptom_translated=nausea_vomito;
                                        break;
                                    case "diarreia":
                                        symptom_translated=diarreia;
                                        break;
                                    case "sangramento":
                                        symptom_translated=sangramento;
                                        break;
                                }
                                /*
                                * febre
                                * manchas-vermelhas
                                * dor-no-corpo
                                * dor-nas-juntas
                                * dor-de-cabeca
                                * coceira
                                * olhos-vermelhos
                                * dor-de-garganta
                                * tosse
                                * falta-de-ar
                                * nausea-vomito
                                * diarreia
                                * sangramento
                                * */
                                SymptomList symptomList = new SymptomList(jsonObjectSymptoms.get("code").toString(), symptom_translated);
                                symptomArray.add(symptomList);
                            }
                        }
                        symptomArray.add(new SymptomList("hadContagiousContact", contato));
                        symptomArray.add(new SymptomList("hadHealthCare", procurei));
                        symptomArray.add(new SymptomList("hadTravelledAbroad", estivefora));

                        listView.setAdapter(new SymptomAdapter(SymptomActivity.this, symptomArray));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Symptom Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }


    private void sendSymptom() throws JSONException, ExecutionException, InterruptedException {
        loadDialog.show(getFragmentManager(), LoadDialog.TAG);
        final Map<String, Object> map = new HashMap<>();

        final User user = new User();

        user.setId(id);

        SingleUser singleUser = SingleUser.getInstance();

        map.put("user_id", singleUser.getId());

        if (!(user.getId().equals(singleUser.getId()))) {
            map.put("household_id", user.getId());
        }
        map.put("lat", latitude);
        map.put("lon", longitude);
        map.put("app_token", user.getAppToken());
        map.put("platform", user.getPlatform());
        map.put("client", user.getClient());
        map.put("no_symptom", "N");
        map.put("token", singleUser.getUserToken());
        map.put("travelLocation", country);
        map.put("deviceModel",android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")");
        //
        Log.d("lat", ""+latitude);
        Log.d("lon", ""+longitude);
        Log.d("household_id", user.getId());
        Log.d("TRAVEL_LOCATION",country);
        for (int i = 0; i < symptomArray.size(); i++) {
            String symptomName = symptomArray.get(i).getCodigo();
            Log.d("symptomArray("+i+")",symptomName);
            if (symptomArray.get(i).isSelected()) {
                if(symptomName.equals("hadContagiousContact") || symptomName.equals("hadHealthCare") || symptomName.equals("hadTravelledAbroad")) {
                    map.put(symptomArray.get(i).getCodigo(), "true");
                } else {
                    map.put(symptomArray.get(i).getCodigo(), "Y");
                }
            }
        }

        new SurveyRequester(this).sendSurvey(map, new RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {
                loadDialog.dismiss();
            }

            @Override
            public void onSuccess(final String result) {
                hasSurveyToday();
                loadDialog.dismiss();
                try {

                    final JSONObject jsonObject = new JSONObject(result);
                    Log.d("sendSurvey",jsonObject.toString());
                    if (jsonObject.get("error").toString() == "true") {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        if (jsonObject.get("exantematica").toString() == "true") {
                            isExantematica = true;
                        }
                    }
                    Log.d("ISEXANTEMATICA",""+isExantematica);
                    if (isExantematica) {
                        Log.d("confirmSendSymptons","navigateTo(ZikaActivity.class)");
                        navigateTo(ZikaActivity.class);
                    } else {
                        Log.d("confirmSendSymptons","navigateTo(ShareActivity.class, bundle)");
                        final Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.Bundle.BAD_STATE, true);
                        navigateTo(ShareActivity.class, bundle);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (SocialShare.getInstance().isShared()) {
            new DialogBuilder(SymptomActivity.this).load()
                    .title(R.string.app_name)
                    .content(R.string.share_ok)
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            SocialShare.getInstance().setIsShared(false);
                            navigateTo(HomeActivity.class);
                        }
                    }).show();
        }
    }

    private void confirmSendSymptons() {
        new DialogBuilder(SymptomActivity.this).load()
                .title(R.string.attention)
                .content(R.string.message_register_info)
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(final MaterialDialog dialog) {
                    try {
                            sendSymptom();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
    }

    private void hasSurveyToday() {

        new SurveyRequester(this).hasSurveyToday(new RequestListener<Integer>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onSuccess(final Integer amount) {
                Log.d("hasSurveyToday",""+ amount);
                if (amount == 0) {

                    final User user = new PrefManager(SymptomActivity.this).get(Constants.Pref.USER, User.class);

                    if (user != null) {

                        user.addEnergy(10);

                        new PrefManager(SymptomActivity.this).put(Constants.Pref.USER, user);
                    }
                    Log.d("hasSurveyToday.user",""+ user.toString());
                }
            }
        });
    }
}
