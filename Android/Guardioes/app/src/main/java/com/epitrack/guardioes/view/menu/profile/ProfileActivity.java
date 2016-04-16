package com.epitrack.guardioes.view.menu.profile;

import android.os.Bundle;

import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DialogBuilder;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class ProfileActivity extends BaseAppCompatActivity implements UserListener {

    @Bind(R.id.list_view)
    ListView listView;

    private SingleUser singleUser = SingleUser.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.profile_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTracker().setScreenName("List Profile Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());

        new UserRequester(this).getAllProfiles(SingleUser.getInstance().getId(), new UserHandler());
    }

    private class UserHandler implements RequestListener<List<User>> {

        private final LoadDialog loadDialog = new LoadDialog();

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

            final SingleUser user = SingleUser.getInstance();

            parentList.add(0, new User(R.drawable.image_avatar_small_2,
                    user.getNick(),
                    user.getEmail(),
                    user.getId(),
                    user.getDob(),
                    user.getRace(),
                    user.getGender(),
                    user.getPicture(),
                    "",
                    user.getFile()));

            listView.setAdapter(new UserAdapter(ProfileActivity.this, parentList, ProfileActivity.this));
        }
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
    @OnClick(R.id.button_add)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
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

        getTracker().send(new HitBuilders.EventBuilder()
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
        if (singleUser.getId().equals(user.getId())) {
            bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);
        }
        navigateTo(UserActivity.class, bundle);
    }

    @Override
    public void onDelete(final User user) {
        //Miquéias Lopes

        getTracker().send(new HitBuilders.EventBuilder()
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

                            } catch (Exception e) {
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
}
