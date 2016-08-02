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
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.SymptomList;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.SurveyRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                        arrayAdapter.add("Afeganistão");
                        arrayAdapter.add("África do Sul");
                        arrayAdapter.add("Akrotiri");
                        arrayAdapter.add("Albânia");
                        arrayAdapter.add("Alemanha");
                        arrayAdapter.add("Andorra");
                        arrayAdapter.add("Angola");
                        arrayAdapter.add("Anguila");
                        arrayAdapter.add("Antárctida");
                        arrayAdapter.add("Antígua e Barbuda");
                        arrayAdapter.add("Antilhas Neerlandesas");
                        arrayAdapter.add("Arábia Saudita");
                        arrayAdapter.add("Arctic Ocean");
                        arrayAdapter.add("Argélia");
                        arrayAdapter.add("Argentina");
                        arrayAdapter.add("Arménia");
                        arrayAdapter.add("Aruba");
                        arrayAdapter.add("Ashmore and Cartier Islands");
                        arrayAdapter.add("Atlantic Ocean");
                        arrayAdapter.add("Austrália");
                        arrayAdapter.add("Áustria");
                        arrayAdapter.add("Azerbaijão");
                        arrayAdapter.add("Baamas");
                        arrayAdapter.add("Bangladeche");
                        arrayAdapter.add("Barbados");
                        arrayAdapter.add("Barém");
                        arrayAdapter.add("Bélgica");
                        arrayAdapter.add("Belize");
                        arrayAdapter.add("Benim");
                        arrayAdapter.add("Bermudas");
                        arrayAdapter.add("Bielorrússia");
                        arrayAdapter.add("Birmânia");
                        arrayAdapter.add("Bolívia");
                        arrayAdapter.add("Bósnia e Herzegovina");
                        arrayAdapter.add("Botsuana");
                        arrayAdapter.add("Brunei");
                        arrayAdapter.add("Bulgária");
                        arrayAdapter.add("Burquina Faso");
                        arrayAdapter.add("Burúndi");
                        arrayAdapter.add("Butão");
                        arrayAdapter.add("Cabo Verde");
                        arrayAdapter.add("Camarões");
                        arrayAdapter.add("Camboja");
                        arrayAdapter.add("Canadá");
                        arrayAdapter.add("Catar");
                        arrayAdapter.add("Cazaquistão");
                        arrayAdapter.add("Chade");
                        arrayAdapter.add("Chile");
                        arrayAdapter.add("China");
                        arrayAdapter.add("Chipre");
                        arrayAdapter.add("Clipperton Island");
                        arrayAdapter.add("Colômbia");
                        arrayAdapter.add("Comores");
                        arrayAdapter.add("Congo-Brazzaville");
                        arrayAdapter.add("Congo-Kinshasa");
                        arrayAdapter.add("Coral Sea Islands");
                        arrayAdapter.add("Coreia do Norte");
                        arrayAdapter.add("Coreia do Sul");
                        arrayAdapter.add("Costa do Marfim");
                        arrayAdapter.add("Costa Rica");
                        arrayAdapter.add("Croácia");
                        arrayAdapter.add("Cuba");
                        arrayAdapter.add("Dhekelia");
                        arrayAdapter.add("Dinamarca");
                        arrayAdapter.add("Domínica");
                        arrayAdapter.add("Egipto");
                        arrayAdapter.add("Emiratos Árabes Unidos");
                        arrayAdapter.add("Equador");
                        arrayAdapter.add("Eritreia");
                        arrayAdapter.add("Eslováquia");
                        arrayAdapter.add("Eslovénia");
                        arrayAdapter.add("Espanha");
                        arrayAdapter.add("Estados Unidos");
                        arrayAdapter.add("Estónia");
                        arrayAdapter.add("Etiópia");
                        arrayAdapter.add("Faroé");
                        arrayAdapter.add("Fiji");
                        arrayAdapter.add("Filipinas");
                        arrayAdapter.add("Finlândia");
                        arrayAdapter.add("França");
                        arrayAdapter.add("Gabão");
                        arrayAdapter.add("Gâmbia");
                        arrayAdapter.add("Gana");
                        arrayAdapter.add("Gaza Strip");
                        arrayAdapter.add("Geórgia");
                        arrayAdapter.add("Geórgia do Sul e Sandwich do Sul");
                        arrayAdapter.add("Gibraltar");
                        arrayAdapter.add("Granada");
                        arrayAdapter.add("Grécia");
                        arrayAdapter.add("Gronelândia");
                        arrayAdapter.add("Guame");
                        arrayAdapter.add("Guatemala");
                        arrayAdapter.add("Guernsey");
                        arrayAdapter.add("Guiana");
                        arrayAdapter.add("Guiné");
                        arrayAdapter.add("Guiné Equatorial");
                        arrayAdapter.add("Guiné-Bissau");
                        arrayAdapter.add("Haiti");
                        arrayAdapter.add("Honduras");
                        arrayAdapter.add("Hong Kong");
                        arrayAdapter.add("Hungria");
                        arrayAdapter.add("Iémen");
                        arrayAdapter.add("Ilha Bouvet");
                        arrayAdapter.add("Ilha do Natal");
                        arrayAdapter.add("Ilha Norfolk");
                        arrayAdapter.add("Ilhas Caimão");
                        arrayAdapter.add("Ilhas Cook");
                        arrayAdapter.add("Ilhas dos Cocos");
                        arrayAdapter.add("Ilhas Falkland");
                        arrayAdapter.add("Ilhas Heard e McDonald");
                        arrayAdapter.add("Ilhas Marshall");
                        arrayAdapter.add("Ilhas Salomão");
                        arrayAdapter.add("Ilhas Turcas e Caicos");
                        arrayAdapter.add("Ilhas Virgens Americanas");
                        arrayAdapter.add("Ilhas Virgens Britânicas");
                        arrayAdapter.add("Índia");
                        arrayAdapter.add("Indian Ocean");
                        arrayAdapter.add("Indonésia");
                        arrayAdapter.add("Irão");
                        arrayAdapter.add("Iraque");
                        arrayAdapter.add("Irlanda");
                        arrayAdapter.add("Islândia");
                        arrayAdapter.add("Israel");
                        arrayAdapter.add("Itália");
                        arrayAdapter.add("Jamaica");
                        arrayAdapter.add("Jan Mayen");
                        arrayAdapter.add("Japão");
                        arrayAdapter.add("Jersey");
                        arrayAdapter.add("Jibuti");
                        arrayAdapter.add("Jordânia");
                        arrayAdapter.add("Kuwait");
                        arrayAdapter.add("Laos");
                        arrayAdapter.add("Lesoto");
                        arrayAdapter.add("Letónia");
                        arrayAdapter.add("Líbano");
                        arrayAdapter.add("Libéria");
                        arrayAdapter.add("Líbia");
                        arrayAdapter.add("Listenstaine");
                        arrayAdapter.add("Lituânia");
                        arrayAdapter.add("Luxemburgo");
                        arrayAdapter.add("Macau");
                        arrayAdapter.add("Macedónia");
                        arrayAdapter.add("Madagáscar");
                        arrayAdapter.add("Malásia");
                        arrayAdapter.add("Malávi");
                        arrayAdapter.add("Maldivas");
                        arrayAdapter.add("Mali");
                        arrayAdapter.add("Malta");
                        arrayAdapter.add("Man, Isle of");
                        arrayAdapter.add("Marianas do Norte");
                        arrayAdapter.add("Marrocos");
                        arrayAdapter.add("Maurícia");
                        arrayAdapter.add("Mauritânia");
                        arrayAdapter.add("Mayotte");
                        arrayAdapter.add("México");
                        arrayAdapter.add("Micronésia");
                        arrayAdapter.add("Moçambique");
                        arrayAdapter.add("Moldávia");
                        arrayAdapter.add("Mónaco");
                        arrayAdapter.add("Mongólia");
                        arrayAdapter.add("Monserrate");
                        arrayAdapter.add("Montenegro");
                        arrayAdapter.add("Mundo");
                        arrayAdapter.add("Namíbia");
                        arrayAdapter.add("Nauru");
                        arrayAdapter.add("Navassa Island");
                        arrayAdapter.add("Nepal");
                        arrayAdapter.add("Nicarágua");
                        arrayAdapter.add("Níger");
                        arrayAdapter.add("Nigéria");
                        arrayAdapter.add("Niue");
                        arrayAdapter.add("Noruega");
                        arrayAdapter.add("Nova Caledónia");
                        arrayAdapter.add("Nova Zelândia");
                        arrayAdapter.add("Omã");
                        arrayAdapter.add("Pacific Ocean");
                        arrayAdapter.add("Países Baixos");
                        arrayAdapter.add("Palau");
                        arrayAdapter.add("Panamá");
                        arrayAdapter.add("Papua-Nova Guiné");
                        arrayAdapter.add("Paquistão");
                        arrayAdapter.add("Paracel Islands");
                        arrayAdapter.add("Paraguai");
                        arrayAdapter.add("Peru");
                        arrayAdapter.add("Pitcairn");
                        arrayAdapter.add("Polinésia Francesa");
                        arrayAdapter.add("Polónia");
                        arrayAdapter.add("Porto Rico");
                        arrayAdapter.add("Portugal");
                        arrayAdapter.add("Quénia");
                        arrayAdapter.add("Quirguizistão");
                        arrayAdapter.add("Quiribáti");
                        arrayAdapter.add("Reino Unido");
                        arrayAdapter.add("República Centro-Africana");
                        arrayAdapter.add("República Checa");
                        arrayAdapter.add("República Dominicana");
                        arrayAdapter.add("Roménia");
                        arrayAdapter.add("Ruanda");
                        arrayAdapter.add("Rússia");
                        arrayAdapter.add("Salvador");
                        arrayAdapter.add("Samoa");
                        arrayAdapter.add("Samoa Americana");
                        arrayAdapter.add("Santa Helena");
                        arrayAdapter.add("Santa Lúcia");
                        arrayAdapter.add("São Cristóvão e Neves");
                        arrayAdapter.add("São Marinho");
                        arrayAdapter.add("São Pedro e Miquelon");
                        arrayAdapter.add("São Tomé e Príncipe");
                        arrayAdapter.add("São Vicente e Granadinas");
                        arrayAdapter.add("Sara Ocidental");
                        arrayAdapter.add("Seicheles");
                        arrayAdapter.add("Senegal");
                        arrayAdapter.add("Serra Leoa");
                        arrayAdapter.add("Sérvia");
                        arrayAdapter.add("Singapura");
                        arrayAdapter.add("Síria");
                        arrayAdapter.add("Somália");
                        arrayAdapter.add("Southern Ocean");
                        arrayAdapter.add("Spratly Islands");
                        arrayAdapter.add("Sri Lanca");
                        arrayAdapter.add("Suazilândia");
                        arrayAdapter.add("Sudão");
                        arrayAdapter.add("Suécia");
                        arrayAdapter.add("Suíça");
                        arrayAdapter.add("Suriname");
                        arrayAdapter.add("Svalbard e Jan Mayen");
                        arrayAdapter.add("Tailândia");
                        arrayAdapter.add("Taiwan");
                        arrayAdapter.add("Tajiquistão");
                        arrayAdapter.add("Tanzânia");
                        arrayAdapter.add("Território Britânico do Oceano Índico");
                        arrayAdapter.add("Territórios Austrais Franceses");
                        arrayAdapter.add("Timor Leste");
                        arrayAdapter.add("Togo");
                        arrayAdapter.add("Tokelau");
                        arrayAdapter.add("Tonga");
                        arrayAdapter.add("Trindade e Tobago");
                        arrayAdapter.add("Tunísia");
                        arrayAdapter.add("Turquemenistão");
                        arrayAdapter.add("Turquia");
                        arrayAdapter.add("Tuvalu");
                        arrayAdapter.add("Ucrânia");
                        arrayAdapter.add("Uganda");
                        arrayAdapter.add("União Europeia");
                        arrayAdapter.add("Uruguai");
                        arrayAdapter.add("Usbequistão");
                        arrayAdapter.add("Vanuatu");
                        arrayAdapter.add("Vaticano");
                        arrayAdapter.add("Venezuela");
                        arrayAdapter.add("Vietname");
                        arrayAdapter.add("Wake Island");
                        arrayAdapter.add("Wallis e Futuna");
                        arrayAdapter.add("West Bank");
                        arrayAdapter.add("Zâmbia");
                        arrayAdapter.add("Zimbabué");

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
                                        String strName = arrayAdapter.getItem(which);
                                        country = strName;
                                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                                SymptomActivity.this);
                                        builderInner.setMessage(obrigado);
                                        builderInner.setTitle(R.string.app_name);
                                        builderInner.setPositiveButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
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

        for (int i = 0; i < symptomArray.size(); i++) {
            String symptomName = symptomArray.get(i).getCodigo();

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
}
