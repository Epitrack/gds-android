package com.epitrack.guardioes.view.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.game.model.Phase;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

public class TrophyAdapter extends ArrayAdapter<Phase> {

    public TrophyAdapter(final Context context, final List<Phase> phaseList) {
        super(context, 0, phaseList);
    }

    private static class ViewHolder {

        TextView textViewName;
        CircularImageView imageViewImage;
        ImageView imageViewArrow;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.trophy_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.name);
            viewHolder.imageViewImage = (CircularImageView) view.findViewById(R.id.image);
            viewHolder.imageViewArrow = (ImageView) view.findViewById(R.id.arrow);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        final Phase phase = getItem(position);

        viewHolder.textViewName.setText(phase.getName());
        viewHolder.imageViewImage.setImageResource(phase.getImage());

        return view;
    }
}
