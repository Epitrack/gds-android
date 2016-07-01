package com.epitrack.guardioes.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.Country;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<Country> {

    public CountryAdapter(final Context context, final List<Country> countryList) {
        super(context, 0, countryList);
    }

    private static class ViewHolder {

        TextView textViewName;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_preview_white, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.name);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textViewName.setText(getItem(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(final int position, final View convertView, final ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        View view = convertView;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_select, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.name);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textViewName.setText(getItem(position).getName());

        return view;
    }
}
