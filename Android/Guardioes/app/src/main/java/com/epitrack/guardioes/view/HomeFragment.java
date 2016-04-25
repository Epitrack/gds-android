package com.epitrack.guardioes.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.AvatarHelper;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.diary.DiaryActivity;
import com.epitrack.guardioes.view.menu.profile.ProfileActivity;
import com.epitrack.guardioes.view.survey.SelectParticipantActivity;
import com.epitrack.guardioes.view.tip.TipActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.image_view_image)
    CircularImageView imageViewImage;

    private final SingleUser singleUser = SingleUser.getInstance();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, Bundle bundle) {
        return inflater.inflate(R.layout.home_fragment, viewGroup, false);
    }

    @Override
    public void onActivityCreated(final Bundle bundle) {
        super.onActivityCreated(bundle);

        setShowTitle(false);
        setShowLogo(true);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle bundle) {

        bind(view);

        textViewName.setText(getString(R.string.message_hello, singleUser.getNick()));
    }

    @Override
    public void onResume() {
        super.onResume();

        new AvatarHelper().loadImage(getActivity(), imageViewImage, SingleUser.getInstance());
    }

    @OnClick(R.id.image_view_image)
    public void showProfile() {
        navigateTo(ProfileActivity.class);
    }

    @OnClick(R.id.text_view_notice)
    public void onNotice() {

        getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Notice Button")
                    .build());

        navigateTo(NoticeActivity.class);
    }

    @OnClick(R.id.text_view_map)
    public void onMap() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Map Button")
                .build());

        navigateTo(MapSymptomActivity.class);
    }

    @OnClick(R.id.text_view_tip)
    public void onTip() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Tip Button")
                .build());

        navigateTo(TipActivity.class);
    }

    @OnClick(R.id.text_view_diary)
    public void onDiary() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Diary of Health Button")
                .build());

        navigateTo(DiaryActivity.class);
    }

    @OnClick(R.id.text_view_join)
    public void onJoin() {

        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Survey Button")
                .build());

        navigateTo(SelectParticipantActivity.class);
    }
}
