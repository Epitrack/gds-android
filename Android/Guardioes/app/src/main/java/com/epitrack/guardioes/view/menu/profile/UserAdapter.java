package com.epitrack.guardioes.view.menu.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.DateFormat;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Igor Morais
 */
public class UserAdapter extends ArrayAdapter<User> {

    private final UserListener listener;
    SingleUser singleUser = SingleUser.getInstance();
    private ArrayList<User> userArrayList;

    public UserAdapter(final Context context, final ArrayList<User> userList, final UserListener listener) {
        super(context, 0, userList);

        this.userArrayList = userList;
        this.listener = listener;
    }

    class ViewHolder {

        TextView textViewName;
        TextView textViewType;
        CircularImageView imageViewImage;
        ImageView imageTrash;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;
        User user;
        ViewHolder viewHolder;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            view = inflater.inflate(R.layout.user_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
            viewHolder.textViewType = (TextView) view.findViewById(R.id.text_view_type);
            viewHolder.imageViewImage = (CircularImageView) view.findViewById(R.id.image_view_image);

            view.findViewById(R.id.linear_layout).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View itemView) {
                    listener.onEdit(getItem(position));
                }
            });

            view.findViewById(R.id.image_view_trash).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View itemView) {
                    listener.onDelete(getItem(position));
                }
            });

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        user = userArrayList.get(position); //getItem(position);

        if (user.getId().equals(singleUser.getId())) {
            viewHolder.imageTrash = (ImageView) view.findViewById(R.id.image_view_trash);
            viewHolder.imageTrash.setVisibility(View.INVISIBLE);
        }

        viewHolder.textViewName.setText(user.getNick());
        viewHolder.textViewType.setText(user.getType());
        viewHolder.imageViewImage = SingleUser.getInstance().getImageProfile(viewHolder.imageViewImage, user);

        return view;
    }
}