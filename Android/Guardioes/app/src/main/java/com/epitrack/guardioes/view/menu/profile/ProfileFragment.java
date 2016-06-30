package com.epitrack.guardioes.view.menu.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Constants;
import com.epitrack.guardioes.helper.DialogBuilder;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.UserRequester;
import com.epitrack.guardioes.request.base.Method;
import com.epitrack.guardioes.request.base.RequestListener;
import com.epitrack.guardioes.request.old.Requester;
import com.epitrack.guardioes.request.old.SimpleRequester;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.dialog.LoadDialog;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class ProfileFragment extends BaseFragment implements UserListener {

    @Bind(R.id.list_view)
    ListView listView;

    private SingleUser singleUser = SingleUser.getInstance();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {
        return inflater.inflate(R.layout.profile, viewGroup, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle bundle) {

        bind(view);

    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("List Profile Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());

        new UserRequester(getActivity()).getAllProfiles(SingleUser.getInstance().getId(), new UserHandler());
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

            final SingleUser single = SingleUser.getInstance();

            final User user = new User(single.getNick(),
                    single.getEmail(),
                    single.getId(),
                    single.getDob(),
                    single.getRace(),
                    single.getGender(),
                    single.getImage());

            user.setPath(single.getPath());

            parentList.add(0, user);

            listView.setAdapter(new UserAdapter(getActivity(), parentList, ProfileFragment.this));
        }
    }

    @Override
    @OnClick(R.id.button_save)
    public void onAdd() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Add New Member Button")
                .build());

        if (listView.getAdapter().getCount() == Constants.MAX_USER) {

            new DialogBuilder(getActivity()).load()
                    .title(R.string.app_name)
                    .content(R.string.message_user_max)
                    .positiveText(R.string.ok)
                    .show();

        } else {

            navigateTo(UserActivity.class);
        }
    }

    @Override
    public void onEdit(final User user) {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Edit Profile Button")
                .build());

        final Bundle bundle = new Bundle();

        bundle.putParcelable(Constants.Bundle.USER, Parcels.wrap(user));

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

            new DialogBuilder(getActivity()).load()
                    .title(R.string.attention)
                    .content(R.string.not_remove_member)
                    .positiveText(R.string.ok)
                    .show();
        } else {

            new DialogBuilder(getActivity()).load()
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
                                Toast.makeText(getActivity(), R.string.generic_error + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }).show();
        }
    }

    private void refresh(boolean error) {

        if (error) {

            Toast.makeText(getActivity(), R.string.generic_error, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getActivity(), R.string.delete_user, Toast.LENGTH_SHORT).show();

            new UserRequester(getActivity()).getAllProfiles(SingleUser.getInstance().getId(), new UserHandler());
        }
    }
}
