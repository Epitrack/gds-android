package com.epitrack.guardioes.view.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.game.model.Answer;

/**
 * @author Igor Morais
 */
public class AnswerAdapter extends ArrayAdapter<Answer> {

    public AnswerAdapter(final Context context) {
        super(context, 0);
    }

    @Override
    public int getCount() {
        return 9;
    }

    public static class ViewHolder {

        ImageView imageViewAnswer;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {

        View view = convertView;

        ViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.answer_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.imageViewAnswer = (ImageView) view.findViewById(R.id.answer);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }


        return view;
    }
}
