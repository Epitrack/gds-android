package com.epitrack.guardioes.view.survey;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.service.AnalyticsApplication;
import com.epitrack.guardioes.utility.BitmapUtility;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.menu.profile.Avatar;
import com.epitrack.guardioes.view.menu.profile.UserActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class SelectParticipantActivity extends BaseAppCompatActivity implements ParentListener {

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.text_view_age)
    TextView textViewAge;

    @Bind(R.id.image_view_photo)
    ImageView imageViewAvatar;
    //com.pkmmte.view.CircularImageView imageViewAvatar;
    //de.hdodenhof.circleimageview.CircleImageView imageViewAvatar;
    //ImageView imageViewAvatar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.text_view_id)
    TextView textViewId;

    public static List<User> parentList = new ArrayList<>();
    SingleUser singleUser = SingleUser.getInstance();
    private Tracker mTracker;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.select_participant);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        //Miquéias Lopes
        int j = DateFormat.getDateDiff(singleUser.getDob());

        textViewName.setText(singleUser.getNick());
        textViewAge.setText(j + " Anos");
        textViewId.setText(singleUser.getId());

        if (singleUser.getPicture().length() > 2) {
            Uri uri = Uri.parse(singleUser.getPicture());

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int densityDpi = (int) (metrics.density * 160f);
            int width = 0;
            int height = 0;

            if (densityDpi == DisplayMetrics.DENSITY_LOW) {
                width = 90;
                height = 90;
            } else if (densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
                width = 120;
                height = 120;
            } else if (densityDpi == DisplayMetrics.DENSITY_HIGH) {
                width = 180;
                height = 180;
            } else if (densityDpi >= DisplayMetrics.DENSITY_XHIGH) {
                width = 240;
                height = 240;
            }
            imageViewAvatar.getLayoutParams().width = width;
            imageViewAvatar.getLayoutParams().height = height;
            imageViewAvatar.setImageURI(uri);

            File file = new File(singleUser.getPicture());

            if (!file.exists()) {
                imageViewAvatar.setImageURI(uri);
                Drawable drawable = imageViewAvatar.getDrawable();
                imageViewAvatar.setImageDrawable(drawable);

                if (drawable == null) {
                    setDefaultAvatar();
                }
            } else {
                imageViewAvatar.setImageURI(uri);
            }
        } else {
            if (Integer.parseInt(singleUser.getPicture()) == 0) {
                setDefaultAvatar();
            } else {
                try {
                    imageViewAvatar.setImageResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getLarge());
                } catch (Exception e) {
                    setDefaultAvatar();
                }
            }
        }

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loadHousehold();
    }

    private void setDefaultAvatar() {
        int age = DateFormat.getDateDiff(singleUser.getDob());

        if (singleUser.getGender().equals("F")) {
            if (singleUser.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                if(age > 49) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_3);
                } else if(age > 25) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_2);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.avatar_1);
                }
            }
            else if(singleUser.getRace().equals("amarelo"))
            {
                if(age > 49) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_9);
                } else if(age > 25) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_8);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.avatar_7);
                }
            }
            else if(singleUser.getRace().equals("branco"))
            {
                if(age > 49) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_14);
                } else if(age > 25) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_8);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.avatar_13);
                }
            }
        } else if (singleUser.getGender().equals("M")) {
            if (singleUser.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                if(age > 49) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_6);
                } else if(age > 25) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_5);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.avatar_4);
                }
            }
            else if(singleUser.getRace().equals("amarelo"))
            {
                if(age > 49) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_12);
                } else if(age > 25) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_11);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.avatar_10);
                }
            } else if(singleUser.getRace().equals("branco")) {
                if(age > 49) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_16);
                } else if(age > 25) {
                    imageViewAvatar.setImageResource(R.drawable.avatar_11);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.avatar_15);
                }
            }
        }
    }

    private void loadHousehold() {
        recyclerView.setAdapter(new ParentAdapter(getApplicationContext(), this, parentList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Select Participant Survey Screen - " + this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        loadHousehold();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadHousehold();
    }

    //@OnClick(R.id.button_add)
    public void onAdd() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Add New Member Button")
                .build());

        User user = new User();
        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
        bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
        navigateTo(UserActivity.class, bundle);
    }

    @OnClick(R.id.image_view_photo)
    public void onUserSelect() {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Select Main User Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);
        navigateTo(StateActivity.class, bundle);
    }


    @Override
    public void onParentSelect(String id) {
        if (id.equals("-1")) {
            final Bundle bundle = new Bundle();

            bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
            bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);
            navigateTo(UserActivity.class, bundle);
        } else {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Survey Select Household Button")
                    .build());

            final Bundle bundle = new Bundle();

            bundle.putString("id_user", id);
            bundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
            navigateTo(StateActivity.class, bundle);
        }
    }
}
