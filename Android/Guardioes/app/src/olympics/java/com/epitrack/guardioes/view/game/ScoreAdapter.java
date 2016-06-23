package com.epitrack.guardioes.view.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.game.model.Score;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {

    public ScoreAdapter(final Context context, final List<Score> scoreList) {
        super(context, 0, scoreList);
    }

    private static class ViewHolder {

        TextView textViewName;
        TextView textViewNumber;
        ImageView imageViewImage;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.score_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.name);
            viewHolder.textViewNumber = (TextView) view.findViewById(R.id.number);
            viewHolder.imageViewImage = (ImageView) view.findViewById(R.id.image);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        final Score score = getItem(position);

        viewHolder.textViewName.setText(score.getCountry());
        viewHolder.textViewNumber.setText(String.valueOf(position + 1));

        Picasso.with(viewGroup.getContext())
                .load(score.getUrl())
                .into(viewHolder.imageViewImage);

        return view;
    }
}
