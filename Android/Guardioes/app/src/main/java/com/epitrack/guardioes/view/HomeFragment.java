package com.epitrack.guardioes.view;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.view.base.BaseFragment;
import com.epitrack.guardioes.view.diary.DiaryActivity;
import com.epitrack.guardioes.view.menu.profile.Avatar;
import com.epitrack.guardioes.view.menu.profile.ProfileActivity;
import com.epitrack.guardioes.view.survey.SelectParticipantActivity;
import com.epitrack.guardioes.view.tip.TipActivity;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.analytics.HitBuilders;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.image_view_photo)
    CircularImageView imageViewPhoto;

    @Bind(R.id.linear_layout_menu_home)
    LinearLayout linearLayoutMenuHome;

    final SingleUser singleUser = SingleUser.getInstance();

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

        //loadImageProfile();
        //loadImage(imageViewPhoto);

        textViewName.setText(getString(R.string.message_hello, singleUser.getNick()));

        resizeBackgroundMenu();

        imageViewPhoto = singleUser.getImageProfile(imageViewPhoto, null);
    }

    private void resizeBackgroundMenu() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int) (metrics.density * 160f);
        int width = 0;
        int height = 0;

        if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            //width = 299;
            height = 298;
        } else {
            //width = 597;
            height = 596;
        }

        ViewGroup.LayoutParams params= linearLayoutMenuHome.getLayoutParams();
        params.height = height;
        linearLayoutMenuHome.setLayoutParams(params);
    }

    private void loadImageProfile() {

       try {
           String picture = singleUser.getPicture();

           if (singleUser.getPicture().length() > 2) {

               Uri uri = Uri.parse(singleUser.getPicture());

               setImageViewSize(imageViewPhoto);

               File file = new File(singleUser.getPicture());

               if (!file.exists()) {
                   imageViewPhoto.setImageURI(uri);
                   Drawable drawable = imageViewPhoto.getDrawable();
                   imageViewPhoto.setImageDrawable(drawable);

                   if (drawable == null) {
                       setDefaultAvatar();
                   }
               } else {
                   imageViewPhoto.setImageURI(uri);
               }
           } else {

               if (singleUser.getPicture().equals("")) {
                   singleUser.setPicture("0");
               }
               if (Integer.parseInt(singleUser.getPicture()) == 0) {
                   setDefaultAvatar();
               } else {
                   imageViewPhoto.setImageResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getLarge());
               }
           }
       } catch (Exception e) {
           setDefaultAvatar();

       }
    }

    private void setDefaultAvatar() {

        Log.e("",singleUser.toString());
        setImageViewSize(imageViewPhoto);

        int age = DateFormat.getDateDiff(singleUser.getDob());

        if (singleUser.getGender().equals("F")) {
            if (singleUser.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                if(age > 49) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_3);
                } else if(age > 25) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_2);
                } else {
                    imageViewPhoto.setImageResource(R.drawable.avatar_1);
                }
            }
            else if(singleUser.getRace().equals("amarelo"))
            {
                if(age > 49) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_9);
                } else if(age > 25) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_8);
                } else {
                    imageViewPhoto.setImageResource(R.drawable.avatar_7);
                }
            }
            else if(singleUser.getRace().equals("branco"))
            {
                if(age > 49) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_14);
                } else if(age > 25) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_8);
                } else {
                    imageViewPhoto.setImageResource(R.drawable.avatar_13);
                }
            }
        } else if (singleUser.getGender().equals("M")) {
            if (singleUser.getRace().equals("preto") || singleUser.getRace().equals("indigena") || singleUser.getRace().equals("pardo")) {
                if(age > 49) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_6);
                } else if(age > 25) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_5);
                } else {
                    imageViewPhoto.setImageResource(R.drawable.avatar_4);
                }
            }
            else if(singleUser.getRace().equals("amarelo"))
            {
                if(age > 49) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_12);
                } else if(age > 25) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_11);
                } else {
                    imageViewPhoto.setImageResource(R.drawable.avatar_10);
                }
            } else if(singleUser.getRace().equals("branco")) {
                if(age > 49) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_16);
                } else if(age > 25) {
                    imageViewPhoto.setImageResource(R.drawable.avatar_11);
                } else {
                    imageViewPhoto.setImageResource(R.drawable.avatar_15);
                }
            }
        }
    }

    private void setImageViewSize(com.github.siyamed.shapeimageview.CircularImageView imageViewPhoto) {

      /*  DisplayMetrics metrics = getResources().getDisplayMetrics();
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
        } else if (densityDpi == DisplayMetrics.DENSITY_XHIGH) {
            width = 250;
            height = 250;
        } else if (densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
            width = 360;
            height = 360;
        } else if (densityDpi >= DisplayMetrics.DENSITY_XXXHIGH) {
            width = 400;
            height = 400;
        } else {
            width = 300;
            height = 300;
        }

        imageViewPhoto.getLayoutParams().width = width;
        imageViewPhoto.getLayoutParams().height = height;
        */
    }


    private void loadImage(final View view) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int width = view.getWidth();
                final int height = view.getHeight();

                /*Picasso.with(getActivity()).load(Constants.PATH + profile.getUser().getPhoto())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(width, height)
                        .centerCrop()
                        .into(imageViewPhoto);*/

                loadImageProfile();

                if (!singleUser.getPicture().equals("0")) {

                    if (!singleUser.getFile().equals("")) {
                        Uri uri = Uri.parse(singleUser.getFile());

                        setImageViewSize(imageViewPhoto);

                        File file = new File(singleUser.getFile());

                        if (!file.exists()) {
                            imageViewPhoto.setImageURI(uri);
                            Drawable drawable = imageViewPhoto.getDrawable();
                            imageViewPhoto.setImageDrawable(drawable);
                        }
                    }
                } else {
                    setDefaultAvatar();
                    //imageViewPhoto.setImageResource(Avatar.getBy(Integer.parseInt("2")).getLarge());
                }

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @OnClick(R.id.image_view_photo)
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
