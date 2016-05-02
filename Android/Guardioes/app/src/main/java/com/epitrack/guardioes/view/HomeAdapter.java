package com.epitrack.guardioes.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.menu.Home;

public class HomeAdapter extends ArrayAdapter<Home> {

    public HomeAdapter(final Context context, final Home[] homeArray) {
        super(context, 0, homeArray);
    }

    private class ViewHolder {

        TextView textViewName;
        ImageView imageViewIcon;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.home_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.name);
            viewHolder.imageViewIcon = (ImageView) view.findViewById(R.id.icon);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        final Home home = getItem(position);

        viewHolder.textViewName.setText(home.getName());
        viewHolder.imageViewIcon.setBackgroundResource(home.getIcon());

        return view;
    }
}
