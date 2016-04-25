package com.epitrack.guardioes.view.diary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.helper.AvatarHelper;
import com.epitrack.guardioes.helper.ViewUtility;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.view.survey.ParentListener;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private static final int SELECT = 0;

    private static final float MARGIN_LARGE = 16;
    private static final float MARGIN_SMALL = 8;

    private ViewHolder viewHolder;

    private final ParentListener listener;

    private List<User> userList = new ArrayList<>();

    private Context context;
    private SingleUser singleUser = SingleUser.getInstance();

    public MemberAdapter(final Context context, final ParentListener listener, final List<User> parentList) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.userList = parentList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.text_view_name)
        TextView textViewName;

        @Bind(R.id.image_view_image)
        CircularImageView imageViewPhoto;

        @Bind(R.id.view_select)
        View viewSelect;

        @Bind(R.id.text_id_view)
        TextView textViewId;

        private boolean select;

        public ViewHolder(final View view) {
            super(view);

            ButterKnife.bind(this, view);

            imageViewPhoto.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {

            if (viewSelect.getVisibility() == View.INVISIBLE) {

                viewSelect.setVisibility(View.VISIBLE);
                viewHolder.viewSelect.setVisibility(View.INVISIBLE);

                viewHolder = this;

                listener.onParentSelect(viewHolder.textViewId.getText().toString());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.diary_member_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if (position == SELECT) {

            ViewUtility.setMarginLeft(viewHolder.imageViewPhoto,
                                      ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_LARGE));

            viewHolder.viewSelect.setVisibility(View.VISIBLE);

            this.viewHolder = viewHolder;

        } else {

            ViewUtility.setMarginLeft(viewHolder.imageViewPhoto,
                    ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_SMALL));
        }

        if (position == getItemCount() - 1) {
            ViewUtility.setMarginRight(viewHolder.imageViewPhoto,
                    ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_LARGE));

        } else {

            ViewUtility.setMarginRight(viewHolder.imageViewPhoto,
                                       ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_SMALL));
        }

        final User user = userList.get(position);

        viewHolder.textViewId.setText(user.getId());
        viewHolder.textViewName.setText(user.getNick());

        new AvatarHelper().loadImage(context, viewHolder.imageViewPhoto, user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
