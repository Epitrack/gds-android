package com.epitrack.guardioes.view.tip;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.Logger;
import com.epitrack.guardioes.helper.ViewUtility;
import com.epitrack.guardioes.view.IMenu;

import java.util.Map;

/**
 * @author Igor Morais
 */
public class TipAdapter extends BaseExpandableListAdapter {

    private static final String TAG = TipAdapter.class.getSimpleName();

    private static final int ELEVATION = 1;
    private static final int MARGIN = 5;

    private final Tip[] tipArray;
    private final Map<Integer, IMenu[]> menuMap;

    public TipAdapter(final Tip[] tipArray, final Map<Integer, IMenu[]> menuMap) {

        this.tipArray = tipArray;
        this.menuMap = menuMap;
    }

    @Override
    public int getGroupCount() {
        return tipArray.length;
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        return menuMap.get(tipArray[groupPosition].getId()) == null ? 0 : menuMap.get(tipArray[groupPosition].getId()).length;
    }

    @Override
    public Object getGroup(final int groupPosition) {
        return tipArray[groupPosition];
    }

    @Override
    public Object getChild(final int groupPosition, final int childPosition) {
        return menuMap.get(tipArray[groupPosition].getId())[childPosition];
    }

    @Override
    public long getGroupId(final int groupPosition) {
        return tipArray[groupPosition].getId();
    }

    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        return menuMap.get(tipArray[groupPosition].getId())[childPosition].getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;

        ItemViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.tip_item, viewGroup, false);

            viewHolder = new ItemViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
            viewHolder.imageViewIcon = (ImageView) view.findViewById(R.id.image_view_icon);
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ItemViewHolder) view.getTag();
        }

        if (menuMap.get(tipArray[groupPosition].getId()) == null) {
            Logger.logDebug(TAG, "The menu is null.");

        } else {

            if (isExpanded) {

                ViewUtility.setMarginBottom(viewHolder.linearLayout, 0);
                viewHolder.linearLayout.setBackgroundResource(R.drawable.background_rounded_top_white);

            } else {

                ViewUtility.setMarginBottom(viewHolder.linearLayout, ViewUtility.toPixel(viewGroup.getContext(), MARGIN));
                viewHolder.linearLayout.setBackgroundResource(R.drawable.background_rounded_white);
            }
        }

        viewHolder.textViewName.setText(tipArray[groupPosition].getName());
        viewHolder.imageViewIcon.setImageResource(tipArray[groupPosition].getIcon());

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;

        SubItemViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.tip_phone_item, viewGroup, false);

            viewHolder = new SubItemViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);

            view.setTag(viewHolder);

        } else {

            viewHolder = (SubItemViewHolder) view.getTag();
        }

        if (isLastChild) {

            ViewUtility.setMarginBottom(viewHolder.linearLayout, ViewUtility.toPixel(viewGroup.getContext(), MARGIN));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.linearLayout.setElevation(ViewUtility.toPixel(viewGroup.getContext(), ELEVATION));
            }

            viewHolder.linearLayout.setBackgroundResource(R.drawable.background_rounded_bottom_white);

        } else {

            ViewUtility.setMarginBottom(viewHolder.linearLayout, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.linearLayout.setElevation(0);
            }

            viewHolder.linearLayout.setBackgroundResource(android.R.color.white);
        }

        final IMenu menu = menuMap.get(tipArray[groupPosition].getId())[childPosition];

        viewHolder.textViewName.setText(menu.getName());

        return view;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return true;
    }

    private static class ItemViewHolder {

        TextView textViewName;
        ImageView imageViewIcon;
        LinearLayout linearLayout;
    }

    private static class SubItemViewHolder {

        TextView textViewName;
        LinearLayout linearLayout;
    }
}
