package com.epitrack.guardioes.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.manager.Loader;
import com.epitrack.guardioes.model.Point;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.utility.LocationUtility;
import com.epitrack.guardioes.view.base.AbstractBaseMapActivity;
import com.epitrack.guardioes.view.tip.Tip;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * @author Igor Morais / Miquéias Lopes
 */
public class MapPointActivity extends AbstractBaseMapActivity {

    @Bind(R.id.linear_layout)
    LinearLayout linearLayoutPoint;

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.text_view_address)
    TextView textViewAddress;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private MarkerOptions markerOption;
    private final Map<Marker, Point> pointMap = new HashMap<>();

    int tip;
    private LocationUtility locationUtility;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int REQUEST_LOCATION = 0;
    private Tracker mTracker;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.map_point);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        locationUtility = new LocationUtility(getApplicationContext());

        if (locationUtility.getLocation() == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapPointActivity.this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
            }
        } else {
            tip = getIntent().getIntExtra(Constants.Bundle.TIP, 0);

            final MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.fragment_map);

            mapFragment.getMapAsync(this);

            if (Tip.getBy(tip) == Tip.PHARMACY) {
                getSupportActionBar().setTitle(R.string.pharmacy);

                /*new DialogBuilder(MapPointActivity.this).load()
                        .title(R.string.attention)
                        .content(R.string.alert_pharmacy)
                        .positiveText(R.string.ok)
                        .show();*/

            } else if (Tip.getBy(tip) == Tip.HOSPITAL) {
                getSupportActionBar().setTitle(R.string.hospital);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        try {
            if (requestCode == REQUEST_LOCATION) {
                // BEGIN_INCLUDE(permission_result)
                // Received permission result for camera permission.

                // Check if the only required permission has been granted
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationUtility = new LocationUtility(getApplicationContext());

                    if (locationUtility.getLocation() == null) {
                        new DialogBuilder(MapPointActivity.this).load()
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
                        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                                .findFragmentById(R.id.fragment_map);

                        mapFragment.getMapAsync(this);
                    }
                } else {
                    new DialogBuilder(MapPointActivity.this).load()
                            .title(R.string.attention)
                            .content(R.string.network_disable)
                            .positiveText(R.string.ok)
                            .callback(new MaterialDialog.ButtonCallback() {

                                @Override
                                public void onPositive(final MaterialDialog dialog) {
                                    navigateTo(HomeActivity.class);
                                }
                            }).show();

                }
                // END_INCLUDE(permission_result)

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } catch (Exception e) {
            new DialogBuilder(MapPointActivity.this).load()
                    .title(R.string.attention)
                    .content(e.getMessage())
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            navigateTo(HomeActivity.class);
                        }

                    }).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Tip.getBy(tip) == Tip.PHARMACY) {
            mTracker.setScreenName("Pharmacy Screen - " + this.getClass().getSimpleName());
        } else {
            mTracker.setScreenName("UPAs Screen - " + this.getClass().getSimpleName());
        }
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
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

    @Override
    public void onMapReady(final GoogleMap map) {
        super.onMapReady(map);

        String tipMap;

        if(Tip.getBy(tip) == Tip.PHARMACY) {
            tipMap = "farmácias";
        } else {
            tipMap = "UPAs";
        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Carregando " + tipMap + " no mapa. Algumas farmácias podem não ser exibidas.", Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.blue_dark);
        snackbar.show();

        new MapAsyncTaskRunner().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private void loadPharmacy() {

        Loader.with().getHandler().post(new Runnable() {
            @Override
            public void run() {
                final List<Point> pointList = new ArrayList<Point>();

                SimpleRequester simpleRequester = new SimpleRequester();
                simpleRequester.setJsonObject(null);
                simpleRequester.setMethod(Method.GET);
                simpleRequester.setOtherAPI(true);
                simpleRequester.setUrl("https://maps.googleapis.com/maps/api/place/textsearch/json?query=pharmacy&location="+locationUtility.getLatitude()+","+locationUtility.getLongitude()+"&radius=10000&key=AIzaSyDYl7spN_NpAjAWL7Hi183SK2cApiIS3Eg");

                String jsonStr = null;
                try {
                    jsonStr = simpleRequester.execute(simpleRequester).get();

                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if (!jsonObject.get("status").toString().toUpperCase().equals("OK")) {
                        Toast.makeText(getApplicationContext(), R.string.generic_error, Toast.LENGTH_SHORT).show();
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonObject = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectGeometry = jsonObject.getJSONObject("geometry");
                            JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");

                            Point point = new Point();
                            point.setLatitude(jsonObjectLocation.getDouble("lat"));
                            point.setLongitude(jsonObjectLocation.getDouble("lng"));
                            point.setLogradouro(jsonObject.getString("formatted_address"));
                            point.setName(jsonObject.getString("name"));

                            pointList.add(point);
                        }

                        if (pointList.size() > 0) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    addMarker(pointList);
                                }
                            }, 2000);
                        }
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

    private void load() {

        Loader.with().getHandler().post(new Runnable() {

            @Override
            public void run() {

               /* try {
                    final ArrayList<Point> pointList = new ArrayList<Point>();


                    SimpleRequester simpleRequester = new SimpleRequester();
                    simpleRequester.setJsonObject(null);
                    simpleRequester.setMethod(Method.GET);
                    simpleRequester.setUrl(Requester.API_URL + "content/upas.json");

                    String jsonStr = simpleRequester.execute(simpleRequester).get();
                    //JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectUpa = jsonArray.getJSONObject(i);

                        Point point = new Point();

                        point.setLatitude(jsonObjectUpa.getDouble("latitude"));
                        point.setLongitude(jsonObjectUpa.getDouble("longitude"));

                        String logradouro = jsonObjectUpa.getString("logradouro");
                        String numero = jsonObjectUpa.getString("numero");
                        String bairro = jsonObjectUpa.getString("bairro");
                        String formattedAddress = logradouro + ", " + numero + " - " + bairro;

                        point.setLogradouro(formattedAddress);
                        point.setName(jsonObjectUpa.getString("name"));
                        pointList.add(point);
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            addMarker(pointList);
                        }

                    }, 2000);

                }  catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                try {

                    final InputStream inputStream = getAssets().open("upas.json");

                    final List<Point> pointList = new ObjectMapper().readValue(inputStream, new TypeReference<List<Point>>() {
                    });

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            addMarker(pointList);
                        }

                    }, 2000);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addMarker(final List<Point> pointList) {

        final Handler handler = new Handler(Looper.getMainLooper());

        for (final Point point : pointList) {

            handler.post(new Runnable() {

                @Override
                public void run() {

                    final LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());

                    final Marker marker = getMap().addMarker(getMarkerOption().position(latLng));

                    pointMap.put(marker, point);
                }
            });
        }
    }

    private MarkerOptions getMarkerOption() {

        if (Tip.getBy(tip) == Tip.PHARMACY) {
            if (markerOption == null) {
                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_pharmacy));
            }
        } else if (Tip.getBy(tip) == Tip.HOSPITAL) {
            if (markerOption == null) {
                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_hospital));
            }
        }

        return markerOption;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        final Point point = pointMap.get(marker);

        if (point != null) {
            textViewName.setText(point.getName());
            textViewAddress.setText(formatAddress(point));

            final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(final Animation animation) {

                    if (linearLayoutPoint.getVisibility() == View.INVISIBLE) {
                        linearLayoutPoint.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(final Animation animation) {

                }

                @Override
                public void onAnimationRepeat(final Animation animation) {

                }
            });

            linearLayoutPoint.startAnimation(animation);

            new DialogBuilder(MapPointActivity.this).load()
                    .title(R.string.attention)
                    .content(R.string.open_google_maps)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onNegative(final MaterialDialog dialog) {

                        }

                        @Override
                        public void onPositive(final MaterialDialog dialog) {
                            double latitude = point.getLatitude();
                            double longitude = point.getLongitude();
                            String label = point.getName();
                            String uriBegin = "geo:" + latitude + "," + longitude;
                            String query = latitude + "," + longitude + "(" + label + ")";
                            String encodedQuery = Uri.encode(query);
                            String uriString = uriBegin + "?q=" + encodedQuery + "&z=14";
                            Uri uri = Uri.parse(uriString);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }).show();
        }

        return true;
    }

    private String formatAddress(final Point point) {
        return point.getLogradouro() + ", " + point.getNumero();
    }

    private class MapAsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {


            if (Tip.getBy(tip) == Tip.PHARMACY) {
                loadPharmacy();
            } else if (Tip.getBy(tip) == Tip.HOSPITAL) {
                load();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MapPointActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(30, 136, 229)));
            progressDialog.setTitle(R.string.app_name);
            progressDialog.setMessage("Atualizando dados no mapa...");
            progressDialog.show();
        }
    }

}
