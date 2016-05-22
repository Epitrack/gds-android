package com.epitrack.guardioes.view.menu.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.IMenu;

/**
 * @author Igor Morais
 */
public class HelpAdapter extends ArrayAdapter<IMenu> {

    public HelpAdapter(final Context context, final IMenu[] menuArray) {
        super(context, 0, menuArray);
    }

    public static class ViewHolder {

        TextView textViewName;
        ImageView imageViewIcon;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;

        ViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.help_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
            viewHolder.imageViewIcon = (ImageView) view.findViewById(R.id.image_view_icon);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        final IMenu menu = getItem(position);

        viewHolder.textViewName.setText(menu.getName());
        viewHolder.imageViewIcon.setImageResource(menu.getIcon());

        return view;
    }
}
