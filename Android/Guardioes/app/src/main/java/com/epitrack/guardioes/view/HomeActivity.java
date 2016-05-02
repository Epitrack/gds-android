package com.epitrack.guardioes.view;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.manager.PrefManager;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.view.menu.Home;
import com.epitrack.guardioes.view.menu.profile.ProfileActivity;
import com.epitrack.guardioes.view.welcome.WelcomeActivity;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final Class<? extends Fragment> HOME_FRAGMENT = HomeFragment.class;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.list_view)
    ListView listView;

    private ActionBarDrawerToggle drawerToggle;

    private final Map<String, Fragment> fragmentMap = new HashMap<>();
    public static final String PREFS_NAME = "preferences_user_token";

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.home_activity);

        ButterKnife.bind(this);

        loadView();

        listView.setAdapter(new HomeAdapter(this, Home.values()));
        listView.setOnItemClickListener(this);
    }

    private void loadView() {

        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.image_logo_small);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(drawerToggle);

        addFragment(HOME_FRAGMENT, HOME_FRAGMENT.getSimpleName());
    }

    @Override
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);

        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return drawerToggle.onOptionsItemSelected(menuItem) || super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (getCurrentFragment().getTag().equals(HOME_FRAGMENT.getSimpleName())) {

            onExit();

        } else {

            replaceFragment(HOME_FRAGMENT, HOME_FRAGMENT.getSimpleName());
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {

        drawerLayout.closeDrawer(GravityCompat.START);

        final Home home = (Home) adapterView.getItemAtPosition(position);

        if (home == Home.PROFILE) {

            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));

        } else if (home == Home.FACEBOOK) {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Help Contact Facebook Button")
                    .build());

            final Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("https://www.facebook.com/minsaude"));

            startActivity(intent);

        } else if (home == Home.TWITTER) {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Help Contact Twitter Button")
                    .build());

            final Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("https://twitter.com/minsaude"));

            startActivity(intent);

        } else if (home == Home.EXIT) {

            onExit();

        } else if (home.isFragment()) {

            if (!home.getTag().equals(getCurrentFragment().getTag())) {

                replaceFragment(home.getType(), home.getTag());
            }
        }
    }

    private Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.frame_layout);
    }

    private void addFragment(final Class<? extends Fragment> fragmentClass, final String tag) {

        final Fragment fragment = Fragment.instantiate(this, fragmentClass.getName());

        getFragmentManager().beginTransaction()
                .add(R.id.frame_layout, fragment, tag)
                .commit();

        fragmentMap.put(tag, fragment);
    }

    private void replaceFragment(final Class<?> fragmentClass, final String tag) {

        Fragment fragment = fragmentMap.get(tag);

        if (fragment == null) {

            fragment = Fragment.instantiate(this, fragmentClass.getName());

            fragmentMap.put(tag, fragment);
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment, tag)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Home Screen - " + this.getClass().getSimpleName());

        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void onExit() {

        new DialogBuilder(HomeActivity.this).load()
                .title(R.string.attention)
                .content(R.string.exit_message)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (new PrefManager(getApplicationContext()).remove(Constants.Pref.USER)) {

                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("preferences_user_token", "");
                            editor.commit();

                            SingleUser.getInstance().clean();

                            final Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        }
                    }

                }).show();
    }

    public Tracker getTracker() {

        if (tracker == null) {
            tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.analytics);
        }

        return tracker;
    }
}
