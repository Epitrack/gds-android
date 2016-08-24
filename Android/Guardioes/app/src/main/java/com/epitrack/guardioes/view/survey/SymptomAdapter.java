package com.epitrack.guardioes.view.survey;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SymptomList;
import com.epitrack.guardioes.view.menu.profile.InterestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Morais
 */
public class SymptomAdapter extends ArrayAdapter<SymptomList> {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private static final int VIEW_TYPE_AMOUNT = 2;

    private static final int POSITION_VIEW_TYPE_OTHER = 13;

    final private List<SymptomList> symptomArray;

    public SymptomAdapter(final Context context, final List<SymptomList> symptomArray) {
        super(context, 0, symptomArray);

        this.symptomArray = symptomArray;
    }

    public static class ViewHolder {
        CheckBox checkBoxSymptom;
        EditText editTextCountry;
        //TextView textViewName;
    }

    @Override
    public int getCount() {
        return symptomArray.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_AMOUNT;
    }

    @Override
    public int getItemViewType(final int position) {
        return (position == POSITION_VIEW_TYPE_OTHER ? VIEW_TYPE_OTHER: VIEW_TYPE_ITEM) - 1;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;

        if (getItemViewType(position) == VIEW_TYPE_OTHER - 1) {

            return LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.symptom_other, viewGroup, false);

        } else {

            final ViewHolder viewHolder;

            if (view == null) {

                view = LayoutInflater.from(viewGroup.getContext())
                                     .inflate(R.layout.symptom_item, viewGroup, false);

                viewHolder = new ViewHolder();

                viewHolder.checkBoxSymptom = (CheckBox) view.findViewById(R.id.check_box_symptom);

                viewHolder.checkBoxSymptom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        symptomArray.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    }
                });

                view.setTag(viewHolder);
                view.setTag(R.id.check_box_symptom, viewHolder.checkBoxSymptom);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final SymptomList symptomList = position > POSITION_VIEW_TYPE_OTHER ? this.symptomArray.get(position - 1) : this.symptomArray.get(position);

            if (position > POSITION_VIEW_TYPE_OTHER) {
                //viewHolder.textViewName.setTag(position - 1);
                viewHolder.checkBoxSymptom.setTag(position - 1);
            } else {
                //viewHolder.textViewName.setTag(position);
                viewHolder.checkBoxSymptom.setTag(position);
            }

            //viewHolder.textViewName.setText(symptomList.getNome());
            viewHolder.checkBoxSymptom.setChecked(symptomList.isSelected());
            viewHolder.checkBoxSymptom.setText("" + symptomList.getNome() + "");
            viewHolder.checkBoxSymptom.setTextColor(Color.parseColor("#757575"));
            viewHolder.checkBoxSymptom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            viewHolder.checkBoxSymptom.setCompoundDrawablePadding(50);
            viewHolder.checkBoxSymptom.setPadding(50,0,100,0);


            return view;
        }
    }
}
