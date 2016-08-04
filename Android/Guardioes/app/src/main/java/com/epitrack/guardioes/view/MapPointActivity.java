package com.epitrack.guardioes.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.manager.Loader;
import com.epitrack.guardioes.model.Point;
import com.epitrack.guardioes.request.MapRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.AbstractBaseMapActivity;
import com.epitrack.guardioes.view.tip.Tip;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private MarkerOptions markerOption;

    private final Map<Marker, Point> pointMap = new HashMap<>();

    private Tip tip;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.map_point);

        tip = Tip.getBy(getIntent().getIntExtra(Constants.Bundle.TIP, 0));

        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (tip == Tip.PHARMACY) {
            getTracker().setScreenName("Pharmacy Screen - " + this.getClass().getSimpleName());

        } else {
            getTracker().setScreenName("UPAs Screen - " + this.getClass().getSimpleName());
        }

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onAnimationEnd(final LatLng latLng) {

        if (tip == Tip.HOSPITAL) {
            load();

        } else {
            loadPharmacy(latLng.latitude, latLng.longitude);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPharmacy(final double latitude, final double longitude) {

        new MapRequester(this).loadPharmacy(latitude, longitude, new RequestListener<List<Point>>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onSuccess(final List<Point> pointList) {
                addMarker(pointList);
            }


        });
    }

    private void load() {

        Loader.with().handler().post(new Runnable() {

            @Override
            public void run() {

                try {

                    final InputStream inputStream = getAssets().open("upas.json");

                    final List<Point> pointList = new ObjectMapper().readValue(inputStream, new TypeReference<List<Point>>() {
                    });

                    addMarker(pointList);

                } catch (final IOException e) {
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

        if (tip == Tip.PHARMACY) {

            if (markerOption == null) {
                markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_pharmacy));
            }

        } else if (tip == Tip.HOSPITAL) {

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
            textViewAddress.setText(format(point));

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
                            String uriString = uriBegin + "?q=" + Uri.encode(query) + "&z=14";
                            Uri uri = Uri.parse(uriString);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }

                    }).show();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        navigateTo(HomeActivity.class);
    }

    private String format(final Point point) {
        return point.getLogradouro() + ", " + point.getNumero();
    }
}
