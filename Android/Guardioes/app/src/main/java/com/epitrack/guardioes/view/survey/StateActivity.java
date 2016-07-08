package com.epitrack.guardioes.view.survey;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.helper.LocationHelper;
import com.epitrack.guardioes.helper.SocialShare;
import com.epitrack.guardioes.manager.LocationListener;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.SurveyRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.HomeActivity;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class StateActivity extends BaseAppCompatActivity {

    private String id;

    private final LoadDialog loadDialog = new LoadDialog();

    private final SingleUser singleUser = SingleUser.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        final boolean mainMember = getIntent().getBooleanExtra(Constants.Bundle.MAIN_MEMBER, false);

        id = mainMember ? singleUser.getId() : getIntent().getStringExtra("id_user");

        setContentView(R.layout.state);
    }

    @OnClick(R.id.text_view_state_good)
    public void onStateGood() {

        getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Survey State Good Button")
                    .build());

        if (!Dexter.isRequestOngoing()) {

            Dexter.checkPermissions(new MultiplePermissionsListener() {

                @Override
                public void onPermissionsChecked(final MultiplePermissionsReport permissionReport) {

                    if (permissionReport.areAllPermissionsGranted()) {

                        final LocationHelper locationHelper = new LocationHelper(StateActivity.this);

                        if (locationHelper.isEnabled()) {

                            loadDialog.show(getFragmentManager(), LoadDialog.TAG);

                            locationHelper.addListener(new LocationListener() {

                                @Override
                                public void onConnect(final Bundle bundle) {

                                }

                                @Override
                                public void onSuspend(final int i) {

                                }

                                @Override
                                public void onFail(final ConnectionResult connectionResult) {

                                }

                                @Override
                                public void onLastLocation(final Location location) {

                                    if (location != null) {

                                        locationHelper.disconnect();

                                        sendSurvey(new LatLng(location.getLatitude(), location.getLongitude()));
                                    }
                                }

                                @Override
                                public void onLocation(final Location location) {

                                    if (location != null) {

                                        locationHelper.disconnect();

                                        sendSurvey(new LatLng(location.getLatitude(), location.getLongitude()));
                                    }
                                }
                            });

                            locationHelper.connect();

                        } else {

                            new DialogBuilder(StateActivity.this).load()
                                                                 .content(R.string.location_disabled)
                                                                 .cancelable(false)
                                                                 .negativeText(R.string.not_now)
                                                                 .positiveText(R.string.setting_upper)
                                                                 .onPositive(new MaterialDialog.SingleButtonCallback() {

                                                                    @Override
                                                                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                                                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                                                    }

                                                                 }).show();
                        }
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> permissionList, final PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }

            }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void sendSurvey(final LatLng latLng) {

        new SurveyRequester(StateActivity.this).saveSurveyGood(new User(id), latLng, new RequestListener<Boolean>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(final Exception e) {
                loadDialog.dismiss();

                Toast.makeText(StateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(final Boolean result) {
                loadDialog.dismiss();

                navigateTo(ShareActivity.class);
            }
        });
    }

    @OnClick(R.id.text_view_state_bad)
    public void onStateBad() {

        getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Survey State Bad Button")
                    .build());

        if (!Dexter.isRequestOngoing()) {

            Dexter.checkPermissions(new MultiplePermissionsListener() {

                @Override
                public void onPermissionsChecked(final MultiplePermissionsReport permissionReport) {

                    if (permissionReport.areAllPermissionsGranted()) {

                        final LocationHelper locationHelper = new LocationHelper(StateActivity.this);

                        if (locationHelper.isEnabled()) {

                            final LoadDialog loadDialog = new LoadDialog();

                            loadDialog.show(getFragmentManager(), LoadDialog.TAG);

                            locationHelper.addListener(new LocationListener() {

                                @Override
                                public void onConnect(final Bundle bundle) {

                                }

                                @Override
                                public void onSuspend(final int i) {

                                }

                                @Override
                                public void onFail(final ConnectionResult connectionResult) {

                                }

                                @Override
                                public void onLastLocation(final Location location) {

                                    if (location != null) {

                                        loadDialog.dismiss();
                                        locationHelper.disconnect();

                                        navigateToSymptomActivity(new LatLng(location.getLatitude(), location.getLongitude()));
                                    }
                                }

                                @Override
                                public void onLocation(final Location location) {

                                    if (location != null) {

                                        loadDialog.dismiss();
                                        locationHelper.disconnect();

                                        navigateToSymptomActivity(new LatLng(location.getLatitude(), location.getLongitude()));
                                    }
                                }
                            });

                            locationHelper.connect();

                        } else {

                            new DialogBuilder(StateActivity.this).load()
                                    .content(R.string.location_disabled)
                                    .cancelable(false)
                                    .negativeText(R.string.not_now)
                                    .positiveText(R.string.setting_upper)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {

                                        @Override
                                        public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }

                                    }).show();
                        }
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> permissionList, final PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }

            }, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void navigateToSymptomActivity(final LatLng latLng) {

        final Bundle bundle = new Bundle();

        bundle.putString("id_user", id);
        bundle.putDouble("latitude", latLng.latitude);
        bundle.putDouble("longitude", latLng.longitude);

        navigateTo(SymptomActivity.class, bundle);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (SocialShare.getInstance().isShared()) {

            new DialogBuilder(StateActivity.this).load()
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

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Select State Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
