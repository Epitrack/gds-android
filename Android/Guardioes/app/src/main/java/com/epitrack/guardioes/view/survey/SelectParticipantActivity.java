package com.epitrack.guardioes.view.survey;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.AvatarHelper;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DateFormat;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.epitrack.guardioes.view.menu.profile.UserActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;

import java.util.List;

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

    @Bind(R.id.image_view_image)
    CircularImageView imageViewAvatar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    SingleUser singleUser = SingleUser.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.select_participant);

        int j = DateFormat.getDateDiff(singleUser.getDob());

        textViewName.setText(singleUser.getNick());
        textViewAge.setText(j + " "+this.getString(R.string.anos));

        new AvatarHelper().loadImage(this, imageViewAvatar, singleUser);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loadHousehold();
    }

    private void loadHousehold() {

        final String novo_integrante = this.getString(R.string.adicionar_novo_integrante);

        new UserRequester(this).getAllHousehold(singleUser.getId(), new RequestListener<List<User>>() {

            final LoadDialog loadDialog = new LoadDialog();

            @Override
            public void onStart() {
                loadDialog.show(getFragmentManager(), LoadDialog.TAG);
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final List<User> parentList) {
                loadDialog.dismiss();

                parentList.add(new User("    "+novo_integrante, "", "-1", "", "", "", R.drawable.img_add_profile));

                recyclerView.setAdapter(new ParentAdapter(SelectParticipantActivity.this, SelectParticipantActivity.this, parentList));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTracker().setScreenName("Select Participant Survey Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    //@OnClick(R.id.button_add)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Add New Member Button")
                .build());

        if (recyclerView.getAdapter().getItemCount() - 1 == Constants.MAX_USER) {

            new DialogBuilder(this).load()
                    .title(R.string.app_name)
                    .content(R.string.message_user_max)
                    .positiveText(R.string.ok)
                    .show();

        } else {

            final Bundle bundle = new Bundle();

            bundle.putBoolean(Constants.Bundle.NEW_MEMBER, true);
            bundle.putBoolean(Constants.Bundle.SOCIAL_NEW, false);

            navigateTo(UserActivity.class, bundle);
        }
    }

    @OnClick(R.id.image_view_image)
    public void onUserSelect() {

        getTracker().send(new HitBuilders.EventBuilder()
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

            navigateTo(UserActivity.class, bundle);

        } else {

            getTracker().send(new HitBuilders.EventBuilder()
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
