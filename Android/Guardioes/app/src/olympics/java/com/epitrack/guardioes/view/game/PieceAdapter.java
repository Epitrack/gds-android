package com.epitrack.guardioes.view.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.epitrack.guardioes.R;

import java.util.Map;

/**
 * @author Igor Morais
 */
public class PieceAdapter extends ArrayAdapter<Integer> {

    private final Map<Integer, Boolean> pieceMap;

    public PieceAdapter(final Context context, final Map<Integer, Boolean> pieceMap, final Integer[] pieceArray) {
        super(context, 0, pieceArray);

        this.pieceMap = pieceMap;
    }

    private static class ViewHolder {

        ImageView imageViewPiece;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {

        View view = convertView;

        ViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.piece_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.imageViewPiece = (ImageView) view.findViewById(R.id.piece);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        if (pieceMap.get(position)) {

            viewHolder.imageViewPiece.setImageResource(getItem(position));
        }

        return view;
    }
}
