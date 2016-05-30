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

public class TrophyAdapter extends ArrayAdapter<String> {

    public TrophyAdapter(final Context context, final List<String> tagList) {
        super(context, 0, tagList);
    }

    private static class ViewHolder {

        TextView textViewName;
        ImageView imageViewImage;
        ImageView imageViewArrow;
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
                                 .inflate(R.layout.trophy_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.imageViewImage = (ImageView) view.findViewById(R.id.image);
            viewHolder.textViewName = (TextView) view.findViewById(R.id.name);
            viewHolder.imageViewArrow = (ImageView) view.findViewById(R.id.arrow);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }
}
