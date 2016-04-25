package com.epitrack.guardioes.helper;

import android.content.Context;
import android.widget.ImageView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.User;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class AvatarHelper {

    public void loadImage(final Context context, final ImageView view, final User user) {

        if (user.getImage() != 0) {

            view.setImageResource(user.getImage());

        } else {

            final int width = view.getWidth();
            final int height = view.getHeight();

            Picasso.with(context).load(Constants.PATH + user.getPath())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(width, height)
                    .centerCrop()
                    .into(view);
        }
    }

    public void setAvatar(final ImageView imageView, final User user) {

        final int age = DateFormat.getDateDiff(user.getDob());

        if (user.getGender().equals("F")) {

            if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {

                if (age > 49) {
                    imageView.setImageResource(R.drawable.avatar_3);

                } else if (age > 25) {
                    imageView.setImageResource(R.drawable.avatar_2);

                } else {
                    imageView.setImageResource(R.drawable.avatar_1);
                }

            } else if (user.getRace().equals("amarelo")) {

                if (age > 49) {
                    imageView.setImageResource(R.drawable.avatar_9);

                } else if (age > 25) {
                    imageView.setImageResource(R.drawable.avatar_8);

                } else {
                    imageView.setImageResource(R.drawable.avatar_7);
                }

            } else if (user.getRace().equals("branco")) {

                if (age > 49) {
                    imageView.setImageResource(R.drawable.avatar_14);

                } else if (age > 25) {
                    imageView.setImageResource(R.drawable.avatar_8);

                } else {
                    imageView.setImageResource(R.drawable.avatar_13);
                }
            }

        } else if (user.getGender().equals("M")) {

            if (user.getRace().equals("preto") || user.getRace().equals("indigena") || user.getRace().equals("pardo")) {

                if (age > 49) {
                    imageView.setImageResource(R.drawable.avatar_6);

                } else if (age > 25) {
                    imageView.setImageResource(R.drawable.avatar_5);

                } else {
                    imageView.setImageResource(R.drawable.avatar_4);
                }

            } else if (user.getRace().equals("amarelo")) {

                if (age > 49) {
                    imageView.setImageResource(R.drawable.avatar_12);

                } else if (age > 25) {
                    imageView.setImageResource(R.drawable.avatar_11);

                } else {
                    imageView.setImageResource(R.drawable.avatar_10);
                }

            } else if (user.getRace().equals("branco")) {

                if (age > 49) {
                    imageView.setImageResource(R.drawable.avatar_16);

                } else if (age > 25) {
                    imageView.setImageResource(R.drawable.avatar_11);

                } else {
                    imageView.setImageResource(R.drawable.avatar_15);
                }
            }
        }
    }
}
