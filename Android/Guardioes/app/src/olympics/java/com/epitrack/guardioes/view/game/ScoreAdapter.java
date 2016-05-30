package com.epitrack.guardioes.view.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<String> {

    public ScoreAdapter(final Context context, final List<String> tagList) {
        super(context, 0, tagList);
    }

    private static class ViewHolder {

        TextView textViewName;
        TextView textViewNumber;
        ImageView imageViewImage;
    }

    @Override
    public int getCount() {
        return 10;
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

        return view;
    }
}
