package com.epitrack.guardioes.view;

import android.Manifest;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.Loader;
import com.epitrack.guardioes.model.Point;
import com.epitrack.guardioes.model.SingleDTO;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.Extension;
import com.epitrack.guardioes.utility.LocationUtility;
import com.epitrack.guardioes.view.base.AbstractBaseMapActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class MapSymptomActivity extends AbstractBaseMapActivity implements SearchView.OnQueryTextListener {

    @Bind(R.id.text_view_city)
    TextView textViewCity;

    @Bind(R.id.text_view_state)
    TextView textViewState;

    @Bind(R.id.text_view_participation)
    TextView textViewParticipation;

    @Bind(R.id.text_view_good_percentage)
    TextView textViewGoodPercentage;

    @Bind(R.id.text_view_good_report)
    TextView textViewGoodReport;

    @Bind(R.id.text_view_bad_percentage)
    TextView textViewBadPercentage;

    @Bind(R.id.text_view_bad_report)
    TextView textViewBadReport;

    @Bind(R.id.text_view_percentage_1)
    TextView textViewPercentage1;

    @Bind(R.id.progress_bar_1)
    ProgressBar progressBar1;

    @Bind(R.id.text_view_percentage_2)
    TextView textViewPercentage2;

    @Bind(R.id.progress_bar_2)
    ProgressBar progressBar2;

    @Bind(R.id.text_view_percentage_3)
    TextView textViewPercentage3;

    @Bind(R.id.progress_bar_3)
    ProgressBar progressBar3;

    @Bind(R.id.sliding_panel)
    SlidingUpPanelLayout slidingPanel;

    @Bind(R.id.pie_chart_diary)
    PieChart pieChart;

    @Bind(R.id.syndromes)
    TextView txtSyndromes;

    @Bind(R.id.button_expand)
    Button buttonExpand;

    private MarkerOptions badMarkerOption;
    private MarkerOptions goodMarkerOption;
    private LocationUtility locationUtility;
    private SearchView searchVieww;
    private String queryText;
    SingleDTO singleDTO = SingleDTO.getInstance();
    private static final long DEFAULT_ZOOM = 12;

    private Tracker mTracker;
    private View mLayout;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int REQUEST_LOCATION = 0;


    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.map_symptom);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        locationUtility = new LocationUtility(getApplicationContext());

        if (locationUtility.getLocation() == null) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Snackbar.make(mLayout, R.string.permission_location_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MapSymptomActivity.this,
                                        PERMISSIONS_LOCATION,
                                        REQUEST_LOCATION);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
            }

        } else {

            final MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.fragment_map);

            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                //Snackbar.make(mLayout, R.string.permision_available_location,
                //        Snackbar.LENGTH_SHORT).show();
                new DialogBuilder(MapSymptomActivity.this).load()
                        .title(R.string.attention)
                        .content(R.string.network_disable)
                        .positiveText(R.string.ok)
                        .callback(new MaterialDialog.ButtonCallback() {

                            @Override
                            public void onPositive(final MaterialDialog dialog) {
                                navigateTo(HomeActivity.class);
                            }

                        }).show();
            } else {
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setLocationBySearch() {

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);
        simpleRequester.setOtherAPI(true);
        simpleRequester.setUrl("https://maps.googleapis.com/maps/api/geocode/json?address=" + singleDTO.getDto() + "&key=AIzaSyDRoA88MUJbF8TFPnaUXHvIrQzGPU5JC94");

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject geocodeJson = new JSONObject(jsonStr);

            if (geocodeJson.get("status").toString().toUpperCase().equals("OK")) {
                JSONArray jsonArray = geocodeJson.getJSONArray("results");

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                JSONObject jsonObjectGeometry = jsonObject.getJSONObject("geometry");
                JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");

                LatLng latLng = new LatLng(jsonObjectLocation.getDouble("lat"), jsonObjectLocation.getDouble("lng"));
                singleDTO.setLatLng(latLng);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);

        MenuItem searchItem = menu.findItem(R.id.search_survey);
        searchVieww = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);

        return true;
    }

    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            searchVieww.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            searchVieww.setSearchableInfo(info);
        }

        searchVieww.setOnQueryTextListener(this);
    }

    public void onShare(final MenuItem menuItem) {

        new BottomSheet.Builder(this).sheet(R.menu.share)
                                     .grid()
                                     .show();
    }

    @OnClick(R.id.syndromes)
    public void onSyndromes() {

        new DialogBuilder(MapSymptomActivity.this).load()
                .title(R.string.syndromes)
                .content(R.string.syndromes_desc)
                .positiveText(R.string.ok)
                .show();
    }

    @OnClick(R.id.button_expand)
    public void onExpand() {

        if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            buttonExpand.setBackground(this.getResources().getDrawable(R.drawable.fab_close));
        } else {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            buttonExpand.setBackground(this.getResources().getDrawable(R.drawable.fab_open));
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        super.onMapReady(map);
        if (locationUtility.getLocation() != null) {
            load();
            setupView();
        }
    }

    private void load() {

        Loader.with().getHandler().post(new Runnable() {

            @Override
            public void run() {

                try {

                    final List<Point> pointList = new ArrayList<Point>();

                    SimpleRequester simpleRequester = new SimpleRequester();
                    simpleRequester.setJsonObject(null);
                    simpleRequester.setMethod(Method.GET);
                    if (singleDTO.getDto() != null) {
                        if (!singleDTO.getDto().equals("")) {
                            simpleRequester.setUrl(Requester.API_URL + "surveys/l?q=" + singleDTO.getDto());
                            singleDTO.setDto(null);
                        } else {
                            simpleRequester.setUrl(Requester.API_URL + "surveys/l?lon=" + locationUtility.getLongitude() + "&lat=" + locationUtility.getLatitude());
                        }
                    } else {
                        simpleRequester.setUrl(Requester.API_URL + "surveys/l?lon=" + locationUtility.getLongitude() + "&lat=" + locationUtility.getLatitude());
                    }

                    String jsonStr = simpleRequester.execute(simpleRequester).get();

                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if (jsonObject.get("error").toString() == "true") {
                        Toast.makeText(getApplicationContext(), "Erro: " + jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Point point = new Point();

                            if (jsonArray.getJSONObject(i).get("no_symptom").equals("Y")) {
                                point.setSympton(false);
                            } else {
                                point.setSympton(true);
                            }

                            point.setLongitude(Double.parseDouble(jsonArray.getJSONObject(i).get("lon").toString()));
                            point.setLatitude(Double.parseDouble(jsonArray.getJSONObject(i).get("lat").toString()));

                            pointList.add(point);
                        }
                    }

                    if (pointList.size() > 0) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                addMarker(pointList);
                            }

                        }, 2000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addMarker(final List<Point> pointList) {

        final Handler handler = new Handler(Looper.getMainLooper());

        for (int i = 0; i < pointList.size(); i++) {

            final int count = i;

            final Point point = pointList.get(i);

            handler.post(new Runnable() {

                @Override
                public void run() {

                    final LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());

                    if (!point.isSympton()) {
                        getMap().addMarker(loadGoodMarkerOption().position(latLng));

                    } else {
                        getMap().addMarker(loadBadMarkerOption().position(latLng));
                    }
                }
            });
        }
    }

    private void setupView() {

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);
        if (singleDTO.getDto() != null) {
            if (!singleDTO.getDto().equals("")) {
                simpleRequester.setUrl(Requester.API_URL + "surveys/summary/?q=" + singleDTO.getDto());
            } else {
                simpleRequester.setUrl(Requester.API_URL + "surveys/summary/?lon=" + locationUtility.getLongitude() + "&lat=" + locationUtility.getLatitude());
            }
        } else {
            if (singleDTO.getLatLng() != null) {
                simpleRequester.setUrl(Requester.API_URL + "surveys/summary/?lon=" + singleDTO.getLatLng().longitude + "&lat=" + singleDTO.getLatLng().latitude);
            } else {
                simpleRequester.setUrl(Requester.API_URL + "surveys/summary/?lon=" + locationUtility.getLongitude() + "&lat=" + locationUtility.getLatitude());
            }
        }

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "true") {
                Toast.makeText(getApplicationContext(), "Erro: " + jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                JSONObject jsonObjectLocation = jsonObjectData.getJSONObject("location");

                try {
                    textViewCity.setText(jsonObjectLocation.get("city").toString());
                } catch (Exception e) {
                    try {
                        String formattedAddress = jsonObjectLocation.get("formattedAddress").toString();
                        String formattedAddressParts[] = formattedAddress.split(",");
                        String cityUf = formattedAddressParts[2];
                        String cityUfParts[] = cityUf.split("-");
                        textViewCity.setText(cityUfParts[0].trim());
                    } catch (Exception e1) {
                        textViewCity.setText("");
                    }
                }

                textViewState.setText(getStateDescription(jsonObjectLocation.get("state").toString().toUpperCase()));
                textViewParticipation.setText(jsonObjectData.get("total_surveys").toString() + " Participações essa semana.");

                double totalNoSympton = Double.parseDouble(jsonObjectData.get("total_no_symptoms").toString());
                double goodPercent = 0;

                if (totalNoSympton > 0) {
                    goodPercent = (totalNoSympton / Double.parseDouble(jsonObjectData.get("total_surveys").toString()));
                }

                String htmlStringGood = "<b>" + (int) (goodPercent * 100) + "%</b> Bem";
                textViewGoodPercentage.setText(Html.fromHtml(htmlStringGood));
                //textViewGoodPercentage.setText((int)(goodPercent * 100) + "% Bem");
                textViewGoodReport.setText(jsonObjectData.get("total_no_symptoms").toString() + " Relatórios");

                double totalSympton = Double.parseDouble(jsonObjectData.get("total_symptoms").toString());
                double badPercent = 0;

                if (totalNoSympton > 0) {
                    badPercent = (totalSympton / Double.parseDouble(jsonObjectData.get("total_surveys").toString()));
                }

                String htmlStringBad = "<b>" + (int) (badPercent * 100) + "%</b> Mal";
                textViewBadPercentage.setText(Html.fromHtml(htmlStringBad));
                //textViewBadPercentage.setText((int)(badPercent * 100) + "% Mal");
                textViewBadReport.setText(jsonObjectData.get("total_symptoms").toString() + " Relatórios");

                JSONObject jsonObjectDiseases = jsonObjectData.getJSONObject("diseases");

                int total = (int)totalNoSympton + (int)totalSympton;
                double diarreica = 0;

                if (total > 0) {
                    Double d;

                    diarreica = ((Integer.parseInt(jsonObjectDiseases.get("diarreica").toString()) * 100) / total);
                    d = new Double(diarreica);

                    textViewPercentage1.setText(Math.round(diarreica) + "%");
                    progressBar1.setProgress(d.intValue());

                    double exantematica = 0;

                    exantematica = ((Integer.parseInt(jsonObjectDiseases.get("exantematica").toString()) * 100) / total);
                    d = new Double(exantematica);

                    textViewPercentage2.setText(Math.round(exantematica) + "%");
                    progressBar2.setProgress(d.intValue());

                    double respiratoria = 0;

                    respiratoria = ((Integer.parseInt(jsonObjectDiseases.get("respiratoria").toString()) * 100) / total);
                    d = new Double(respiratoria);

                    textViewPercentage3.setText(Math.round(respiratoria) + "%");
                    progressBar3.setProgress(d.intValue());
                } else {
                    textViewPercentage1.setText("0%");
                    progressBar1.setProgress(0);

                    textViewPercentage2.setText("0%");
                    progressBar2.setProgress(0);

                    textViewPercentage3.setText("0%");
                    progressBar3.setProgress(0);
                }

                //Pie Char Config
                pieChart.setUsePercentValues(false);
                pieChart.setDescription("");
                pieChart.setDrawCenterText(false);
                pieChart.setDrawSliceText(false);
                pieChart.setDrawHoleEnabled(false);
                pieChart.setHoleColorTransparent(false);
                pieChart.setHoleRadius(7);
                pieChart.setTransparentCircleRadius(10);
                pieChart.setRotationAngle(0);
                pieChart.setClickable(false);
                pieChart.setRotationEnabled(false);

                setData(badPercent, goodPercent);
            }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

    private void setData(double badPercent, double goodPercent) {

        float[] yData = { (int)(badPercent * 100), (int)(goodPercent * 100)};
        String[] xData = { "Mal", "Bem" };

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(2);

        int colors[] = {Color.parseColor("#FF0000"),Color.parseColor("#CCCC00")};

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setDrawValues(false);
        data.setHighlightEnabled(false);

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.getLegend().setEnabled(false);
    }

    private MarkerOptions loadBadMarkerOption() {

        if (badMarkerOption == null) {
            badMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_bad_small));
        }

        return badMarkerOption;
    }

    private MarkerOptions loadGoodMarkerOption() {

        if (goodMarkerOption == null) {
            goodMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_good_small));
        }

        return goodMarkerOption;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        queryText = query;

        if (query.equals("")) {
            singleDTO.setDto(null);
            singleDTO.setLatLng(null);
        } else {
            singleDTO.setDto(query+"-BR");
            setLocationBySearch();
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);

        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    private String getStateDescription(String uf) {

            String stateDiscription = "";

            if (uf.equals("AC")) {
                stateDiscription = "Acre";
            } else if (uf.equals("AL")) {
                stateDiscription = "Alagoas";
            } else if (uf.equals("AP")) {
                stateDiscription = "Amapá";
            } else if (uf.equals("AM")) {
                stateDiscription = "Amazonas";
            } else if (uf.equals("BA")) {
                stateDiscription = "Bahia";
            } else if (uf.equals("CE")) {
                stateDiscription = "Ceará";
            } else if (uf.equals("DF")) {
                stateDiscription = "Distrito Federal";
            } else if (uf.equals("ES")) {
                stateDiscription = "Espirito Santo";
            } else if (uf.equals("GO")) {
                stateDiscription = "Goiás";
            } else if (uf.equals("MA")) {
                stateDiscription = "Maranhão";
            } else if (uf.equals("MT")) {
                stateDiscription = "Mato Grosso";
            } else if (uf.equals("MS")) {
                stateDiscription = "Mato Grosso do Sul";
            } else if (uf.equals("MG")) {
                stateDiscription = "Minas Gerais";
            } else if (uf.equals("PR")) {
                stateDiscription = "Paraná";
            } else if (uf.equals("PB")) {
                stateDiscription = "Paraiba";
            } else if (uf.equals("PA")) {
                stateDiscription = "Pará";
            } else if (uf.equals("PE")) {
                stateDiscription = "Pernambuco";
            } else if (uf.equals("PI")) {
                stateDiscription = "Piauí";
            } else if (uf.equals("RJ")) {
                stateDiscription = "Rio de Janeiro";
            } else if (uf.equals("RN")) {
                stateDiscription = "Rio Grande do Norte";
            } else if (uf.equals("RS")) {
                stateDiscription = "Rio Grande do Sul";
            } else if (uf.equals("RO")) {
                stateDiscription = "Rondônia";
            } else if (uf.equals("RR")) {
                stateDiscription = "Roraima";
            } else if (uf.equals("SC")) {
                stateDiscription = "Santa Catarina";
            } else if (uf.equals("SE")) {
                stateDiscription = "Sergipe";
            } else if (uf.equals("SP")) {
                stateDiscription = "São Paulo";
            } else if (uf.equals("TO")) {
                stateDiscription = "Tocantins";
            }

            return stateDiscription;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Map of Health Screen - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}